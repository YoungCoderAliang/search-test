package elasticsearch.search;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.ClientFactory;

public abstract class AbstractSearchTest {
	public void test() {
		RestHighLevelClient client = ClientFactory.getNewClient();
		try {
			dropIndex(client);
		} catch(Exception e) {
		}
		try {
			createIndex(client);
		} catch(Exception e) {
			e.printStackTrace();
			closeClient(client);
			return;
		}
		try {
			addData(client);
			doSearch(client);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			dropIndex(client);
		} catch(Exception e) {
			e.printStackTrace();
		}
		closeClient(client);
	}

	private void doSearch(RestHighLevelClient client)
			throws InterruptedException, Exception, IOException {
		// 添加数据似乎不是能够立刻读出来的，延迟一秒以后，才正常可读
		// 应与索引配置 index.refresh_interval 有关，该配置 权衡建索引的性能和检索的时效性
		Thread.sleep(1000);
		SearchRequest searchRequest = new SearchRequest(getIndexName());
		searchRequest.source(searchSourceBuilder(client));
		searchRequest.types(getTypeName());
		SearchResponse res = client.search(searchRequest);
		SearchHits hits = res.getHits();
		System.out.println(hits.totalHits);
		hits.forEach(hit -> {
			System.out.println(hit.getId() + " | " + hit.getScore() + " | " + hit.getSourceAsString());
		});
	}
	
	private void closeClient(RestHighLevelClient client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void createIndex(RestHighLevelClient client) throws Exception;
	
	public abstract void addData(RestHighLevelClient client) throws Exception;
	
	public abstract SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client) throws Exception;

	public void dropIndex(RestHighLevelClient client) throws Exception {
		DeleteIndexRequest req = new DeleteIndexRequest(getIndexName());
		DeleteIndexResponse res = client.indices().delete(req);
	}
	
	public String getIndexName() {
		return "artical";
	}
	
	public String getTypeName() {
		return "sports";
	}
	
}
