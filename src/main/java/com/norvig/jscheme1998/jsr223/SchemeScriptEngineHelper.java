package com.norvig.jscheme1998.jsr223;

import static com.norvig.jscheme1998.SchemeUtils.num;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.norvig.jscheme1998.Pair;
import com.norvig.jscheme1998.SchemeUtils;

public class SchemeScriptEngineHelper {

	/**
	 * Convert a Map<?,?> to an association list. <code>map.put("a", "alpha");
		map.put("b", "beta");
		map.put("d", "delta");</code>
	 * becomes <code>'(("a" "alpha")("b" "beta")("d" "delta"))</code>
	 * 
	 * @param map
	 * @return
	 */
	public static Pair mapToAssociationList(Map<?, ?> map) {
		// build list of ("a" "alpha")("b" "beta")("d" "delta") pairs.
		List<Object> l = new ArrayList<Object>();
		for (Entry<?, ?> entry : map.entrySet()) {
			// Note: Strings are converted to char[] for JScheme
			Object key = entry.getKey();
			if (key instanceof String) {
				key = ((String) key).toCharArray();
			}
			Object value = entry.getValue();
			if (value instanceof String) {
				value = ((String) value).toCharArray();
			}
			l.add(SchemeUtils.vectorToList(new Object[] { key, value }));
		}
		// and convert that list to a scheme association list.
		Pair alist = SchemeUtils.vectorToList(l.toArray());
		return alist;
	}

	/**
	 * Converts a Scheme atom to a useful string representation. Notably
	 * char[]'s are returned as Strings, Numbers are returned as whole numbers
	 * if less than 1e-4 from that whole number, otherwise with a decimal
	 * fraction (this avoids the "30.0" strings). <code>()</code> returns as a
	 * null. All other values are returned as their toString().
	 * 
	 * @param v0
	 * @return
	 */
	public static String str(Object v0) {
		if (v0 instanceof char[]) {
			return new String((char[]) v0);
		}
		if (v0 instanceof Number) {
			double d = num(v0);
			long l = Math.round(d);
			if (Math.abs(d - l) < 1e-4) {
				return "" + l;
			} else {
				return "" + num(v0);
			}
		}
		if (v0 == null) {
			return null;
		}
		return v0.toString();
	}

	public static Map<String, Object> schemeListToJavaMap(Object schemeList) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		Object l = schemeList;
		while (l != null) {
			String first = str(SchemeUtils.first(l));
			Object second = SchemeUtils.second(l);
			if (second instanceof char[] || second instanceof Number) {
				second = str(second);
			}
			map.put(first, second);

			l = SchemeUtils.rest(SchemeUtils.rest(l));
		}
		return map;
	}
}
