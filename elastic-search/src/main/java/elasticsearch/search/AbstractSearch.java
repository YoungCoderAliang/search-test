package elasticsearch.search;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import elasticsearch.ClientFactory;

public abstract class AbstractSearch {
	public void test() {
		RestHighLevelClient client = ClientFactory.getNewClient();
		try {
			dropIndex(client);
		} catch(Exception e) {
		}
		try {
			createIndex(client);
		} catch(Exception e) {
			e.printStackTrace();
			closeClient(client);
			return;
		}
		try {
			addData(client);
			// MARK 添加数据似乎不是能够立刻读出来的，延迟一秒以后，才正常可读
			// MARK 应与索引配置 index.refresh_interval 有关，该配置 权衡建索引的性能和检索的时效性
			// MARK 下面的代码可以进行手工刷新
			client.indices().refresh(new RefreshRequest(getIndexName()));
			doSearch(client);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			dropIndex(client);
		} catch(Exception e) {
			e.printStackTrace();
		}
		closeClient(client);
	}

	protected void doSearch(RestHighLevelClient client)
			throws InterruptedException, Exception, IOException {
//		Thread.sleep(3000);
		SearchRequest searchRequest = new SearchRequest(getIndexName());
		SearchSourceBuilder builder = new SearchSourceBuilder();
		buildSearch(builder);
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("*");
		highlightBuilder.preTags(" ${");
		highlightBuilder.postTags("} ");
		builder.highlighter(highlightBuilder);
		searchRequest.source(builder);
		searchRequest.types(getTypeName());
		SearchResponse res = client.search(searchRequest);
		SearchHits hits = res.getHits();
		System.out.println(hits.totalHits);
		hits.forEach(hit -> {
			System.out.println(hit.getId() + " | " + hit.getScore() + " | " + hit.getHighlightFields().toString());
		});
	}
	
	private void closeClient(RestHighLevelClient client) {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createIndex(RestHighLevelClient client) throws Exception {
		baseIndex(client);
	}
	
	public void addData(RestHighLevelClient client) throws Exception {
		baseData(client);
	}
	
	public abstract void buildSearch(SearchSourceBuilder builder) throws Exception;

	public void dropIndex(RestHighLevelClient client) throws Exception {
		DeleteIndexRequest req = new DeleteIndexRequest(getIndexName());
		DeleteIndexResponse res = client.indices().delete(req);
	}
	
	public String getIndexName() {
		return "artical";
	}
	
	public String getTypeName() {
		return "sports";
	}
	
	protected void baseIndex(RestHighLevelClient client) throws IOException {
		XContentBuilder mapping = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("properties")
					// MARK ElasticSearch 2.x 文档关于 index 字段已经过时，index在5.x以后，只有 yes 和 no 两个值
					// MARK 如果不希望被分词，那么需要设置 type 为 keyword ，而不是 type=text,index=not_analyzed 组合
						.startObject("aid").field("type","keyword").endObject()
						.startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
						.startObject("content").field("type", "text").field("analyzer", "ik_max_word").endObject()
						.startObject("sport_type").field("type", "keyword").endObject()
						.startObject("score").field("type", "long").endObject()
					.endObject()
				.endObject();
		CreateIndexRequest request = new CreateIndexRequest(getIndexName());
		request.mapping(getTypeName(), mapping);
		CreateIndexResponse res = client.indices().create(request);
		System.out.println(res.isAcknowledged());
	}
	
	protected void baseData(RestHighLevelClient client) throws IOException {
		BulkRequest bulk = new BulkRequest();
		IndexRequest req = new IndexRequest(getIndexName(), getTypeName(), UUID.get());
		XContentBuilder item = XContentFactory.jsonBuilder()
				.startObject()
					.field("aid", "123-321")
					.field("title", "太阳奇才完成3人交易 湖人心爱之人被首都截胡")
					.field("content", "昨天，太阳、奇才和灰熊几乎达成了一桩三方交易，但是由于沟通失误（太阳希望得到灰熊的迪龙-布鲁克斯，但灰熊只愿意送出马肖恩-布鲁克斯），这笔交易瞬间告吹。")
					.field("sport_type", "basket ball")
					.field("score", 3)
				.endObject();
		req.source(item);
		bulk.add(req);
		
		req = new IndexRequest(getIndexName(), getTypeName(), UUID.get());
		item = XContentFactory.jsonBuilder()
				.startObject()
					.field("aid", "123-322")
					.field("title", "总决赛四局横扫水谷隼 林高远将与张本争男单冠军")
					.field("content", "北京时间12月15日消息，2018年国际乒联世界巡回赛总决赛在韩国仁川结束男子单打半决赛，林高远挺进决赛。在与水谷隼的半决赛里，林高远发挥出色以4比0横扫对手闯入决赛，他将与日本的张本智和争夺冠军。")
					.field("sport_type", "table tenis")
				.endObject();
		req.source(item);
		bulk.add(req);

		req = new IndexRequest(getIndexName(), getTypeName(), UUID.get());
		item = XContentFactory.jsonBuilder()
				.startObject()
					.field("aid", "123-323")
					.field("title", "哈登32分三双保罗两双 火箭力取灰熊收三连胜")
					.field("content", "灰熊在最后一节顽强反击。比赛还有3分04秒时，康利两罚两中，灰熊只以91-97落后。哈登强突造成犯规，两罚一中。两队此后相继失误，而在本节还有1分16秒时，哈登在三分线外造成犯规，对他下手的仍是杰克逊。这已经是本场比赛他第三次在三分线外被哈登造犯规。")
					.field("sport_type", "basket ball")
					.field("score", 2)
				.endObject();
		req.source(item);
		bulk.add(req);
		
		req = new IndexRequest(getIndexName(), getTypeName(), UUID.get());
		item = XContentFactory.jsonBuilder()
				.startObject()
					.field("aid", "123-324")
					.field("title", "詹姆斯球哥同时砍三双 湖人三节一波血虐黄蜂")
					.field("content", "黄蜂的崩盘有些意外，湖人的“一波流”让胜负失去了悬念。本节还有3分18秒时，詹姆斯抢下篮板，然后送出助攻，斯蒂芬森投中三分。这一板和一助攻，让詹姆斯达到了三双。")
					.field("sport_type", "basket ball")
					.field("score", 11)
				.endObject();
		req.source(item);
		bulk.add(req);
		
		req = new IndexRequest(getIndexName(), getTypeName(), UUID.get());
		item = XContentFactory.jsonBuilder()
				.startObject()
					.field("aid", "123-325")
					.field("title", "总决赛先丢一局后连扳四局 陈梦力挫何卓佳夺冠")
					.field("content", "北京时间12月16日消息，2018年国际乒联世界巡回赛总决赛在韩国仁川又产生一项冠军，陈梦夺得女单冠军。在决赛里先丢一局的情况下陈梦强势反弹，她连扳四局以4比1击败队友何卓佳夺冠。")
					.field("sport_type", "table tenis")
					.field("score", 55)
				.endObject();
		req.source(item);
		bulk.add(req);
		
		BulkResponse res = client.bulk(bulk);
		
		System.out.println(res.hasFailures() ? res.buildFailureMessage() : "success");
	}
}
