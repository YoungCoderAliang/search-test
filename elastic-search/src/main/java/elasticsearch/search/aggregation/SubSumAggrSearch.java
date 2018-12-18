package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class SubSumAggrSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new SubSumAggrSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		Terms ts = res.getAggregations().get("sportType");
		for (Terms.Bucket entry : ts.getBuckets()) {
            System.out.println("Key: "+entry.getKey()+"\t\tDoc:"+entry.getDocCount());
            Terms t1 = entry.getAggregations().get("aid");
            for (Terms.Bucket e1 : t1.getBuckets()) {
            	System.out.println("sub Key: " + entry.getKey() + "\t\tDoc:" + e1.getDocCount());
            }
        }
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		// select sport_type as sportType, aid as aid sum(score) as scoreSum from {index} group by sportType,aid
		TermsAggregationBuilder tab = AggregationBuilders.terms("sportType").field("sport_type");
		TermsAggregationBuilder tab1 = AggregationBuilders.terms("aid").field("aid");
		SumAggregationBuilder sab = AggregationBuilders.sum("scoreSum").field("score");
		tab1.subAggregation(sab);
		tab.subAggregation(tab1);
		builder.aggregation(tab);
	}

}
