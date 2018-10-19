package pl.nlp.day;

import com.google.common.collect.Sets;
import org.junit.Test;
import pl.nlp.day.evaluator.JaccardEvaluator;

import java.util.Set;

/**
 * Klasa przykladow użycia ewaluatora Jaccarda
 */
public class ExperimentsJaccardEvaluator {

    @Test
    public void test_JaccardEvaluator(){
        String s = "Mama lubi koty";
        String t = "Tata i mama lubi koty i psy";

        Set<String> srcTokens = Sets.newHashSet(s.toLowerCase().split("\\s+|\\p{Punct}"));
        Set<String> trgTokens = Sets.newHashSet(t.toLowerCase().split("\\s+|\\p{Punct}"));

        System.out.println("JaccardSim: " + (new JaccardEvaluator()).computeRatio(srcTokens,trgTokens));
    }

    @Test
    public void test_JaccardEvaluator2(){
        String s = "Mama lubi koty";
        String t = "Tata i mama lubi koty i psy";

        System.out.println("JaccardSim: " + (new JaccardEvaluator()).computeRatio(s,t));
    }
}
