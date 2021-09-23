package com.swj.es.study.index.engine;

import com.swj.es.study.index.Document;
import org.jsoup.Jsoup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/20 12:23
 */
public class DocumentParser {
    private ConcurrentHashMap<String, String> stringPool;
    private boolean parseHtml;
    private boolean useStringPool;

    public DocumentParser(boolean parseHtml, boolean useStringPool) {
        this.parseHtml = parseHtml;
        this.useStringPool = useStringPool;
        if (useStringPool) {
            stringPool = new ConcurrentHashMap<>();
        }
    }

    /**
     * parse a target document to a parsedDocument.
     *
     * @param document representative of an physical document
     * @return
     */

    public ParsedDocument parseDocument(Document document) {
        List<TermPosition> termList = getTermsFromDocument(document);
        return new ParsedDocument(termList, document.getUniqueIdentifier());
    }

    private List<TermPosition> getTermsFromDocument(Document document) {

        if (document == null) {
            return Collections.emptyList();
        }
        String rawText = document.getRawText();
        if (parseHtml) {
            rawText = Jsoup.parse(document.getRawText()).text();
        }
        if (rawText == null) {
            rawText = "";
        }
        // to lower case
        rawText = rawText.toLowerCase();

        // 这里没有使用静态方法，是因为 util 的 analyzer 对象必须是非静态的
        List<String> wordList = TextParseUtils.tokenizer(rawText);
        List<TermPosition> termPositionList = new LinkedList<>();
        int position = 0;
        for (String word : wordList) {
            String stemWord = TextParseUtils.stemWord(word);
            // filter out the stop words
            if (StopWordHelper.isStopWords(word)) {
                continue;
            }
            String strToUse = stemWord;
            if (useStringPool) {
                if (!stringPool.containsKey(stemWord)) {
                    // 感觉这里有问题，todo：验证下，应该是 stringPool.put(stemWord,work);
                    stringPool.put(stemWord, stemWord);
                }
                strToUse = stringPool.get(stemWord);
            }
            termPositionList.add(new TermPosition(strToUse, position++));
        }
        return termPositionList;
    }

}
