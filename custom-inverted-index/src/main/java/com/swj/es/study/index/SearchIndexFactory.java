package com.swj.es.study.index;

import com.google.common.collect.Lists;
import com.swj.es.study.index.engine.Corpus;
import com.swj.es.study.index.engine.DocumentParser;
import com.swj.es.study.index.engine.InvertedIndex;
import com.swj.es.study.index.engine.ParsedDocument;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/24 10:33
 */
@Slf4j
public class SearchIndexFactory {

  private SearchIndexFactory() {
    throw new UnsupportedOperationException();
  }
  public static TextSearchIndex buildIndex(Collection<Document> documents) {
    Collection<ParsedDocument> parsedDocuments = buildDocumentsParallel(documents);
    Corpus corpus = new Corpus(new LinkedList<>(parsedDocuments));
    return new InvertedIndex(corpus);
  }

  private static Collection<ParsedDocument> buildDocumentsParallel(Collection<Document> documents) {
    int coreThreadCount = Math.max(1, Runtime.getRuntime().availableProcessors());
    List<Thread> threadList = new LinkedList<>();
    List<Document> documentList = new LinkedList<>(documents);
    List<ParsedDocument> parsedDocumentList = new LinkedList<>();
    final DocumentParser defaultParser = new DocumentParser(true, true);
    for (final List<Document> subDocumentList : Lists.partition(documentList, coreThreadCount)) {
      Thread thread = new Thread(() -> parsedDocumentList.addAll(
          subDocumentList.stream().map(defaultParser::parseDocument).collect(Collectors.toList())));
      threadList.add(thread);
      thread.start();
    }
    for (Thread t : threadList) {
      try {
        t.join();
      } catch (InterruptedException e) {
        log.error("failed to parse the document.", e);
      }
    }
    return parsedDocumentList;
  }
}
