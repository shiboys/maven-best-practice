package com.swj.es.study.index.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/21 17:48
 * the posting list of a single word, regardless of a word that contains in any documents
 */
public class TermDocumentPostingList {
    String word;
    List<DocumentPosting> postingList;
    List<ParsedDocument> documentList;

    public TermDocumentPostingList(String word) {
        this.word = word;
        this.postingList = new LinkedList<>();
        this.documentList = new LinkedList<>();
    }

    public void addTermToPosting(TermPosition termPosition, ParsedDocument parsedDocument) {
        postingList.add(new DocumentPosting(termPosition,parsedDocument));
        documentList.add(parsedDocument);
    }

    public String getWord() {
        return word;
    }

    public List<DocumentPosting> getPostingList() {
        return postingList;
    }

    public List<ParsedDocument> getDocumentList() {
        return documentList;
    }
}
