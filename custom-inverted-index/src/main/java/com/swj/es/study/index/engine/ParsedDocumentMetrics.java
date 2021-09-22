package com.swj.es.study.index.engine;

import com.google.common.collect.ImmutableMap;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/21 18:11
 * the metrics of a parsed document, including the tfidf score and the magnitude(模) of a parsed document
 */
public class ParsedDocumentMetrics {
    private int totalDocumentSize;
    private ParsedDocument parsedDocument;
    // the true posing list in memory.
    private ImmutableMap<String,TermDocumentPostingList> termPostingsMap;


}
