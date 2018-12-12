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
		XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("title", "lucene ��һ��������������")
				.field("content", "Lucene��apache��������4 jakarta��Ŀ���һ������Ŀ����һ������Դ�����ȫ�ļ������湤�߰�����������һ��������ȫ�ļ������棬����һ��ȫ�ļ�������ļܹ����ṩ�������Ĳ�ѯ������������棬�����ı��������棨Ӣ������������������ԣ�").field("sport_type", "��ש").endObject();
		req.source(builder);

		IndexResponse res = client.index(req);
		System.out.println(res.getResult());

		client.close();
	}
}
