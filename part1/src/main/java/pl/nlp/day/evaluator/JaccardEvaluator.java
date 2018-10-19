package pl.nlp.day.evaluator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Ewaluator dwoch tekstow, dwoch kolekcji tokenow z uzyciem wzoru Jaccarda
 */
public class JaccardEvaluator implements Evaluator{

    /**
     * Liczy współczynnik podobieństwa między zbiorami tokenów
     * @param a zbiór tokenów
     * @param b zbiór tokenów
     * @return wartość od 0 do 1
     */
    public Double computeRatio(Set<String> a, Set<String> b) {

        if (a.isEmpty() && b.isEmpty()) {
            return 1.0d;
        }

        if (a.isEmpty() || b.isEmpty()) {
            return 0.0d;
        }

        final int intersectionSize = Sets.intersection(a, b).size();

        return (double)intersectionSize / (double) (a.size() + b.size() - intersectionSize);
    }

    /**
     * Liczy współczynnik podobieństwa między tekstami
     * @param s jeden tekst
     * @param t drugi tekst
     * @return
     */
    @Override
    public Double computeRatio(String query, String text) {
        Set<String> srcTokens = Sets.newHashSet(Lists.newArrayList(query.toLowerCase().split("\\s+|\\p{Punct}")));
        Set<String> trgTokens = Sets.newHashSet(Lists.newArrayList(text.toLowerCase().split("\\s+|\\p{Punct}")));

        return computeRatio(srcTokens,trgTokens);
    }

}
