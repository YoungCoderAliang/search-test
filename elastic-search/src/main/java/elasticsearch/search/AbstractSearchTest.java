package elasticsearch.search;

import java.io.IOException;

import org.elasticsearch.client.RestHighLevelClient;

import elasticsearch.ClientFactory;

public abstract class AbstractSearchTest {
	public void test() {
		RestHighLevelClient client = ClientFactory.getNewClient();
		try {
			createIndex(client);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		try {
			addData(client);
			testSearch(client);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			dropIndex(client);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void createIndex(RestHighLevelClient client) throws Exception;
	
	public abstract void addData(RestHighLevelClient client) throws Exception;
	
	public abstract void testSearch(RestHighLevelClient client) throws Exception;

	public abstract void dropIndex(RestHighLevelClient client) throws Exception;
	
}
