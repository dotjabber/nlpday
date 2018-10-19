package pl.nlp.day.evaluator;

import org.apache.commons.lang3.StringUtils;

/**
 * Ewaluator dwoch tekstow, dwoch kolekcji tokenow z uzyciem Longest Common Substring
 */
public class LCSEvaluator implements Evaluator{

    public LCSEvaluator() { }

    // return the longest common prefix of suffix s[p..] and suffix t[q..]
    private String lcp(String s, int p, String t, int q) {
        int n = Math.min(s.length() - p, t.length() - q);
        for (int i = 0; i < n; i++) {
            if (s.charAt(p + i) != t.charAt(q + i))
                return s.substring(p, p + i);
        }
        return s.substring(p, p + n);
    }

    // compare suffix s[p..] and suffix t[q..]
    private int compare(String s, int p, String t, int q) {
        int n = Math.min(s.length() - p, t.length() - q);
        for (int i = 0; i < n; i++) {
            if (s.charAt(p + i) != t.charAt(q + i))
                return s.charAt(p+i) - t.charAt(q+i);
        }
        if      (s.length() - p < t.length() - q) return -1;
        else if (s.length() - p > t.length() - q) return +1;
        else                                      return  0;
    }

    /**
     * Returns the longest common string of the two specified strings.
     *
     * @param  s one string
     * @param  t the other string
     * @return the longest common string that appears as a substring
     *         in both {@code s} and {@code t}; the empty string
     *         if no such string
     */
    public String lcs(String s, String t) {

        if(StringUtils.isBlank(s) || StringUtils.isBlank(t)){
            return null;
        }

        String cleanedSrc = s.trim().replaceAll("\\s+", " ").toLowerCase();
        String cleanedTrg = t.trim().replaceAll("\\s+", " ").toLowerCase();

        SuffixArray suffix1 = new SuffixArray(cleanedSrc);
        SuffixArray suffix2 = new SuffixArray(cleanedTrg);

        // find longest common substring by "merging" sorted suffixes
        String lcs = "";
        int i = 0, j = 0;
        while (i < cleanedSrc.length() && j < cleanedTrg.length()) {
            int p = suffix1.index(i);
            int q = suffix2.index(j);
            String x = lcp(cleanedSrc, p, cleanedTrg, q);
            if (x.length() > lcs.length())
                lcs = x;
            if (compare(cleanedSrc, p, cleanedTrg, q) < 0)
                i++;
            else
                j++;
        }

        return lcs;
    }

    /**
     * Liczy współczynnik podobieństwa między tekstami
     * @param query zapytanie tekstowe
     * @param text tekstowy dokument
     * @return wartość od 0 do 1
     */
    @Override
    public Double computeRatio(String query, String text){
        String lcsPhrase = lcs(query,text);

        if(lcsPhrase != null){
            return (double)lcsPhrase.length()/(double)query.length();
        }else{
            return null;
        }
    }
}
