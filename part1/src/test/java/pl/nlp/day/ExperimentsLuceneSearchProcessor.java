package pl.nlp.day;

import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;
import pl.nlp.day.lucene.LuceneSearchProcessor;

import java.io.File;
import java.net.URL;
import java.util.List;

public class ExperimentsLuceneSearchProcessor {

    private static final String LUCENE_DIR_PATH = "C:\\Users\\Marek Kozłowski\\nlpday\\lucene-index";

    @Test
    public void test_LuceneIndexBuilding() throws Exception {

        LuceneSearchProcessor luceneSearchProcessor = new LuceneSearchProcessor(LUCENE_DIR_PATH,
                new StandardAnalyzer(), 10);

        URL url = Resources.getResource("sample10k.de-en.en");
        List<String> contents = FileUtils.readLines(new File(url.toURI()), "UTF-8");

        luceneSearchProcessor.buildIndex(contents);

    }

    @Test
    public void test_LuceneIndexSimpleSearching() throws Exception {

        LuceneSearchProcessor luceneSearchProcessor = new LuceneSearchProcessor(LUCENE_DIR_PATH,
                 new StandardAnalyzer(), 10);

        List<String> results = luceneSearchProcessor.retrieveResults("laptops are the most advanced in the world");

        for (String srcSent : results) {
            System.out.println(srcSent);
        }
    }

    @Test
    public void test_LuceneIndexExactSearching() throws Exception {

        LuceneSearchProcessor luceneSearchProcessor = new LuceneSearchProcessor(LUCENE_DIR_PATH,
                new StandardAnalyzer(), 10);

        List<String> results = luceneSearchProcessor.retrieveResults("\"legislation in the European Union\"");

        for (String srcSent : results) {
            System.out.println(srcSent);
        }
    }


    @Test
    public void test_LuceneIndexMorphSearching() throws Exception {

        LuceneSearchProcessor luceneSearchProcessor = new LuceneSearchProcessor(LUCENE_DIR_PATH,
                new EnglishAnalyzer(),  10);

        List<String> results = luceneSearchProcessor.retrieveResults("laptops are the most advanced in the world");

        for (String srcSent : results) {
            System.out.println(srcSent);
        }
    }

    @Test
    public void test_LuceneIndexMoreLikeThisSearching() throws Exception {

        LuceneSearchProcessor luceneSearchProcessor = new LuceneSearchProcessor(LUCENE_DIR_PATH,
                new EnglishAnalyzer(), 10);


        List<String> results = luceneSearchProcessor.retrieveMoreLikeThisResults("laptops are the most advanced in the world");

        for (String srcSent : results) {
            System.out.println(srcSent);
        }
    }


}
