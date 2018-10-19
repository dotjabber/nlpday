package pl.nlp.day;

import org.junit.Test;
import pl.nlp.day.evaluator.LCSEvaluator;

/**
 * Klasa przykładów użycia ewaluatora LCS - Longest Common Substring
 */
public class ExperimentsLCSEvaluator {

    @Test
    public void test_LCSEvaluator(){
        String s = "Od zawsze Mama lubi koty i papugi";
        String t = "Tata i mama lubi koty i psy";
        System.out.println("LCS: " + new LCSEvaluator().lcs(s, t));
        System.out.println("Ratio: " + new LCSEvaluator().computeRatio(s, t));

    }
}
