package pl.nlp.day.babelfy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.stanford.nlp.util.Triple;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.carrot2.util.StringUtils;
import pl.nlp.day.commons.CommonUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.StringReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Klasa ekstraktuje frazy kluczowe w oparciu o Babelfy/Babelnet
 *
 * @author mkozlowski
 */
public class BabelfyExtractor {

    private static final String BABELFY_API_KEY = "dc0b329e-0f19-4008-a6e4-ac8886a4985c";

    private static final String BABELNET_API_KEY = "b625533b-a45f-428a-b953-a9c9991ef23e";

    private static final String BABELFY_URL = "https://babelfy.io/v1/disambiguate?text={text}&key=" + BABELFY_API_KEY;

    private static final String BABELNET_URL = "https://babelnet.io/v2/getSynset?id={synsetId}&key=" + BABELNET_API_KEY;

    private static final String BABELEDGES_URL = "https://babelnet.io/v2/getEdges?id={synsetId}&key=" + BABELNET_API_KEY;

    private int resultSize = 15;


    private List<Triple<String, String, Double>> getBabelNetSynsetsIds(String text) {
        List<Triple<String, String, Double>> results = Lists.newArrayList();

        String convText = StringUtils.urlEncodeWrapException(text, "UTF-8");

        HttpGet get = new HttpGet(BABELFY_URL.replace("{text}", convText));

        try {
            HttpClient httpClient = getHttpClient();

            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();


            String jsonResponse = EntityUtils.toString(entity);

            JsonReader rdr = Json.createReader(new StringReader(jsonResponse));
            JsonArray jsonArray = rdr.readArray();

            for (JsonObject result : jsonArray.getValuesAs(JsonObject.class)) {
                String synsetId = result.getString("babelSynsetID");
                int start = result.getJsonObject("charFragment").getInt("start");
                int end = result.getJsonObject("charFragment").getInt("end");

                String key = text.substring(start, end + 1);

                Double cohScore = result.getJsonNumber("coherenceScore").doubleValue();
                results.add(new Triple<>(synsetId, key, cohScore));

            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


        return results;
    }

    private List<String> getBabelGlosses(List<String> synsetIds) {
        List<String> results = Lists.newArrayList();

        for (String babelId : synsetIds) {

            HttpGet get = new HttpGet(BABELNET_URL.replace("{synsetId}", babelId));

            try {
                HttpClient httpClient = getHttpClient();

                HttpResponse response = httpClient.execute(get);
                HttpEntity entity = response.getEntity();


                String jsonResponse = EntityUtils.toString(entity);

                JsonReader rdr = Json.createReader(new StringReader(jsonResponse));
                JsonObject jsonObject = rdr.readObject();

                for (JsonObject result : jsonObject.getJsonArray("glosses").getValuesAs(JsonObject.class)) {
                    if (result.getString("language").equals("EN")) {
                        results.add(result.getString("gloss"));
                    }
                }
            } catch (Exception ex) {
                //throw new RuntimeException(ex);
            }
        }


        return results;
    }

    private Map<String, List<String>> getBabelCategories(Map<String, String> synsetIdsNames) {
        Map<String, List<String>> results = Maps.newHashMap();

        for (Map.Entry<String, String> entry : synsetIdsNames.entrySet()) {

            HttpGet get = new HttpGet(BABELNET_URL.replace("{synsetId}", entry.getKey()));

            try {
                HttpClient httpClient = getHttpClient();

                HttpResponse response = httpClient.execute(get);
                HttpEntity entity = response.getEntity();


                String jsonResponse = EntityUtils.toString(entity);

                JsonReader rdr = Json.createReader(new StringReader(jsonResponse));
                JsonObject jsonObject = rdr.readObject();

                List<String> categories = Lists.newArrayList();

                for (JsonObject result : jsonObject.getJsonArray("categories").getValuesAs(JsonObject.class)) {
                    if (result.getString("language").equals("EN")) {
                        categories.add(result.getString("category"));
                    }
                }
                results.put(entry.getValue(), categories);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }


        return results;
    }

    private Set<String> getBabelHypernyms(List<String> synsetIds) {
        Set<String> results = Sets.newHashSet();

        for (String babelId : synsetIds) {
            results.addAll(getBabelHypernymsById(babelId));
        }


        return results;
    }

    private Set<String> getBabelHypernymsById(String babelSynsetId) {
        Set<String> results = Sets.newHashSet();


        HttpGet get = new HttpGet(BABELEDGES_URL.replace("{synsetId}", babelSynsetId));

        try {
            HttpClient httpClient = getHttpClient();

            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();


            String jsonResponse = EntityUtils.toString(entity);

            JsonReader rdr = Json.createReader(new StringReader(jsonResponse));
            JsonArray jsonArray = rdr.readArray();

            for (JsonObject result : jsonArray.getValuesAs(JsonObject.class)) {
                if (result.getString("language").equals("EN")) {
                    if (result.getJsonObject("pointer").getString("relationGroup").equals("HYPERNYM")) {
                        results.add(result.getString("target"));
                    }

                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


        return results;
    }

    private static HttpClient getHttpClient() {

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null,
                    new TrustManager[]{new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {

                            return null;
                        }

                        public void checkClientTrusted(
                                X509Certificate[] certs, String authType) {

                        }

                        public void checkServerTrusted(
                                X509Certificate[] certs, String authType) {

                        }
                    }}, new SecureRandom());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


            HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();

            return httpClient;

        } catch (Exception e) {
            e.printStackTrace();
            return HttpClientBuilder.create().build();
        }
    }


    public Set<String> extract(String text) {

        List<Triple<String, String, Double>> results = this.getBabelNetSynsetsIds(text);

        Map<String, Double> mapKeys = Maps.newHashMap();

        for (Triple<String, String, Double> keyphrase : results) {
            mapKeys.put(keyphrase.second(), keyphrase.third());
        }

        return CommonUtils.limitResult(mapKeys, resultSize);

    }

    public void setResultSize(int size) {
        this.resultSize = size;
    }
}


