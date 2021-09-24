package com.swj.es.study.test.index;

import static org.hamcrest.CoreMatchers.is;

import com.swj.es.study.index.Document;
import com.swj.es.study.index.SearchIndexFactory;
import com.swj.es.study.index.SearchResult;
import com.swj.es.study.index.SearchResultBatch;
import com.swj.es.study.index.TextSearchIndex;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @author shiweijie
 * @version 1.0.0
 * @since 2021/09/24 11:55
 */
public class SimpleTextSearchTest {

  @Test
  public void testSearchIndexWithExcept() {
    String doc1 = "Shall I compare thee to a summer's day? \n" +
        "Thou art more lovely and more temperate:\n" +
        "Rough winds do shake the darling buds of May,\n" +
        "And summer's lease hath all too short a date: \n" +
        "Sometime too hot the eye of heaven shines,\n" +
        "And often is his gold complexion dimm'd; \n" +
        "And every fair from fair sometime declines,\n" +
        "By chance or nature's changing course untrimm'd;\n" +
        "But thy eternal summer shall not fade\n" +
        "Nor lose possession  possession of that fair thou owest;\n" +
        "Nor shall Death brag thou wander'st in his shade,\n" +
        "When in eternal lines to time thou growest: \n" +
        "So long as men can breathe or eyes can see,\n" +
        "So long lives this and this gives life to thee.\n";

    String doc2 = "Let me not to the marriage of true minds\n" +
        "Admit impediments. Love is not love\n" +
        "Which alters when it alteration finds,\n" +
        "Or bends with the remover to remove:\n" +
        "O no! it is an ever-fixed mark \n" +
        "That looks on tempests and is never shaken;\n" +
        "It is the star to every wandering bark,\n" +
        "Whose worth's unknown, possession although his height be taken.\n" +
        "Love's not Time's fool, though rosy lips and cheeks \n" +
        "Within his bending sickle's compass come: \n" +
        "Love alters not with his brief hours and weeks, \n" +
        "But bears it out even to the edge of doom.\n" +
        "If this be error and upon me proved,\n" +
        "I never writ, nor no man ever loved. ";

    String doc3 = "My mistress' eyes are nothing like the sun;\n" +
        "Coral is far more red than her lips' red;\n" +
        "If snow be white, why then her breasts are dun;\n" +
        "If hairs be wires, black wires grow on her head.\n" +
        "I have seen roses damask'd, red and white,\n" +
        "But no such roses see I possession in her cheeks; \n" +
        "And in some perfumes is there more delight\n" +
        "Than in the breath that from my mistress reeks.\n" +
        "I love to hear her speak, yet well I know\n" +
        "That music hath a far more pleasing sound;\n" +
        "I grant I never saw a goddess go;\n" +
        "My mistress, when she walks, treads on the ground:\n" +
        "And yet, by heaven, I think my love as rare\n" +
        "As any she belied with false compare. \n";

    String doc4 = "The expense of spirit in a waste of shame\n" +
        "Is lust in action; and till action, lust\n" +
        "Is perjured, murderous, bloody, full of blame,\n" +
        "Savage, extreme, rude, cruel, not to trust,\n" +
        "Enjoy'd no sooner but despised straight,\n" +
        "Past reason hunted, and no sooner had\n" +
        "Past reason hated, as a swallow'd bait\n" +
        "On purpose laid to make the taker mad;\n" +
        "Mad in pursuit and in possession possession so;\n" +
        "Had, having, and in quest to have, extreme;\n" +
        "A bliss in proof, and proved, a very woe;\n" +
        "Before, a joy proposed; behind, a dream.\n" +
        "All this the world well knows; yet none knows well\n" +
        "To shun the heaven that leads men to this hell.";

    List<Document> documentList = new LinkedList<>();
    documentList.add(new Document(doc1, 1));
    documentList.add(new Document(doc2, 2));
    documentList.add(new Document(doc3, 3));
    documentList.add(new Document(doc4, 4));

    TextSearchIndex textSearchIndex = SearchIndexFactory.buildIndex(documentList);
    String searchedText = "Mad in pursuit and in possession so";
    SearchResultBatch searchResult = textSearchIndex.search(searchedText, Integer.MAX_VALUE);

    Assert.assertThat(searchResult.getSearchResultList().get(0).getUniqueIdentifier(), is(4));

    for (SearchResult result : searchResult.getSearchResultList()) {
      System.out.println(String.format("docId=%s,score=%s", result.getUniqueIdentifier(), result.getRelevanceScore()));
    }
  }
}
