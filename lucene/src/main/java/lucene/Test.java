package lucene;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Test {
	private static Analyzer analyzer = new IKAnalyzer();

	public static void main(String[] args) throws IOException, ParseException,
			InvalidTokenOffsetsException {
		// add("lucene 是什麽",
		// "Lucene最初由鼎鼎大名Doug Cutting开发，2000年开源，现在也是开源全文检索方案的不二选择，它的特点概述起来就是：全Java实现、开源、高性能、功能完整、易拓展，功能完整体现在对分词的支持、各种查询方式（前缀、模糊、正则等）、打分高亮、列式存储（DocValues）等等。 ");
		// add("啥啊", "Lucene 啥也不是");
		// add("哈哈哈啊哈",
		// "其中词典结构尤为重要，有很多种词典结构，各有各的优缺点，最简单如排序数组，通过二分查找来检索数据，更快的有哈希表，磁盘查找有B树、B+树，但一个能支持TB级数据的倒排索引结构需要在时间和空间上有个平衡，下图列了一些常见词典的优缺点");
		// add("lucene的原理",
		// "全文检索技术由来已久，绝大多数都基于倒排索引来做，曾经也有过一些其他方案如文件指纹。倒排索引，顾名思义，它相反于一篇文章包含了哪些词，它从词出发，记载了这个词在哪些文档中出现过，由两部分组成——词典和倒排表。");
		//
		// searchFiles("lucene");

		Query query = new TermQuery(new Term("content", "数据"));
		searchFiles(query);
		query = new TermQuery(new Term("content", "数"));
		searchFiles(query);
		query = new PrefixQuery(new Term("content", "数据"));
		searchFiles(query);
		query = new PrefixQuery(new Term("content", "数"));
		searchFiles(query);
	}

	public static class Article {
		private Integer id;
		private String title;
		private String content;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public static void add(String title, String content) throws IOException {

		Article article = new Article();
		article.setId(1);
		article.setTitle(title);
		article.setContent(content);

		final Path path = Paths.get("./chineseArtical/");

		Directory directory = FSDirectory.open(path);
		Analyzer analyzer = new IKAnalyzer();

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig
				.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

		Document document = new Document();
		document.add(new TextField("title", article.getTitle(), Field.Store.YES));
		document.add(new TextField("content", article.getContent(),
				Field.Store.YES));

		indexWriter.addDocument(document);
		indexWriter.commit();
		indexWriter.close();

	}

	public static void searchFiles(Query query) throws IOException,
			ParseException, InvalidTokenOffsetsException {
		// String queryString = "全文检索";

		// 多条件
		// Query q = MultiFieldQueryParser.parse(new String[]{},new
		// String[]{},new StandardAnalyzer());

		final Path path = Paths.get("./chineseArtical/");
		Directory directory = FSDirectory.open(path);

		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		// 单条件

		TopDocs topDocs = indexSearcher.search(query, 10);

		long conut = topDocs.totalHits;
		System.out.println("检索总条数：" + conut);

		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("【",
				"】");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter,
				new QueryScorer(query));

		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			System.out.println("相关度：" + scoreDoc.score);
			System.out.println("title:" + document.get("title"));
			System.out.println("content" + document.get("content"));

			TokenStream tokenStream = analyzer.tokenStream("content",
					new StringReader(document.get("content")));
			String content = highlighter.getBestFragment(tokenStream,
					document.get("content"));
			System.out.println("high light : " + content);
		}
		System.out.println("========================");
	}
}
