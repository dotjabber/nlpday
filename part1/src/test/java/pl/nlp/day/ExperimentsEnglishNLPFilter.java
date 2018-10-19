package pl.nlp.day;

import org.junit.Test;
import pl.nlp.day.nlp.EnglishNLPFilter;
import pl.nlp.day.nlp.NLPFilter;
import pl.nlp.day.nlp.PoSPart;

/**
 * Klasa z przykładami jak używać English NLP Filter
 */
public class ExperimentsEnglishNLPFilter {

    @Test
    public void test_getTokens(){
        String text = "Right now the KICK-OFF MEETING - an informational seminar for participants of the Polish-American Innovation Week in California - is taking place in Warsaw.";

        NLPFilter filter = new EnglishNLPFilter();

        for (String token : filter.getTokens(text)){
            System.out.println(token);
        }

    }

    @Test
    public void test_getLemmas(){
        String text = "Right now the KICK-OFF MEETING - an informational seminar for participants of the Polish-American Innovation Week in California - is taking place in Warsaw.";

        NLPFilter filter = new EnglishNLPFilter();

        for (String lemma : filter.getLemmas(text)){
            System.out.println(lemma);
        }

    }

    @Test
    public void test_getPoSParts(){
        String text = "Right now the KICK-OFF MEETING - an informational seminar for participants of the Polish-American Innovation Week in California - is taking place in Warsaw.";

        NLPFilter filter = new EnglishNLPFilter();

        for (String token : filter.getPoSMatchingTokens(text, PoSPart.NOUN)){
            System.out.println(token);
        }

    }
}
