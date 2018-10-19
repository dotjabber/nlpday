package pl.nlp.day.lucene;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Uproszczona klasa obslugująca indeks Lucene na potrzeby indeksowania i wyszukiwania w kolekcji tekstów.
 * Zawiera w sobie funkcjonalności budowniczego indeksu oraz wyszukiwarki na indeksie. Ma ograniczone wsparcie
 * dla kwestii jezykowych (nie ma uzyca NLP FIlters), tylko opiera sie na tym co jest implementacja Analyzera.
 */
public class LuceneSearchProcessor {

    private static final String fieldName = "text_content";

    private String directoryLuceneStorage;
    private Analyzer analyzerWrapper;
    private int topN;

    /***
     * Konstruktor dla procesora wyszukiwania z pomocą Lucene
     * @param directoryLuceneStorage ścieżka do katalogu gdzie ma leżeć lub lezy indeks (jeśli nie ma indeksu zbudowanego to podajemy
     *                               ścieżę gdzie ten indeks ma zostać zbudowany i zapisany, gdy mamy indeks zbudowany to wskazujemy jego lokalizacje)
     * @param plAnalyzer analizator morfosyntaktyczny dla tekstów indeksowanych
     * @param topN top wyników np. ma zwracać top 10
     */
    public LuceneSearchProcessor(String directoryLuceneStorage, Analyzer plAnalyzer, int topN) {
        this.directoryLuceneStorage = directoryLuceneStorage;

        Map<String,Analyzer> analyzerPerField = Maps.newHashMap();
        analyzerPerField.put(fieldName, plAnalyzer);

        this.analyzerWrapper =
                new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);

        this.topN = topN;
    }

    /***
     *  Buduje indeks lucence na danych tekstowych
     * @param inputData dane wej - dokładniej kolekcja tekstów
     * @throws Exception
     */
    public void buildIndex(List<String> inputData) throws Exception {
        Directory directory = FSDirectory.open(Paths.get(this.directoryLuceneStorage));
        IndexWriterConfig config = new IndexWriterConfig(analyzerWrapper);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        // Create a writer
        IndexWriter writer = new IndexWriter(directory, config);

        for (String srcSent : inputData) {
            Document document = new Document();
            document.add(new TextField(fieldName, srcSent, Field.Store.YES));
            writer.addDocument(document);
        }

        writer.close();
    }

    /***
     * Metod wyszukiwania w indeksie na podstawie query, który jest kolekcją słów kluczowych, frazy
     * @param queryStr zapytanie w postaci frazy, albo kolekcji słow kluczowych
     * @return
     * @throws Exception
     */
    public List<String> retrieveResults(String queryStr) throws Exception {

        List<String> results = Lists.newArrayList();

        Directory directory = FSDirectory.open(Paths.get(this.directoryLuceneStorage));

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new MultiFieldQueryParser(new String[]{fieldName},analyzerWrapper);
        Query query = parser.parse(queryStr);
        TopDocs lucResults = searcher.search(query, topN);

        for (int i = 0; i < lucResults.scoreDocs.length; ++i) {
            int docId = lucResults.scoreDocs[i].doc;
            Document d = searcher.doc(docId);
            results.add(d.get(fieldName));
        }

        return results;

    }

    /***
     * Metoda wyszukiwania w indeksie Lucene za pomocą operatora MoreLikeThis (dedykowanego dłuższym zapytaniom np. akapitom)
     * @param queryStr zapytanie w postaci dłuższego tekstu np zdania, kilku zdań
     * @return liste tekstów
     * @throws Exception
     */
    public List<String> retrieveMoreLikeThisResults(String queryStr) throws Exception {

        List<String> results = Lists.newArrayList();

        Directory directory = FSDirectory.open(Paths.get(this.directoryLuceneStorage));

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        MoreLikeThis mlt = new MoreLikeThis(reader);
        mlt.setAnalyzer(this.analyzerWrapper);

        mlt.setMinTermFreq(1);
        mlt.setMinDocFreq(1);
        mlt.setMinWordLen(3);

        Reader stringReader = new StringReader(queryStr);

        Query query = mlt.like(fieldName,stringReader);
        //Search the index using the query and get the top 5 results
        TopDocs lucResults = searcher.search(query, this.topN);

        for (int i = 0; i < lucResults.scoreDocs.length; ++i) {
            int docId = lucResults.scoreDocs[i].doc;
            Document d = searcher.doc(docId);
            results.add(d.get(fieldName));
        }

        return results;
    }

}
