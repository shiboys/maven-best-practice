package com.swj.es.study.index.engine;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/19 19:23
 */
public class ParsedDocument {
    private Object docId;
    private ImmutableMap<String, Integer> wordFrequencyMap;
    private ImmutableList<TermPosition> documentTerms;

    public ParsedDocument(List<TermPosition> termList, Object uniqueId) {
        Preconditions.checkNotNull(uniqueId);
        Preconditions.checkNotNull(termList);
        this.docId = uniqueId;
        this.documentTerms = ImmutableList.copyOf(termList);
        Map<String, Integer> wordCountMap = new HashMap<>();
        for (TermPosition term : termList) {
            if (!wordCountMap.containsKey(term.getWord())) {
                wordCountMap.put(term.getWord(), 0);
            }
            int count = wordCountMap.get(term.getWord());
            wordCountMap.put(term.getWord(), count + 1);
        }
        wordFrequencyMap = ImmutableMap.copyOf(wordCountMap);
    }

    public int getWordFrequency(String word) {
        return wordFrequencyMap.getOrDefault(word, 0);
    }

    public boolean isEmpty() {
        return documentTerms != null && !documentTerms.isEmpty();
    }

    public Set<String> getUniqueWords() {
        return wordFrequencyMap.keySet();
    }

    public Object getDocId() {
        return docId;
    }

    public ImmutableList<TermPosition> getDocumentTerms() {
        return documentTerms;
    }
}
