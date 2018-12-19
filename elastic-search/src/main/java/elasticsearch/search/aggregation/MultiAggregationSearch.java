package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class MultiAggregationSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new MultiAggregationSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		Terms ts = res.getAggregations().get("sportType");
		ts.getBuckets().forEach(action -> {
			System.out.println(action.getKey() + " : " + action.getDocCount());
		});
		
		line();
		
		ParsedCardinality pc = res.getAggregations().get("sportNum");
		System.out.println(pc.getValue());
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		builder.aggregation(AggregationBuilders.terms("sportType").field("sport_type"));
		builder.aggregations().addAggregator(AggregationBuilders.cardinality("sportNum").field("sport_type"));
	}
}
