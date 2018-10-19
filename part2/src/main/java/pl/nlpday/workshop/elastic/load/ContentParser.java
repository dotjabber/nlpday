package pl.nlpday.workshop.elastic.load;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.nlpday.workshop.elastic.common.ElasticDocument;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class ContentParser {
    private static ContentParser parser;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public static ContentParser getInstance() {
        if(parser == null) {
            parser = new ContentParser();
        }

        return parser;
    }

    public ElasticDocument parse(File file) throws IOException, ParseException {
        Document fdoc = Jsoup.parse(FileUtils.readFileToString(file, Charset.forName("UTF-8")));

        Element title = fdoc.selectFirst("title");
        Element date = fdoc.selectFirst("meta[itemprop='datePublished']");
        Element header = fdoc.selectFirst("div[class='article__body'] > div[class='lead']");
        Elements contents = fdoc.select("div[class='article__body'] > div[class='text-block']");

        ElasticDocument edoc = null;
        if(title != null && date != null && header != null && contents.size() > 0) {
            edoc = new ElasticDocument();

            edoc.put(ElasticDocument.FILE, file.getName());
            edoc.put(ElasticDocument.TITLE, title.text());
            edoc.put(ElasticDocument.DATE, dateFormat.parse(date.attr("content")));
            edoc.put(ElasticDocument.HEADER, header.text());

            String content = contents.stream().map(Element::text).collect(Collectors.joining(" "));
            edoc.put(ElasticDocument.CONTENT_STANDARD, content);
            edoc.put(ElasticDocument.CONTENT_ANALYZED, content);
        }

        return edoc;
    }
}
