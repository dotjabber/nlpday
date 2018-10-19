package pl.nlpday.workshop.elastic.experiments.get;

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
        String accessPoint = Config.getString("elastic.access.point") + "_analyze?pretty";
        URL settings = Config.class.getClassLoader().getResource("examples/01-post-runtime.json");

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpUriRequest request = RequestBuilder
                    .get(accessPoint)
                    .setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(Config.getJSON(settings)))
                    .build();

            HttpResponse response = client.execute(request);
            System.out.println(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        }
    }
}
