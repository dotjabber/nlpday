package pl.nlpday.workshop.elastic.encode;

import org.apache.commons.lang.StringEscapeUtils;

public class StringEncode {
    public static void main(String[] args) {
        args = new String[] {
                "<b>Wielki Wybuch</b> - model ewolucji Wszechświata uznawany za najbardziej prawdopodobny. Według tego " +
                        "modelu ok. 13,799 +- 0,021 mld lat temu miał miejsce Wielki Wybuch - z nieskończenie gęstej i " +
                        "gorącej osobliwości początkowej wyłonił się Wszechświat (przestrzeń, czas, materia, energia i " +
                        "oddziaływania)."
        };

        System.out.println(StringEscapeUtils.escapeJava(args[0]));
    }
}
