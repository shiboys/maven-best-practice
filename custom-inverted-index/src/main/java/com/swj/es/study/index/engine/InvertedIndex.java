package com.swj.es.study.index.engine;

import com.google.common.collect.ImmutableMap;
import com.swj.es.study.index.SearchResultBatch;
import com.swj.es.study.index.TextSearchIndex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/22 11:56
 */
public class InvertedIndex implements TextSearchIndex {

  private Corpus corpus;

  private ImmutableMap<String, TermDocumentPostingList> termPostingListMap;
  private ImmutableMap<ParsedDocument, ParsedDocumentMetrics> documentMetricsMap;

  public InvertedIndex(Corpus corpus) {
    this.corpus = corpus;
    init();
  }

  private void init() {
    // build the most important term posting
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
    return null;
  }

  @Override
  public int documentCount() {
    return 0;
  }

  @Override
  public int termCount() {
    return 0;
  }
}
