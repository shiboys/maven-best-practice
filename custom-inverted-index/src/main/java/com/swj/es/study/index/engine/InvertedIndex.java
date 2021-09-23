package com.swj.es.study.index.engine;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.swj.es.study.index.Document;
import com.swj.es.study.index.SearchResult;
import com.swj.es.study.index.SearchResultBatch;
import com.swj.es.study.index.SearchStats;
import com.swj.es.study.index.TextSearchIndex;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/22 11:56
 */
public class InvertedIndex implements TextSearchIndex {

  private static int THREAD_POOL_SIZE = Math.max(1, Runtime.getRuntime().availableProcessors());

  private Corpus corpus;

  private ImmutableMap<String, TermDocumentPostingList> termPostingListMap;
  private ImmutableMap<ParsedDocument, ParsedDocumentMetrics> documentMetricsMap;
  private DocumentParser searchingTermParser = null;
  private ExecutorService threadPool;

  public InvertedIndex(Corpus corpus) {
    this.corpus = corpus;
    init();
    searchingTermParser = new DocumentParser(false, false);
    threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
  }

  private void init() {
    // build the most important term posting map
    Map<String, TermDocumentPostingList> postingListMap = new HashMap<>();
    for (ParsedDocument parsedDocument : corpus.getParsedDocuments()) {
      for (TermPosition termPosition : parsedDocument.getDocumentTerms()) {
        final String word = termPosition.getWord();
        postingListMap.putIfAbsent(word, new TermDocumentPostingList(word)).addTermToPosting(termPosition,
            parsedDocument);
      }
    }
    termPostingListMap = ImmutableMap.copyOf(postingListMap);
    // init the document metrics
    Map<ParsedDocument, ParsedDocumentMetrics> metricsMap = new HashMap<>();
    for (ParsedDocument parsedDocument : corpus.getParsedDocuments()) {
      metricsMap.put(parsedDocument, new ParsedDocumentMetrics(corpus.size(), parsedDocument, termPostingListMap));
    }
    documentMetricsMap = ImmutableMap.copyOf(metricsMap);
  }

  @Override
  public SearchResultBatch search(String searchTerm, int maxResults) {
    Stopwatch stopwatch = Stopwatch.createStarted();

    Document searchingDocument = new Document(searchTerm, null);
    ParsedDocument searchingParsedDocument = searchingTermParser.parseDocument(searchingDocument);
    Set<ParsedDocument> relevantDocumentSet = getRelevantDocumentSet(searchingParsedDocument);
    if (relevantDocumentSet.isEmpty()) {
      return buildSearchResult(stopwatch.elapsed(TimeUnit.NANOSECONDS), Collections.emptyList(), 0);
    }
    // do scan all parsed document and compare them with target document;
    List<SearchResult> searchResultList = new LinkedList<>();
    ParsedDocumentMetrics searchingDocumentMetrics =
        new ParsedDocumentMetrics(corpus.size(), searchingParsedDocument, termPostingListMap);
    for (ParsedDocument parsedDocument : relevantDocumentSet) {
      SearchResult searchResult = new SearchResult();
      searchResult.setUniqueIdentifier(parsedDocument.getDocId());
      searchResult.setRelevanceScore(computeCosine(searchingDocumentMetrics, parsedDocument));
      searchResultList.add(searchResult);
    }

    searchResultList.sort(Comparator.comparing(SearchResult::getRelevanceScore).reversed());

    if (maxResults < searchResultList.size()) {
      searchResultList = searchResultList.subList(0, maxResults);
    }

    return buildSearchResult(stopwatch.elapsed(TimeUnit.NANOSECONDS), searchResultList, relevantDocumentSet.size());
  }

  private double computeCosine(ParsedDocumentMetrics searchingDocumentMetrics, ParsedDocument targetDocument) {
    ParsedDocumentMetrics targetDocumentMetrics = documentMetricsMap.get(targetDocument);
    Set<String> searchingUniqueWordSet = searchingDocumentMetrics.getParsedDocument().getUniqueWords();
    // 这里通过使用 size 比较来获取 目标词结果集是合理的，是可以被验证的
    if (targetDocument.getUniqueWords().size() < searchingUniqueWordSet.size()) {
      searchingUniqueWordSet = targetDocument.getUniqueWords();
    }
    double cosine = 0;
    for (String word : searchingUniqueWordSet) {
      double score = (searchingDocumentMetrics.getTfIdfScore(word) * searchingDocumentMetrics.getDocumentMagnitude()) /
          (targetDocumentMetrics.getTfIdfScore(word) * targetDocumentMetrics.getDocumentMagnitude());
      cosine = cosine + score;
    }
    return cosine;
  }


  private SearchResultBatch buildSearchResult(long elapsedNanoSeconds, List<SearchResult> searchResultList,
      int nDocumentSearched) {
    SearchStats searchStats = new SearchStats();
    searchStats.setDurationNanos(elapsedNanoSeconds);
    searchStats.setDocumentsSearched(nDocumentSearched);
    SearchResultBatch searchResult = new SearchResultBatch();
    searchResult.setSearchStats(searchStats);
    searchResult.setSearchResultList(searchResultList);
    return searchResult;
  }


  private Set<ParsedDocument> getRelevantDocumentSet(ParsedDocument searchingParsedDocument) {
    Set<ParsedDocument> relevantDocumentSet = new HashSet<>();
    for (TermPosition term : searchingParsedDocument.getDocumentTerms()) {
      if (termPostingListMap.containsKey(term.getWord())) {
        // 将这个词的所有相关文档从 倒排列表里面返回
        relevantDocumentSet.addAll(termPostingListMap.get(term.getWord()).getUniqueDocuments());
      }
    }
    return relevantDocumentSet;
  }

  @Override
  public int documentCount() {
    return corpus.size();
  }

  @Override
  public int termCount() {
    return termPostingListMap.keySet().size();
  }
}
