package elasticsearch.search;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class MatchSearch extends AbstractSearch {
	public static void main(String[] args) {
		AbstractSearch test = new MatchSearch();
		test.test();
	}

	@Override
	public void createIndex(RestHighLevelClient client) throws Exception {
		baseIndex(client);
	}

	@Override
	public void addData(RestHighLevelClient client) throws Exception {
		baseData(client);
	}

	@Override
	public SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client)
			throws Exception {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 单字段匹配，match匹配会先进行分词
//		searchSourceBuilder.query(QueryBuilders.matchQuery("content", "时间简史"));
		// 多字段匹配
//		searchSourceBuilder.query(QueryBuilders.multiMatchQuery("决赛连胜", "content", "title"));
		// CommonTermsQuery ，主要用于处理停词（出现频率极高的词，比如 the、not、and）
		// CommonTermsQuery 处理的逻辑是，将词分成两类，一类是重要词语，一类是停词（stopwords或者common terms）。
		// 如果查询中有重要的词语，那么将首先应用重要词语进行查询，然后使用停词占比调整查询出来的结果得分
		// 如果查询中全部是停词，那么将只返回拥有全部查询停词的文档
		// 可以设置 cutoff frequency 来区分停词，出现频率超过该数值，则认为是停词
//		searchSourceBuilder.query(QueryBuilders.matchQuery("content", "决赛的连胜"));
		searchSourceBuilder.query(QueryBuilders.commonTermsQuery("content", "决赛的连胜").cutoffFrequency(0.1f));
		
		return searchSourceBuilder;
	}
}
