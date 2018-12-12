package elasticsearch;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

public class PutData {
	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = ClientFactory.getNewClient();

		IndexRequest req = new IndexRequest("artical", "sports", "1");
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("title", "lucene 是一个单机搜索引擎")
				.field("content", "Lucene是apache软件基金会4 jakarta项目组的一个子项目，是一个开放源代码的全文检索引擎工具包，但它不是一个完整的全文检索引擎，而是一个全文检索引擎的架构，提供了完整的查询引擎和索引引擎，部分文本分析引擎（英文与德文两种西方语言）").field("sport_type", "搬砖").endObject();
		req.source(builder);

		IndexResponse res = client.index(req);
		System.out.println(res.getResult());

		client.close();
	}
}
