package com.swj.es.study.index.engine;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/20 10:37
 */
@Slf4j
public class TextParseUtils {
    private static final Analyzer analyzer = new StandardAnalyzer();

    public static String stemWord(String rawWord) {
        EnglishStemmer stemmer = new EnglishStemmer();
        stemmer.setCurrent(rawWord);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public static List<String> tokenizer(String rawText) {
        if (rawText == null || rawText.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> tokenWords = new LinkedList<>();
        // todo : analyzer 这里使用 final 变量是否有问题
        try (TokenStream tokenStream = analyzer.tokenStream(null, rawText)) {
            CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = termAttribute.toString();
                if(null == term) {
                    continue;
                }
                term = term.replaceAll("[^a-zA-Z ]","");
                if(term.isEmpty()) {
                    continue;
                }
                tokenWords.add(term);
            }
            tokenStream.end();
        } catch (IOException e) {
            log.error("failed to tokenizer the rawText. [rawText='{}']", rawText, e);
        }
        return tokenWords;
    }
}
