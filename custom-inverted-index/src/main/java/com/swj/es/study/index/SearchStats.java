package com.swj.es.study.index;

import lombok.Data;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/22 11:51
 */
@Data
public class SearchStats {
    private int documentsSearched;
    private int durationNanos;
}
