package pl.nlp.day.nlp;

import com.google.common.collect.Lists;
import morfologik.stemming.WordData;
import morfologik.stemming.polish.PolishStemmer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;

/***
 * Filter NLP dla j. polskiego, implementuje interfejs NLPFilter
 */
public class PolishNLPFilter implements NLPFilter {
    @Override
    public List<String> getTokens(String text) {

        List<String> results = Lists.newArrayList();

        if (StringUtils.isBlank(text)) {
            return null;
        } else {
            for (String token : text.toLowerCase(new Locale("pl")).split("(\\s+|\\p{Punct}+|–)")) {
                if (StringUtils.isNotBlank(token)) {
                    results.add(token);
                }
            }
        }

        return results;
    }

    @Override
    public List<String> getLemmas(String text) {

        List<String> results = Lists.newArrayList();

        if (StringUtils.isBlank(text)) {
            return null;
        } else {
            PolishStemmer stemmer = new PolishStemmer();

            for (String token : getTokens(text)) {
                List<WordData> dictEntities = stemmer.lookup(token);
                if(dictEntities.size() > 0) {
                    CharSequence lemma = dictEntities.get(0).getStem();
                    if (lemma != null) {
                        results.add(lemma.toString().toLowerCase().trim());
                    }
                }
            }
        }

        return results;
    }

    @Override
    public List<String> getPoSMatchingTokens(String text, PoSPart pos) {
        List<String> results = Lists.newArrayList();

        if (StringUtils.isBlank(text)) {
            return null;
        } else {
            PolishStemmer stemmer = new PolishStemmer();

            for (String token : getTokens(text)) {
                List<WordData> dictEntities = stemmer.lookup(token);
                if(dictEntities.size() > 0) {
                    CharSequence posTag = dictEntities.get(0).getTag();
                    if (posTag != null) {
                        String posTagStr = posTag.toString();
                        if (decodePoSPart(pos).stream().anyMatch(posTagStr::contains)) {
                            results.add(token.toLowerCase().trim());
                        }
                    }
                }
            }
        }

        return results;
    }

    private List<String> decodePoSPart(PoSPart poSPart){
        List<String> results = Lists.newArrayList();

        if(poSPart == PoSPart.NOUN){
            results.add("subst");
        }else if(poSPart == PoSPart.ADJECTIVE){
            results.add("adj");
            results.add("adjp");
            results.add("pact");
            results.add("ppas");
        }else if(poSPart == PoSPart.VERB){
            results.add("verb");
        }

        return results;
    }
}
