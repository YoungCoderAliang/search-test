package elasticsearch;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class CreateIndex {
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = ClientFactory.getNewClient();
		
		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
				// .startObject("m_id").field("type","keyword").endObject()
				.startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.startObject("sport_type").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.endObject().endObject();
		// ��������
		CreateIndexRequest request = new CreateIndexRequest("artical");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
		// ��������ʱ�����ĵ�����ӳ��
		request.mapping("sports", mapping);

		// ��ʱ,�ȴ����нڵ㱻ȷ��(ʹ��TimeValue��ʽ)
		request.timeout(TimeValue.timeValueMinutes(2));
		// ��ʱ,�ȴ����нڵ㱻ȷ��(ʹ���ַ�����ʽ)s
		// request.timeout("2m");

		// ����master�ڵ�ĳ�ʱʱ��(ʹ��TimeValue��ʽ)
		request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
		// ����master�ڵ�ĳ�ʱʱ��(ʹ���ַ�����ʽ)
		// request.masterNodeTimeout("1m");

		// �ڴ�������API������Ӧ֮ǰ�ȴ��Ļ��Ƭ��������������int��ʽ��ʾ��
		request.waitForActiveShards(2);
		// �ڴ�������API������Ӧ֮ǰ�ȴ��Ļ��Ƭ��������������ActiveShardCount��ʽ��ʾ��
		// request.waitForActiveShards(ActiveShardCount.DEFAULT);

		// ͬ��ִ��
		CreateIndexResponse res = client.indices().create(request);

		System.out.println(res.isAcknowledged());
		System.out.println(res.index());

		client.close();
	}

	public static void main1(String[] args) throws IOException {
		Settings settings = Settings.builder().put("cluster.name", "aliyun-es-test").build();
		TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(
				InetAddress.getByName("es-cn-4590wsh2x000zu4kh.public.elasticsearch.aliyuncs.com"), 9200));
		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
				// .startObject("m_id").field("type","keyword").endObject()
				.startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.startObject("sport_type").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.endObject().endObject();
		// pois�������� cxyword���������������Լ����壩
		PutMappingRequest putmap = Requests.putMappingRequest("artical").type("sports").source(mapping);
		// ��������
		client.admin().indices().prepareCreate("artical").execute().actionGet();
		// Ϊ�������ӳ��
		PutMappingResponse response = client.admin().indices().putMapping(putmap).actionGet();
		System.out.println(response.isAcknowledged());
		client.close();
	}
}
