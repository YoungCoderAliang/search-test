package elasticsearch.search;

public class Test {
	public static void main(String[] args) {
		AbstractSearchTest test = new ExactSearch();
		test.test();
	}
}
