package elasticsearch.search.termlevel;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class TermsQuerySearch extends AbstractSearch {
	public static void main(String[] args) {
		AbstractSearch test = new TermsQuerySearch();
		test.test();
	}

	@Override
	public void buildSearch(SearchSourceBuilder searchSourceBuilder) throws Exception {
		// elasticsearch.yml 配置 index.max_terms_count ， 可以限制最大的terms数目
		searchSourceBuilder.query(QueryBuilders.termsQuery("content", "决赛", "冠军", "比分"));
	}

}
