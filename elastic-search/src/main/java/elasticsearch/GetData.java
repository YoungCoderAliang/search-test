package elasticsearch;

import java.io.IOException;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RestHighLevelClient;

public class GetData {
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = ClientFactory.getNewClient();

		GetRequest req = new GetRequest("artical", "sports", "1");
		
		GetResponse res = client.get(req);
		System.out.println(res.getId());
		System.out.println(res.getIndex());
		System.out.println(res.getType());
		System.out.println(res.getSourceAsString());
		
		client.close();
	}
}
