package elasticsearch.search.termlevel;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class RangeQuerySearch extends AbstractSearch {
	public static void main(String[] args) {
		AbstractSearch test = new RangeQuerySearch();
		test.test();
	}

	@Override
	public void buildSearch(SearchSourceBuilder searchSourceBuilder) throws Exception {
		searchSourceBuilder.query(QueryBuilders.rangeQuery("score").gt(0).lt(30));
	}

}
