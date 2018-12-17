package elasticsearch.search.termlevel;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearchTest;

public class TermQuerySearch extends AbstractSearchTest{
	public static void main(String[] args) {
		AbstractSearchTest test = new TermQuerySearch();
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
	public SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client) throws Exception {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// termQuery 似乎是将value值当作一个 term 去查询，而不是先用分析器进行分词
//		searchSourceBuilder.query(QueryBuilders.termQuery("title", "太阳"));
//		searchSourceBuilder.query(QueryBuilders.termQuery("title", "太阳完成"));
		searchSourceBuilder.query(QueryBuilders.termQuery("aid", "123-321"));
//		searchSourceBuilder.query(QueryBuilders.termQuery("aid", "123"));
		return searchSourceBuilder;
	}
}
