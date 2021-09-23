package com.swj.es.study.index.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/21 17:48
 * the posting list of a single word, regardless of a word that contains in any documents
 */
public class TermDocumentPostingList {
    String word;
    List<DocumentPosting> postingList;
    Set<ParsedDocument> uniqueDocuments;

    public TermDocumentPostingList(String word) {
        this.word = word;
        this.postingList = new LinkedList<>();
        this.uniqueDocuments = new HashSet<>();
    }

    public void addTermToPosting(TermPosition termPosition, ParsedDocument parsedDocument) {
        postingList.add(new DocumentPosting(termPosition,parsedDocument));
        uniqueDocuments.add(parsedDocument);
    }

    public String getWord() {
        return word;
    }

    public List<DocumentPosting> getPostingList() {
        return postingList;
    }

    public Set<ParsedDocument> getUniqueDocuments() {
        return uniqueDocuments;
    }
}
