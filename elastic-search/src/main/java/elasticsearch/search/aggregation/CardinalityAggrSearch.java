package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class CardinalityAggrSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new CardinalityAggrSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		ParsedCardinality pc = res.getAggregations().get("sportType");
		System.out.println(pc.getValue());
		
		line();
		
		ParsedCardinality pc2 = res.getAggregations().get("sportTypeAid");
		System.out.println(pc2.getValue());
	}

	// Cardinality去重，是一个准确但是不精确的值，实在 精确+分布式+实时性 中，取了后两者的特性
	// hadoop 会取 精确+分布式，但是通常是离线计算
	// 而想要做到 精确+实时性，那么只有单机
	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		// Test0
		// select count(distinct(sport_type)) from {index}
		builder.aggregation(AggregationBuilders.cardinality("sportType").field("sport_type"));
		
		// Test1
		// select count(distinct(sport_type,aid)) from {index}
		builder.aggregations().addAggregator(AggregationBuilders.cardinality("sportTypeAid").field("sport_type").field("aid"));
	}
}
