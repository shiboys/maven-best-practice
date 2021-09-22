package com.swj.es.study.index;

import lombok.Data;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/22 11:41
 */
@Data
public class SearchResult {
    private double relevanceScore;
    private Object uniqueIdentifier;
}
