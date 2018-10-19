package pl.nlp.day;

import org.junit.Test;
import pl.nlp.day.nlp.NLPFilter;
import pl.nlp.day.nlp.PoSPart;
import pl.nlp.day.nlp.PolishNLPFilter;

/**
 * Klasa przykładów użycia PolishNLPFilter
 */
public class ExperimentsPolishNLPFilter {

    @Test
    public void test_getTokens(){
        String text = "A w tej chwili teraz trwa SPOTKANIE PRZYGOTOWAWCZE – seminarium informacyjnego dla uczestników Polsko-Amerykańskiego Tygodnia Innowacji w Kalifornii, które odbywa się w Warszawie.";

        NLPFilter filter = new PolishNLPFilter();

        for (String token : filter.getTokens(text)){
            System.out.println(token);
        }

    }

    @Test
    public void test_getLemmas(){
        String text = "A w tej chwili teraz trwa SPOTKANIE PRZYGOTOWAWCZE – seminarium informacyjnego dla uczestników Polsko-Amerykańskiego Tygodnia Innowacji w Kalifornii, które odbywa się w Warszawie.";

        NLPFilter filter = new PolishNLPFilter();

        for (String lemma : filter.getLemmas(text)){
            System.out.println(lemma);
        }

    }

    @Test
    public void test_getPoSParts(){
        String text = "A w tej chwili teraz trwa SPOTKANIE PRZYGOTOWAWCZE – seminarium informacyjnego dla uczestników Polsko-Amerykańskiego Tygodnia Innowacji w Kalifornii, które odbywa się w Warszawie.";

        NLPFilter filter = new PolishNLPFilter();

        for (String token : filter.getPoSMatchingTokens(text, PoSPart.NOUN)){
            System.out.println(token);
        }

    }
}
