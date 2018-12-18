package elasticsearch.search.termlevel;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class TermsSetQuerySearch extends AbstractSearch {
	public static void main(String[] args) {
		AbstractSearch test = new TermsSetQuerySearch();
		test.test();
	}

	@Override
	public SearchSourceBuilder searchSourceBuilder(RestHighLevelClient client) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
