package elasticsearch.search.aggregation;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public abstract class AbstractAggregationSearch extends AbstractSearch {
	
	public void doSearch(RestHighLevelClient client)
			throws InterruptedException, Exception, IOException {
		SearchRequest searchRequest = new SearchRequest(getIndexName());
		SearchSourceBuilder builder = new SearchSourceBuilder();
		buildSearch(builder);
		searchRequest.source(builder);
		searchRequest.types(getTypeName());
		SearchResponse res = client.search(searchRequest);
		SearchHits hits = res.getHits();
		System.out.println(hits.totalHits);
		hits.forEach(hit -> {
			System.out.println(hit.getId() + " | " + hit.getScore() + " | " + hit.getSourceAsString());
		});
		showRes(res);
		System.out.println(res.toString());
	}

	protected abstract void showRes(SearchResponse res);
}
