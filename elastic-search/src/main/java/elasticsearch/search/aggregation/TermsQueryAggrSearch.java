package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class TermsQueryAggrSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new TermsQueryAggrSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		ParsedSum ps = res.getAggregations().get("scoreSum");
		System.out.println(ps.getValue());
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		// select sum(score) as scoreSum from {index} where sport_type = 'basket ball'
		builder.aggregation(AggregationBuilders.sum("scoreSum").field("score"));
		builder.query(QueryBuilders.termQuery("sport_type", "basket ball"));
		builder.size(0);
	}

}
