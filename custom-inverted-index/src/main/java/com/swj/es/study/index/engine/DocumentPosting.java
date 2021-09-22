package com.swj.es.study.index.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/21 17:34
 * 倒排元素，因为文档和 term 都已经封装的比较好了，这里采用了广义的倒排元素，只有 term 和 document
 */
@Getter
@AllArgsConstructor
public class DocumentPosting {
    private TermPosition term;
    private ParsedDocument document;
}
