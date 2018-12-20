package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class SubAggregationSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new SubAggregationSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		Terms ts = res.getAggregations().get("sportType");
		for (Terms.Bucket entry : ts.getBuckets()) {
			ParsedSum ps = entry.getAggregations().get("scoreSum");
            System.out.println("Key: "+entry.getKey()+"\t\tscoreSum:"+ps.getValue());
        }
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
//		 select sport_type as sportType, sum(score) as scoreSum from {index} group by sportType
		TermsAggregationBuilder tab = AggregationBuilders.terms("sportType").field("sport_type");
		SumAggregationBuilder sab = AggregationBuilders.sum("scoreSum").field("score");
		tab.subAggregation(sab);
		builder.aggregation(tab);
	}

}
