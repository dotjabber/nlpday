package pl.nlpday.workshop.elastic.load;

import pl.nlpday.workshop.elastic.common.Config;
import pl.nlpday.workshop.elastic.common.ElasticDocument;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

/**
 * http://127.0.0.1:9200/_cat/indices?v
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        args = new String[] {
                "D:\\nlpday\\dane-polskie-web-news\\se"
        };

        ElasticLoader loader = new ElasticLoader(
                Config.getString("elastic.address"),
                Config.getInt("elastic.cluster.port"),
                Config.getString("elastic.cluster.name"),
                Config.getString("elastic.index.name"),
                Config.getString("elastic.index.type.name"),
                Config.getMappings(),
                Config.getSettings()
        );

        for (File file : Objects.requireNonNull(new File(args[0]).listFiles())) {
            ElasticDocument document = ContentParser.getInstance().parse(file);

            if(document != null) {
                loader.store(document);
            }
        }

        loader.close();
    }
}
