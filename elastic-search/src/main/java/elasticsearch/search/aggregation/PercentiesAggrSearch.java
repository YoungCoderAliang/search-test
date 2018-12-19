package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.ParsedTDigestPercentileRanks;
import org.elasticsearch.search.aggregations.metrics.percentiles.tdigest.ParsedTDigestPercentiles;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class PercentiesAggrSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new PercentiesAggrSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		// Test0
		ParsedTDigestPercentiles ptdp = res.getAggregations().get("scorePercent");
		ptdp.forEach(action -> {
			System.out.println(action.getPercent() + " : " + action.getValue());
		});
		
		line();
		
		// Test1
		ParsedTDigestPercentileRanks ptdpr = res.getAggregations().get("rank");
		ptdpr.forEach(action -> {
			System.out.println(action.getValue() + " : " + action.getPercent());
		});
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		// Test0
		// 获取百分比分布
		builder.aggregation(AggregationBuilders.percentiles("scorePercent").field("score"));

		// Test1
		// 获取某个值所处的百分位
		builder.aggregations().addAggregator(AggregationBuilders.percentileRanks("rank", new double[] { 10, 33 }).field("score"));
	}

}
