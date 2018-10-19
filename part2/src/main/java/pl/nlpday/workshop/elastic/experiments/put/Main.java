package pl.nlpday.workshop.elastic.experiments.put;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import pl.nlpday.workshop.elastic.common.Config;

import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        String indexName = "test_index";
        String accessPoint = Config.getString("elastic.access.point") + indexName;

        URL settings = Config.class.getClassLoader().getResource("examples/03-put-default.json");
        URL query = Config.class.getClassLoader().getResource("examples/03-get-default.json");

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            // create index
            HttpUriRequest request = RequestBuilder
                    .put(accessPoint)
                    .setHeader("Content-Type", "application/json;charset=UTF-8")
                    .setEntity(new StringEntity(Config.getJSON(settings)))
                    .build();

            HttpResponse response = client.execute(request);
            System.out.println(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        }

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            // query index
            HttpUriRequest request = RequestBuilder
                    .post(accessPoint + "/_analyze?pretty")
                    .setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(Config.getJSON(query)))
                    .build();

            HttpResponse response = client.execute(request);
            System.out.println(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        }

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            // delete index
            HttpUriRequest request = RequestBuilder
                    .delete(accessPoint)
                    .setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(Config.getJSON(settings)))
                    .build();

            HttpResponse response = client.execute(request);
            System.out.println(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        }
    }
}
