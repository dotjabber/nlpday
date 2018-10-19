package pl.nlpday.workshop.elastic.query;

import pl.nlpday.workshop.elastic.common.Config;
import pl.nlpday.workshop.elastic.load.ContentParser;
import pl.nlpday.workshop.elastic.common.ElasticDocument;
import pl.nlpday.workshop.elastic.load.ElasticLoader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

/**
 * http://127.0.0.1:9200/_cat/indices?v
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        args = new String[] {
                "nowa gałąź nowoczesnych technologii"
        };

        ElasticQuery es = new ElasticQuery(
                Config.getString("elastic.address"),
                Config.getInt("elastic.cluster.port"),
                Config.getString("elastic.cluster.name"),
                Config.getString("elastic.index.name"),
                Config.getString("elastic.index.type.name")
        );

        List<ElasticDocument> results = es.query(args[0]);
        results.forEach(System.out::println);

        es.close();
    }
}
