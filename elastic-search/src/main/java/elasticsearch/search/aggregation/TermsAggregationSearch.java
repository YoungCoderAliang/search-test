package elasticsearch.search.aggregation;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class TermsAggregationSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new TermsAggregationSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		Terms ts = res.getAggregations().get("sportType");
		for (Terms.Bucket entry : ts.getBuckets()) {
            System.out.println("Key: "+entry.getKey()+"\t\tDoc:"+entry.getDocCount());
        }
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		// select sportType,count(*) from {index} group by sportType
		TermsAggregationBuilder tab = AggregationBuilders.terms("sportType").field("sport_type");
		builder.aggregation(tab);
	}
}
