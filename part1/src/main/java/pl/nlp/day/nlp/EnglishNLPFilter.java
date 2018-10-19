package pl.nlp.day.nlp;

import com.google.common.collect.Lists;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/***
 * Filter NLP dla j. angielskiego, implementuje interfejs NLPFilter
 */
public class EnglishNLPFilter implements NLPFilter {
    @Override
    public List<String> getTokens(String text) {

        List<String> results = Lists.newArrayList();

        Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            for (String word : sent.words()) {
                if (StringUtils.isNotBlank(word)) {
                    results.add(word.toLowerCase().trim());
                }
            }
        }

        return results;
    }

    @Override
    public List<String> getLemmas(String text) {
        List<String> results = Lists.newArrayList();

        Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            for (String lemma : sent.lemmas()) {
                if (StringUtils.isNotBlank(lemma) && StringUtils.isAlphanumeric(lemma)) {
                    results.add(lemma.toLowerCase().trim());
                }
            }
        }

        return results;
    }

    @Override
    public List<String> getPoSMatchingTokens(String text, PoSPart pos) {
        List<String> results = Lists.newArrayList();

        Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            for (int i = 0; i < sent.words().size(); ++i) {
                if (StringUtils.isNotBlank(sent.word(i)) && StringUtils.isAlphanumeric(sent.word(i))) {
                    String posTag = sent.posTag(i);
                    if (StringUtils.isNotBlank(posTag) && decodePoSPart(pos).stream().anyMatch(posTag::contains)) {
                        results.add(sent.word(i).toLowerCase().trim());
                    }
                }
            }
        }

        return results;
    }

    private List<String> decodePoSPart(PoSPart poSPart) {
        List<String> results = Lists.newArrayList();

        if (poSPart == PoSPart.NOUN) {
            results.add("NN");
            results.add("NNP");
            results.add("NNS");
        } else if (poSPart == PoSPart.ADJECTIVE) {
            results.add("JJ");
            results.add("JJR");
            results.add("JJS");
        } else if (poSPart == PoSPart.VERB) {
            results.add("VB");
            results.add("VBG");
            results.add("VBN");
            results.add("VBD");
        }

        return results;
    }
}
