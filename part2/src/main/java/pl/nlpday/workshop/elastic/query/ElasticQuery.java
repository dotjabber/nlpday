package pl.nlpday.workshop.elastic.query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import pl.nlpday.workshop.elastic.common.Config;
import pl.nlpday.workshop.elastic.common.ElasticDocument;

import java.io.Closeable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticQuery implements Closeable {
    private TransportClient client;

    private String indexName;
    private String typeName;

    public ElasticQuery(String address, int port, String clusterName, String indexName, String typeName) throws UnknownHostException {
        this.indexName = indexName;
        this.typeName = typeName;

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", false)
                .build();

        client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(address), port));
    }

    private ElasticDocument map(SearchHit model) {
        ElasticDocument object = new ElasticDocument();

        for (String fieldName : model.getSourceAsMap().keySet()) {
            object.put(fieldName, model.getSourceAsMap().get(fieldName));
        }

        object.setScore(model.getScore());
        return object;
    }

    public List<ElasticDocument> query(String content) {
        QueryBuilder builder = QueryBuilders.matchQuery("content_standard", content);
//        QueryBuilder builder = QueryBuilders.moreLikeThisQuery(
//                new String[] { "content_standard" }, new String[] { content } , null)
//
//                .minTermFreq(Config.getInt("min.term.freq"))
//                .minDocFreq(Config.getInt("min.doc.freq"))
//                .maxQueryTerms(Config.getInt("max.query.terms"))
//                .minimumShouldMatch(Config.getString("min.should.match"));

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(typeName)
                .setQuery(builder)
                .setSize(Config.getInt("result.count"))
                .get();

        return Arrays.stream(response.getHits().getHits()).map(this::map).collect(Collectors.toList());
    }

    @Override
    public void close() {
        client.close();
    }
}
