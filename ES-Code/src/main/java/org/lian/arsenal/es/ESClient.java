package org.lian.arsenal.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * Hello world!
 *
 * @author sedra
 */
public class ESClient {
    public static void main(String[] args) throws IOException {
        // 创建ES客户端
        HttpHost host = new HttpHost("localhost", 9200, "http");
        RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(host));

        // 索引操作
        createIndices(esClient);
        queryIndices(esClient);
        deleteIndices(esClient);

        // 文档操作
        insertIndex(esClient);
        updateIndex(esClient);
        getIndex(esClient);
        delIndex(esClient);
        insertBatch(esClient);
        delBatch(esClient);

        // 查询

        search(esClient);

        // 关闭ES客户端
        esClient.close();
    }

    private static void search(RestHighLevelClient esClient) throws IOException {
        // 1. 查询索引中全部的数据
        SearchRequest request = new SearchRequest();
        request.indices("test");
        SearchSourceBuilder source = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        request.source(source);
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        System.out.println(hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println(response.getTook());
    }

    private static void delBatch(RestHighLevelClient esClient) throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest().index("test").id("1002"));
        request.add(new DeleteRequest().index("test").id("1003"));
        request.add(new DeleteRequest().index("test").id("1004"));
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
    }

    private static void insertBatch(RestHighLevelClient esClient) throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index("test").id("1002")
                .source(XContentType.JSON, "name", "zs", "age", 18, "sex", "women"));
        request.add(new IndexRequest().index("test").id("1003")
                .source(XContentType.JSON, "name", "li", "age", 20, "sex", "man"));
        request.add(new IndexRequest().index("test").id("1004")
                .source(XContentType.JSON, "name", "wu", "age", 23, "sex", "man"));
        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.getTook());
    }

    private static void delIndex(RestHighLevelClient esClient) throws IOException {
        DeleteRequest request = new DeleteRequest();
        request.index("test").id("1001");
        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    private static void getIndex(RestHighLevelClient esClient) throws IOException {
        GetRequest request = new GetRequest();
        request.index("test").id("1001");
        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);
        //System.out.println(response.getSourceAsMap());
        System.out.println(response.getSourceAsString());
    }

    private static void updateIndex(RestHighLevelClient esClient) throws IOException {
        // 修改数据
        UpdateRequest request = new UpdateRequest();
        request.index("test").id("1001").doc(XContentType.JSON, "age", 28);
        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }

    public static void createIndices(RestHighLevelClient esClient) throws IOException {
        // 创建索引
        CreateIndexRequest request = new CreateIndexRequest("test");
        CreateIndexResponse response = esClient.indices().create(request, RequestOptions.DEFAULT);

        // 响应状态
        boolean acknowledged = response.isAcknowledged();
        System.out.println("创建索引：" + acknowledged);
    }

    public static void queryIndices(RestHighLevelClient esClient) throws IOException {
        // 查询索引
        GetIndexRequest getIndexRequest = new GetIndexRequest("test");
        GetIndexResponse getIndexResponse = esClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(getIndexResponse.getAliases());
        System.out.println(getIndexResponse.getMappings());
        System.out.println(getIndexResponse.getSettings());
    }

    public static void deleteIndices(RestHighLevelClient esClient) throws IOException {
        // 删除索引
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test");
        AcknowledgedResponse acknowledgedResponse = esClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }

    public static void insertIndex(RestHighLevelClient esClient) throws IOException {
        // 向ES插入数据,必须将数据转换成JSON格式
        User user = new User("lianwl", "man", 28);
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);

        // 插入数据
        IndexRequest request = new IndexRequest();
        request.index("test").id("1001").source(userJson, XContentType.JSON);
        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }
}
