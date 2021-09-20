package com.swj.es.study.index;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/19 18:40
 */
public class Document {
    private String rawText;
    private Object uniqueIdentifier;

    /**
     *
     * @param rawText          the raw text of this document
     * @param uniqueIdentifier (optional) a unique identifier for this document. Used in Search result to
     *                         refer back to original data
     */
    public Document(String rawText, Object uniqueIdentifier) {
        this.rawText = rawText;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getRawText() {
        return rawText;
    }

    public Object getUniqueIdentifier() {
        return uniqueIdentifier;
    }
}
