package com.swj.es.study.test.index;

import com.swj.es.study.index.Document;
import com.swj.es.study.index.engine.DocumentParser;
import com.swj.es.study.index.engine.ParsedDocument;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/23 19:39
 */
public class DocumentParserTest {

  @Test
  public void testParseHtml() {
    String rawText = "Body=\"<p><img src=\"http://i.stack.imgur.com/M64qK.png\" alt=\"animal king takeover\"></p>\n" +
        "\n" +
        "<p>In this image we see six flags behind the animal king.\n" +
        "I think I recognize some of them:\n" +
        "First - Argentina;\n" +
        "Third - Bulgaria;\n" +
        "Fifth - Sweden;\n" +
        "Sixth - Austria;</p>\n" +
        "\n" +
        "<p>Am I correct, and which countries' flags are the second and fourth?</p>\n" +
        "\"";
    DocumentParser documentParser = new DocumentParser(true, true);
    ParsedDocument parsedDocument = documentParser.parseDocument(new Document(rawText, 1));
    boolean findCommaSplitter = false;
    for (String word : parsedDocument.getUniqueWords()) {
      if (word.equals(";")) {
        findCommaSplitter = true;
      }
    }

    Assert.assertFalse (findCommaSplitter);
  }
}