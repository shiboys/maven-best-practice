package com.swj.es.study.index.engine;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/21 18:11
 * the metrics of a parsed document, including the tfidf score and the magnitude(模) of a parsed document
 */
public class ParsedDocumentMetrics {
    private int totalDocumentSize;
    private ParsedDocument parsedDocument;
    // the real posting list in memory.
    private ImmutableMap<String, TermDocumentPostingList> termPostingsMap;

    // metrics
    private Double magnitude;
    private ImmutableMap<String /*word*/, Double /*score*/> tfIdfCache;

    public ParsedDocumentMetrics(int totalDocumentSize, ParsedDocument parsedDocument, ImmutableMap<String, TermDocumentPostingList> termPostingsMap) {
        this.totalDocumentSize = totalDocumentSize;
        this.parsedDocument = parsedDocument;
        this.termPostingsMap = termPostingsMap;

        Map<String, Double> tempTfIdfMap = new HashMap<>();
        for (String word : parsedDocument.getUniqueWords()) {
            double tfIdfScore = calcTfIdfScore(word);
            tempTfIdfMap.put(word, tfIdfScore);
        }
        tfIdfCache = ImmutableMap.copyOf(tempTfIdfMap);
        // calc the magnitude;
        getDocumentMagnitude();
    }

    public double getTfIdfScore(String word) {
        Double score = tfIdfCache.get(word);
        return score == null ? 0 : score;
    }

    public ParsedDocument getParsedDocument() {
        return parsedDocument;
    }

    public double getDocumentMagnitude() {
        if (magnitude == null) {
            double sumMagnitude = 0;
            for (Map.Entry<String, Double> wordEntry : tfIdfCache.entrySet()) {
                sumMagnitude += wordEntry.getValue() * wordEntry.getValue();
            }
            magnitude = Math.sqrt(sumMagnitude);
        }
        return magnitude;
    }

    private double calcTfIdfScore(String word) {
        // term frequency
        int wordFrequency = parsedDocument.getWordFrequency(word);
        if (wordFrequency == 0) {
            return 0;
        }

        return wordFrequency * calcInverseDocumentFrequency(word);
    }

    private double calcInverseDocumentFrequency(String word) {
        int oneTermDocumentsNum = getDocumentCountByTerm(word);
        return Math.log(totalDocumentSize * 1.0 / (1 + oneTermDocumentsNum));
    }

    private int getDocumentCountByTerm(String word) {
        if (!termPostingsMap.containsKey(word)) {
            return 0;
        }
        return termPostingsMap.get(word).getDocumentList().size();
    }
}
