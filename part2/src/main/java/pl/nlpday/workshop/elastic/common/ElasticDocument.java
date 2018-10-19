package pl.nlpday.workshop.elastic.common;

import java.text.MessageFormat;
import java.util.HashMap;

public class ElasticDocument extends HashMap<String, Object> {
    private static final String FORMAT = "[({0}) {1} ({1}) ->  {2}]";

    public static final String FILE = "file";
    public static final String TITLE = "title";
    public static final String HEADER = "header";
    public static final String CONTENT_STANDARD = "content_standard";
    public static final String CONTENT_ANALYZED = "content_analyzed";
    public static final String DATE = "date";

    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return MessageFormat.format(FORMAT, getScore(), get(TITLE), get(DATE), get(CONTENT_STANDARD));
    }
}