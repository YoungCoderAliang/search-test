package elasticsearch;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class CreateIndex {
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = ClientFactory.getNewClient();
		
		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("properties")
				// .startObject("m_id").field("type","keyword").endObject()
				.startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.startObject("sport_type").field("type", "text").field("analyzer", "ik_max_word").endObject()
				.endObject().endObject();
		CreateIndexRequest request = new CreateIndexRequest("artical");
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
		request.mapping("sports", mapping);

		request.timeout(TimeValue.timeValueMinutes(2));
		request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
		request.waitForActiveShards(2);
		CreateIndexResponse res = client.indices().create(request);

		System.out.println(res.isAcknowledged());
		System.out.println(res.index());

		client.close();
	}
}
