package elasticsearch.search;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ComposeSearch extends AbstractSearch {
	public static void main(String[] args) {
		AbstractSearch test = new ComposeSearch();
		test.test();
	}

	@Override
	public void createIndex(RestHighLevelClient client) throws Exception {
		baseIndex(client);
	}

	@Override
	public void addData(RestHighLevelClient client) throws Exception {
		baseData(client);
	}

	@Override
	public SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client)
			throws Exception {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("title", "太阳"))
//				.must(QueryBuilders.termQuery("content", "交易")));
				.must(QueryBuilders.termQuery("content", "明星")));
		return searchSourceBuilder;
	}

}
