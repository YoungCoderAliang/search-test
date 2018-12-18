package elasticsearch.search.termlevel;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class TermQuerySearch extends AbstractSearch{
	public static void main(String[] args) {
		AbstractSearch test = new TermQuerySearch();
		test.test();
	}

	@Override
	public void buildSearch(SearchSourceBuilder searchSourceBuilder) throws Exception {
		// termQuery 似乎是将value值当作一个 term 去查询，而不是先用分析器进行分词
//		searchSourceBuilder.query(QueryBuilders.termQuery("title", "太阳"));
//		searchSourceBuilder.query(QueryBuilders.termQuery("title", "太阳完成"));
		searchSourceBuilder.query(QueryBuilders.termQuery("aid", "123-321"));
//		searchSourceBuilder.query(QueryBuilders.termQuery("aid", "123"));
	}
}
