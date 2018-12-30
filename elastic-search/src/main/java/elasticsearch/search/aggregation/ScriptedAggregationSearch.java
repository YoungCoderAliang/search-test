package elasticsearch.search.aggregation;

import java.util.ArrayList;
import java.util.HashMap;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.scripted.ParsedScriptedMetric;
import org.elasticsearch.search.aggregations.metrics.scripted.ScriptedMetricAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elasticsearch.search.AbstractSearch;

public class ScriptedAggregationSearch extends AbstractAggregationSearch {
	public static void main(String[] args) {
		AbstractSearch test = new ScriptedAggregationSearch();
		test.test();
	}

	@Override
	protected void showRes(SearchResponse res) {
		System.out.println(res.getAggregations().get("sortedScore").getClass());
		ParsedScriptedMetric metric = res.getAggregations().get("sortedScore");
		@SuppressWarnings("unchecked")
		ArrayList<Long> list = (ArrayList<Long>) metric.aggregation();
		System.out.println(list);
	}

	@Override
	public void buildSearch(SearchSourceBuilder builder) throws Exception {
		// 6.4 版本将 params._agg 修改为 state ， 将 params._aggs 修改为 states
		// 在脚本中加入 Debug.explain(doc.goals) ，即可抛出包含参数类型信息的异常
		ScriptedMetricAggregationBuilder scriptAggr = AggregationBuilders.scriptedMetric("sortedScore")
				.initScript(painless("params._agg.scores = []"))
				.mapScript(painless("params._agg.scores.add(doc.score.getValue())"))
				.combineScript(painless(
						"def sorted = new LinkedList(params._agg.scores); Collections.sort(sorted, Collections.reverseOrder()); if (sorted.size() > 10) {sorted = sorted.subList(0,10);} return sorted; "))
				.reduceScript(painless(
						"def combined = new LinkedList(); params._aggs.forEach(s -> {combined.addAll(s)}); Collections.sort(combined, Collections.reverseOrder()); if (combined.size() > 10) {combined = combined.subList(0,10);} return combined;"));
		// 参考的都是官方文档 https://www.elastic.co/guide/en/elasticsearch/painless/6.3/painless-general-syntax.html#functions
		// for (piece : params._aggs) {combined.addAll(piece);}    不行，不清楚原因
		// params._aggs.forEach(s -> {combined.addAll(s)});   可以。参考文档 Lambda expressions
		builder.aggregation(scriptAggr);
		System.out.println(builder.toString());
	}

	private Script painless(String source) {
		return new Script(ScriptType.INLINE, "painless", source, new HashMap<String, Object>());
	}
}
