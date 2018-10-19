package pl.nlp.day;

import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import pl.nlp.day.dpl4j.Word2VecKeysExtractor;

import java.io.File;
import java.net.URL;
import java.util.List;

public class ExperimentsWord2VecExtractor {

    @Test
    public void testBuildWord2VecAndExtraction() throws Exception{

        URL url = Resources.getResource("sample10k.de-en.en");
        List<String> contents = FileUtils.readLines(new File(url.toURI()), "UTF-8");
        Word2VecKeysExtractor extractor = new Word2VecKeysExtractor(contents,"C:\\Users\\Marek Kozłowski\\nlpday\\w2v-model");

        String inpText = "tax frauds destroy economy";

        for(String key: extractor.extractKeywords(inpText)){
            System.out.println(key);
        }

    }

}
