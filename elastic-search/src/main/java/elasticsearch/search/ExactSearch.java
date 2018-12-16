package elasticsearch.search;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ExactSearch extends AbstractSearchTest{
	public static void main(String[] args) {
		AbstractSearchTest test = new ExactSearch();
		test.test();
	}

	@Override
	public void createIndex(RestHighLevelClient client) throws Exception {
		XContentBuilder mapping = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("properties")
					// ElasticSearch 2.x 文档关于 index 字段已经过时，index在5.x以后，只有 yes 和 no 两个值
					// 如果不希望被分词，那么需要设置 type 为 keyword ，而不是 type=text,index=not_analyzed 组合
						.startObject("aid").field("type","keyword").endObject()
						.startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
						.startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject()
						.startObject("sport_type").field("type", "text").field("analyzer", "ik_max_word").endObject()
					.endObject()
				.endObject();
		CreateIndexRequest request = new CreateIndexRequest(getIndexName());
		request.mapping(getTypeName(), mapping);
		CreateIndexResponse res = client.indices().create(request);
		System.out.println(res.isAcknowledged());
	}

	@Override
	public void addData(RestHighLevelClient client) throws Exception {
		IndexRequest req = new IndexRequest(getIndexName(), getTypeName(), UUID.get());
		XContentBuilder item = XContentFactory.jsonBuilder()
				.startObject()
					.field("aid", "123-321")
					.field("title", "太阳奇才完成3人交易 湖人心爱之人被首都截胡")
					.field("content", "昨天，太阳、奇才和灰熊几乎达成了一桩三方交易，但是由于沟通失误（太阳希望得到灰熊的迪龙-布鲁克斯，但灰熊只愿意送出马肖恩-布鲁克斯），这笔交易瞬间告吹。")
					.field("sport_type", "basket ball")
				.endObject();
		req.source(item);
		IndexResponse res = client.index(req);
		System.out.println(res.getResult());
	}

	@Override
	public SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client) throws Exception {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// termQuery 似乎是将value值当作一个 term 去查询，而不是先用分析器进行分词
//		searchSourceBuilder.query(QueryBuilders.termQuery("title", "太阳"));
//		searchSourceBuilder.query(QueryBuilders.termQuery("title", "太阳完成"));
		searchSourceBuilder.query(QueryBuilders.termQuery("aid", "123-321"));
//		searchSourceBuilder.query(QueryBuilders.termQuery("aid", "123"));
		return searchSourceBuilder;
	}
}
