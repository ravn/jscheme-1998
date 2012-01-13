package com.norvig.jscheme1998;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestScheme {

	@Test
	public void test() {
		Scheme scheme = new Scheme(null);
		// constructed initial example, replace with actual code string later.
		Object o = scheme.eval(new Pair("quote", new Pair("42", null))); 
		assertEquals("eval working?", "42", o);
	}
}
