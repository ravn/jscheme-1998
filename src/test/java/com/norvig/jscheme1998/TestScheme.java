package com.norvig.jscheme1998;

import static com.norvig.jscheme1998.SchemeUtils.first;
import static com.norvig.jscheme1998.SchemeUtils.num;
import static com.norvig.jscheme1998.SchemeUtils.second;
import static com.norvig.jscheme1998.SchemeUtils.truth;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;

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
	public void testDefine() {
		assertEquals(10.0, num(evalString("(define a 10) a")), 1e-6);
		// remember a?
		assertEquals(10.0, num(evalString("a")), 1e-6);
		assertEquals(20.0, num(evalString("(+ a a)")), 1e-6);

	}
}
