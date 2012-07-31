package com.norvig.jscheme1998.jsr223;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SchemeScriptEngineTest {

	ScriptEngine engine;

	@Before
	public void setUp() throws Exception {
		engine = new ScriptEngineManager().getEngineByName("jscheme1998");
	}

	@Test
	public void testSimple() throws ScriptException {
		Object helloWorld = engine.eval("\"Hello World\"");

		assertSchemeEquals("Hello World", helloWorld);

		assertEquals(Boolean.TRUE, engine.eval("#t"));

		assertEquals(Boolean.FALSE, engine.eval("#f"));

		Assert.assertNull(engine.eval("()"));
	}

	@Test
	public void testSimplePutAndEval() throws ScriptException {
		engine.eval("(define sp \" \")");
		engine.put("h", "Hello"); // should be char[]
		engine.put("w", "World"); // should be char[]

		Object helloWorld = engine.eval("(string-append h sp w)");
		assertSchemeEquals("Hello World", helloWorld);

	}

	@Test
	public void testPutStringsToVectorAndEval() throws ScriptException {
		Object[] abc = { "a".toCharArray(), "b".toCharArray(),
				"c".toCharArray() };
		engine.put("abc", abc);
		Object eval = engine.eval("(vector-ref abc 1)");
		assertSchemeEquals("b", eval);
	}

	@Test
	public void testPutStringMapToAListAndEval() throws ScriptException {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("a", "alpha");
		map.put("b", "beta");
		map.put("d", "delta");

		engine.put("map", SchemeScriptEngineHelper.mapToAssociationList(map));

		Object o = engine.eval("map");
		assertSchemeEquals("alpha", engine.eval("(second (assoc \"a\" map))"));
		assertSchemeEquals("beta", engine.eval("(second (assoc \"b\" map))"));
		assertSchemeEquals("delta", engine.eval("(second (assoc \"d\" map))"));
	}

	private void assertSchemeEquals(String string, Object o) {
		if (o instanceof String) {
			assertEquals(string, (String) o);
		} else if (o instanceof char[]) {
			assertEquals(string, new String((char[]) o));
		} else {
			assertEquals(string, o); // Object, Object.
		}
	}
}
