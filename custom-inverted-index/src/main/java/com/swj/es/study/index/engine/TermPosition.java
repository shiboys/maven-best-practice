package com.swj.es.study.index.engine;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/19 19:07
 */
public class TermPosition {

    private String  word;
    private Integer positionInDoc;

    public TermPosition(String word, Integer positionInDoc) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(word));
        this.word = word;
        this.positionInDoc = positionInDoc;
    }

    public String getWord() {
        return word;
    }

    public Integer getPositionInDoc() {
        return positionInDoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermPosition that = (TermPosition) o;
        return Objects.equals(word, that.word) &&
                Objects.equals(positionInDoc, that.positionInDoc);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,37)
                .append(word)
                .append(positionInDoc)
                .build();
    }
}
