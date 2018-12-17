package elasticsearch.search.termlevel;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearchTest;

public class TermsSetQuerySearch extends AbstractSearchTest {
	public static void main(String[] args) {
		AbstractSearchTest test = new TermsSetQuerySearch();
		test.test();
	}

	@Override
	public SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
