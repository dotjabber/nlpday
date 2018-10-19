package pl.nlp.day.nlp;

import java.util.List;

/***
 * Interfejs filtra NLP obejmuja kontraktem: pobieranie tokenow, lematow, i tylko pasujacych do PoS patternu tokenow
 */
public interface NLPFilter {
    List<String> getTokens(String text);
    List<String> getLemmas(String text);
    List<String> getPoSMatchingTokens(String text, PoSPart pos);
}
