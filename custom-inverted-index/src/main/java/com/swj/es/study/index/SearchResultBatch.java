package com.swj.es.study.index;

import lombok.Data;

import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/22 11:50
 */
@Data
public class SearchResultBatch {
    SearchStats searchStats;
    List<SearchResult> searchResultList;
}
