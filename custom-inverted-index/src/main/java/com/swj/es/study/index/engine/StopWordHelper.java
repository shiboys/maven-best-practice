package com.swj.es.study.index.engine;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/20 12:27
 */
@Slf4j
public class StopWordHelper {

    public static Set<String> getAllDefaultStopWords() {
        ClassLoader classLoader = StopWordHelper.class.getClassLoader();
        String filePath = "stopwords/en.txt";
        InputStream fileWordsStream = classLoader.getResourceAsStream(filePath);
        if (fileWordsStream == null) {
            return Collections.emptySet();
        }
        Set<String> stopWords = new HashSet<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileWordsStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim().toLowerCase();// remove empty line
                if (line.isEmpty()) {
                    continue;
                }
                stopWords.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            log.error("failed to parse the stop word file.[filePath='{}']", filePath, e);
        }
        return stopWords;
    }
}
