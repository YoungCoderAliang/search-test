package elasticsearch;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;

public class SearchData {
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = ClientFactory.getNewClient();

		SearchRequest searchRequest = new SearchRequest("artical");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.query(QueryBuilders.termQuery("title", "引擎"));
//		searchSourceBuilder.query(
//				QueryBuilders.matchQuery("content", "英文单词").fuzziness(Fuzziness.AUTO).prefixLength(1).maxExpansions(10));

		// 排序
		// searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));

		// 不查询source
//		searchSourceBuilder.fetchSource(false);
//		String[] includeFields = new String[] {"title"};
//		String[] excludeFields = new String[] {"content"};
//		searchSourceBuilder.fetchSource(includeFields, excludeFields);

		// 高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		Field highlightTitle = new Field("title");
		highlightTitle.highlighterType("unified");
		highlightBuilder.field(highlightTitle);

		searchRequest.source(searchSourceBuilder);

		searchRequest.types("sports");

		SearchResponse res = client.search(searchRequest);
		SearchHits hits = res.getHits();
		System.out.println(hits.totalHits);
		hits.forEach(hit -> {
			System.out.println(hit.getId() + " | " + hit.getScore() + " | " + hit.getSourceAsString());
		});
		client.close();
	}
}
