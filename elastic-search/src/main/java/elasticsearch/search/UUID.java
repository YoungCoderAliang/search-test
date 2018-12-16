package elasticsearch.search;

import java.util.Random;

import org.elasticsearch.common.Strings;

public class UUID {
	private static Random r = new Random();

	public static String get() {
		return System.currentTimeMillis() + "-"
				+ Strings.padStart(String.valueOf(r.nextInt(10000)), 4, '0');
	}
	
	public static void main(String[] args) {
		System.out.println(get());
	}
}
