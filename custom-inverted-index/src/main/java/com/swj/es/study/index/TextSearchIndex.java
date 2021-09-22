package com.swj.es.study.index;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/22 11:40
 */
public interface TextSearchIndex {
    SearchResultBatch search(String searchTerm,int maxResults);

    int documentCount();

    int termCount();
}
