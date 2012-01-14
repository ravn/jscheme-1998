package com.norvig.jscheme1998;

import static com.norvig.jscheme1998.SchemeUtils.num;
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
	public void test() {
		// constructed initial example, replace with actual code string later.
		Object o = scheme.eval(new Pair("quote", new Pair("42", null)));
		assertEquals("eval working?", "42", o);

		// all numbers are doubles
		assertEquals(1.0, num(evalString("1")), 1e-6);
		assertEquals(42.0, num(evalString("'42")), 1e-6);

		assertEquals(2.0, num(evalString("(+ 1 1)")), 1e-6);
		assertEquals(4.0, num(evalString("(+ 2 2)")), 1e-6);
		assertEquals(42.0, num(evalString("(* 6 7)")), 1e-6);

		// Object l1 = evalString("(time (zero? 0) 1000)"); // confirm
		// availability
		// Assert.assertNotNull(l1);
	}
}
