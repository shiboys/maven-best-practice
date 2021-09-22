package com.swj.es.study.index.engine;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/21 18:08
 * the warp class of parsed document list
 */
public class Corpus {

    private List<ParsedDocument> parsedDocuments;

    public Corpus(List<ParsedDocument> parsedDocuments) {
        Preconditions.checkNotNull(parsedDocuments);
        Preconditions.checkState(!parsedDocuments.isEmpty());
        this.parsedDocuments = parsedDocuments;
    }

    public List<ParsedDocument> getParsedDocuments() {
        return parsedDocuments;
    }

    public int size() {
        return parsedDocuments.size();
    }
}
