package com.norvig.jscheme1998;

import static com.norvig.jscheme1998.SchemeUtils.first;
import static com.norvig.jscheme1998.SchemeUtils.list;
import static com.norvig.jscheme1998.SchemeUtils.num;
import static com.norvig.jscheme1998.SchemeUtils.second;
import static com.norvig.jscheme1998.SchemeUtils.truth;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestScheme {

	private Scheme scheme;

	@Before
	public void before() {
		scheme = new Scheme(null);
		// need close()?
	}

	public Object evalString(String s) {
		Object result = null;
		Object x;

		InputPort inputPort = new InputPort(new StringReader(s));
		while (InputPort.isEOF(x = inputPort.read()) == false) {
			result = scheme.eval(x);
		}
		return result;
	}

	@Test
	public void testBuiltinConstants() {
		Object t = evalString("#t");
		assertTrue(truth(t));
		Object f = evalString("#f");
		assertTrue(truth(f) == false);
	}

	@Test
	public void testSimpleCalculations() {
		// all numbers are doubles
		assertEquals(1.0, num(evalString("1")), 1e-6);
		assertEquals(42.0, num(evalString("'42")), 1e-6);

		assertEquals(2.0, num(evalString("(+ 1 1)")), 1e-6);
		assertEquals(4.0, num(evalString("(+ 2 2)")), 1e-6);
		assertEquals(42.0, num(evalString("(* 6 7)")), 1e-6);
		assertEquals(42.0, num(evalString("(* (+ 2 4) (- 8 1))")), 1e-6);

		assertEquals(42.0, num(evalString("(* (/ 12 2) (/ 21 3))")), 1e-6);

		// Object l1 = evalString("(time (zero? 0) 1000)"); // confirm
		// availability
		// Assert.assertNotNull(l1);
	}

	@Test
	public void testSimpleExtensions() {
		assertEquals(1.0, evalString("(first '(1 2 3))"));
		assertEquals(2.0, evalString("(second '(1 2 3))"));
		assertEquals(3.0, evalString("(third '(1 2 3))"));
	}

	@Test
	public void testTime() {
		Object o = evalString("(time (zero? 0) 10)");
		// (#t (0 msec) (480 bytes))
		Object o1 = first(o);
		Object o2 = second(o); // (0 msec)
		Object o3 = SchemeUtils.third(o); // (480 bytes)

		assertTrue(truth(o1)); // #t

		assertTrue("complete in <1 sec?", num(first(o2)) < 1000);
		assertEquals("msec", second(o2));

		assertTrue(num(first(o3)) >= 0);
		assertEquals("bytes", second(o3));

	}

	@Test
	public void testDefineAndSet() {
		assertEquals(10.0, num(evalString("(define a 10) a")), 1e-6);
		// remember a?
		assertEquals(10.0, num(evalString("a")), 1e-6);
		assertEquals(20.0, num(evalString("(+ a a)")), 1e-6);

		// reassign a
		assertEquals(5.0, num(evalString("(set! a 5) a")), 1e-6);
		assertEquals(5.0, num(evalString("a")), 1e-6);
		assertEquals(10.0, num(evalString("(+ a a)")), 1e-6);

		evalString("(define (square x) (* x x))");
		assertEquals(25.0, num(evalString("(square a)")), 1e-6);
	}

	@Test
	public void testIf() {
		evalString("(define a 10)");
		assertEquals(10.0, num(evalString("a")), 1e-6);
		assertEquals(1.0, num(evalString("(if (<= a 10) 1 a)")), 1e-6);
		assertEquals(10.0, num(evalString("(if (< a 10) 1 a)")), 1e-6);
		assertEquals(20.0, num(evalString("(if (< a 10) 1 (+ a a))")), 1e-6);
	}

	@Test
	public void testFibbonacci() {
		// naive, slow implementation
		evalString("(define fib\n"
				+ "  (lambda (n)\n"
				+ "    (cond ((= n 0) 0)  \n" //
				+ "          ((= n 1) 1)\n"
				+ "          (else (+ (fib (- n 1))\n"
				+ "                   (fib (- n 2)))))))");

		assertEquals(5.0, num(evalString("(fib 5)")), 1e-6);
		assertEquals(55.0, num(evalString("(fib 10)")), 1e-6);
	}

	@Test(expected = StackOverflowError.class)
	public void testRecursionDepth() {
		// jscheme interpreter just calls itself for expressions
		evalString("(define x\n"
				+ "  (lambda (n)\n"
				+ "    (cond ((= n 0) 0)  \n" //
				+ "          ((= n 1) 1)\n" //
				+ "          (else (+ 1\n"
				+ "                   (x (- n 1)))))))");
		num(evalString("(x 1)"));
		num(evalString("(x 10)"));
		num(evalString("(x 100)"));
		num(evalString("(x 1000)"));
		// around here for default values
		num(evalString("(x 10000)"));
		num(evalString("(x 100000)"));
	}

	@Test
	public void testSimplePropertiesAsStringList() {
		Object o = evalString("(list \"a\" \"b\" \"c\" \"d\")");
		Object[] v = SchemeUtils.listToVector(o);

		assertEquals(4, v.length);
		assertEquals("a", str(v[0]));
		assertEquals("b", str(v[1]));
		assertEquals("c", str(v[2]));
		assertEquals("d", str(v[3]));
	}

	@Test
	public void testSimplePropertiesWithNumbericValues() {
		Object o = evalString("(list \"a\" 0.1 \"c\" 100)");
		Object[] v = SchemeUtils.listToVector(o);

		assertEquals(4, v.length);
		assertEquals("a", str(v[0]));
		assertEquals("0.1", str(v[1]));
		assertEquals("c", str(v[2]));
		assertEquals("100", str(v[3]));
	}

	@Test
	public void testPropertiesWithAppendAsStringList() {
		Object o = evalString("(list "
				+ "\"a\" (string-append \"b\" \"c\" \"d\") "
				+ "\"e\" (string-append \"f\"))");
		Object[] v = SchemeUtils.listToVector(o);

		assertEquals(4, v.length);
		assertEquals("a", str(v[0]));
		assertEquals("bcd", str(v[1]));
		assertEquals("e", str(v[2]));
		assertEquals("f", str(v[3]));
	}

	@Test
	public void testMultiValueList() {
		Object o = evalString("(list \n" + //
				" \"a\" \n" + //
				" (list \"b1\" \"b2\")\n" + //
				" \"c\"\n" + //
				" ()\n" + //
				")\n");
		assertNotNull(o);
		Object[] v = SchemeUtils.listToVector(o);
		assertEquals(4, v.length);
		Object first = v[0];
		assertEquals("a", str(first));

		Object second = v[1];
		Assert.assertTrue(second instanceof Pair);
		Object[] secondVector = SchemeUtils.listToVector(second);
		Object[] x2 = new Object[2];
		x2[0] = "b1".toCharArray();
		x2[1] = "b2".toCharArray();
		assertArrayEquals(x2, secondVector);

		Object third = v[2];
		assertEquals("c", str(third));

		Object fourth = v[3];
		Assert.assertEquals(fourth, null);
	}

	@Test
	public void testSingleJavaCall() {
		Object o = evalString("(define show (method \"getLocalHost\" \"java.net.InetAddress\"))(show)");
		assertNotNull(o);

	}

	@Test
	public void testGetAndEvalSimpleLambda() {
		String s = "(lambda (x y) (+ x y))";
		Object o = evalString(s);
		assertTrue("Closure", o instanceof Closure);
		Closure c = (Closure) o;

		Object x = evalString("10");
		Object y = evalString("20");

		Object sum = c.apply(scheme, list(x, y));

		assertEquals("lambda", "30", str(sum));
		Pair l40 = list(evalString("-10"), evalString("50"));
		assertEquals("40", str(c.apply(scheme, l40)));
		Pair l140 = list(evalString("-10.5"), evalString("150.5"));
		assertEquals("140", str(c.apply(scheme, l140)));
		Pair l0 = list(evalString("-0.333"), evalString("0.333"));
		assertEquals("0", str(c.apply(scheme, l0)));
	}

	@Test
	public void testMakingMapAvailable() {
		String s = "(define basjavad-map (list \"a\" \"a1\" \"b\" \"b2\"))";
		Object o = evalString(s);
		// map defined in interpreter
		assertEquals("a1", str(evalString("(second basjavad-map)")));
	}

	@Test
	public void testMakingMapAvailableAsAlist() {
		Map<String, String> map = new LinkedHashMap<String, String>();// keep
																		// order
		map.put("a", "alpha");
		map.put("b", "beta");
		map.put("d", "delta");

		StringBuffer sb = new StringBuffer();
		sb.append("(define m '(");
		Set<Entry<String, String>> es = map.entrySet();
		for (Entry<String, String> entry : es) {
			// no need to protect these values
			sb.append("(\"" + entry.getKey() + "\" \"" + entry.getValue()
					+ "\")");
		}
		sb.append("))");
		System.out.println(sb);
		Object o = evalString(sb.toString());
		assertEquals("((\"a\" \"alpha\") (\"b\" \"beta\") (\"d\" \"delta\"))",
				str(evalString("m")));

		assertEquals("alpha", str(evalString("(second (assoc \"a\" m))")));
		assertEquals("beta", str(evalString("(second (assoc \"b\" m))")));
		assertEquals("delta", str(evalString("(second (assoc \"d\" m))")));

		assertNull(str(evalString("(second (assoc \"c\" m))")));

	}

	static String str(Object v0) {
		if (v0 instanceof char[]) {
			return new String(SchemeUtils.str(v0));
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
}
