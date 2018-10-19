package pl.nlp.day.dpl4j;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import pl.nlp.day.commons.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Word2VecKeysExtractor {

    private Word2Vec word2Vec;
    private CollectionSentenceIterator iterator;
    private TokenizerFactory tokenizerFactory;


    public Word2VecKeysExtractor(List<String> texts, String pathToModel) throws IOException{
        this.iterator = new CollectionSentenceIterator(texts);

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        this.buildWord2Vec(pathToModel);
    }

    public Word2VecKeysExtractor(String pathToModel) throws IOException {
        this.iterator = null;

        this.tokenizerFactory = new DefaultTokenizerFactory();
        this.tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        this.word2Vec = load(FileUtils.readFileToByteArray(new File(pathToModel)));
        this.word2Vec.setLookupTable(this.word2Vec.getLookupTable());
    }


    public Collection<String> extractKeywords(String text){
        MeansBuilder meansBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>) word2Vec.getLookupTable(),
                tokenizerFactory);

        INDArray documentAsCentroid = meansBuilder.documentAsVector(text);

        return  word2Vec.wordsNearest(documentAsCentroid,10);
    }

    private void buildWord2Vec(String pathToModel) throws IOException {

        word2Vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iterator)
                .tokenizerFactory(tokenizerFactory)
                .build();

        word2Vec.fit();

        CommonUtils.save(pathToModel, CommonUtils.serialize(word2Vec));
    }


    private Word2Vec load(byte[] fileBytes) {

        if (fileBytes != null) {
            return (Word2Vec) SerializationUtils.deserialize(fileBytes);
        } else {
            return null;
        }
    }
}
