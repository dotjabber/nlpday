package pl.nlp.day.commons;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasa narzędziowa zawierająca metody ogólnego przeznaczenia.
 *
 * @author mkozlowski
 */
public final class CommonUtils {

    /**
     * Ukryty konstruktor.
     */
    private CommonUtils() {
    }


    public static final byte[] serialize(Serializable model) {
        ByteArrayOutputStream out;
        try {
            out = new ByteArrayOutputStream();
            SerializationUtils.serialize(model, out);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    public static void save(String path, byte[] serializedBytes) {
        File file = new File(path);

        try {
            FileUtils.writeByteArrayToFile(file, serializedBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ogranicza wyniki do top N (limit), oraz odcina frazy poniżej minLen oraz powyżej maxLen
     * @param keyphraes frazy kluczowe z wagami
     * @param limit limit
     * @return
     */
    public static Set<String> limitResult(Map<String, Double> keyphrases,
                                                   int limit) {

        Set<String> scoredKeyphrases = Sets.newHashSet();

        //sort the map in decreasing order of value
        Map<String, Double> sortedMap = keyphrases
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));


        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            scoredKeyphrases.add(entry.getKey());
            if (--limit <= 0) {
                break;
            }
        }

        return scoredKeyphrases;
    }

}
