package pl.nlpday.workshop.elastic.load;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.logging.Logger;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import pl.nlpday.workshop.elastic.common.ElasticDocument;

public class ElasticLoader implements Closeable {
    private static final Logger LOG = Logger.getGlobal();

    private TransportClient client;
    private BulkProcessor processor;

    private String indexName;
    private String typeName;
    private int loadCount;

    public ElasticLoader(String address, int port, String clusterName, String indexName, String typeName,
                         String mappingsContent, String settingsContent) throws UnknownHostException {

        this.indexName = indexName;
        this.typeName = typeName;

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", false)
                .build();

        client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(address), port));

        processor = BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {

                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                    }
                }).build();

        if (!hasIndex(indexName)) {
            createIndex(indexName, settingsContent);
        }

        if (!hasType(indexName, typeName)) {
            createType(indexName, typeName, mappingsContent);
        }
    }

    private boolean hasIndex(String indexName) {
        return client.admin().indices()
                .prepareExists(indexName)
                .get()
                .isExists();
    }

    private void createIndex(String indexName, String indexMapping) {
        client.admin().indices()
                .prepareCreate(indexName)
                .setSettings(indexMapping, XContentType.JSON)
                .get();
    }

    private boolean hasType(String indexName, String typeName) {
        return client.admin().indices()
                .prepareTypesExists(indexName)
                .setTypes(typeName)
                .get()
                .isExists();
    }

    private void createType(String indexName, String typeName, String typeMapping) {
        client.admin().indices()
                .preparePutMapping(indexName)
                .setType(typeName)
                .setSource(typeMapping, XContentType.JSON)
                .get();
    }

    private XContentBuilder map(ElasticDocument model) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

        for (String fieldName : model.keySet()) {
            builder.field(fieldName, model.get(fieldName));
        }

        return builder.endObject();
    }

    public void store(ElasticDocument document) throws IOException {
        XContentBuilder builder = map(document);
        processor.add(new IndexRequest(indexName, typeName).source(builder));

        if (++loadCount % 1000 == 0) {
            LOG.info(String.format("Loaded documents: %d", loadCount));
        }
    }

    @Override
    public void close() {
        processor.close();
        client.close();
    }
}
