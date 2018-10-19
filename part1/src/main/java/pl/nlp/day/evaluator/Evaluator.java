package pl.nlp.day.evaluator;

/**
 * Interfejs do Ewaluatora dwoch tekstow
 */
public interface Evaluator {
    /**
     * Liczy wspolczynnik podobieństwa od 0 do 1
     * @param query tekstowe zapytanie
     * @param text tekst, względem którego weryfikujemy zapytanie
     * @return
     */
    Double computeRatio(String query, String text);
}
