package com.norvig.jscheme1998;

import static com.norvig.jscheme1998.TestScheme.str;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestSchemeResources {
	private Scheme scheme;

	@Before
	public void before() {
		scheme = new Scheme(null);
		// need close()?
	}

	public Object evalResource(String resource) {
		Object result = null;
		Object expression;

		InputStream is = getClass().getResourceAsStream(resource);
		Assert.assertNotNull(resource, is);

		InputPort inputPort = new InputPort(is);
		while (InputPort.isEOF(expression = inputPort.read()) == false) {
			result = scheme.eval(expression);
		}
		try {
			is.close();
		} catch (IOException e) {
			Assert.fail("could not close " + resource);
		}
		return result;
	}

	@Test
	public void testSimpleList() {
		Object o = evalResource("simple-list.scm");
		Assert.assertNotNull(o);
		Object[] v = SchemeUtils.listToVector(o);
		assertEquals(4, v.length);
		assertEquals("a", str(v[0]));
		assertEquals("b", str(v[1]));
		assertEquals("c", str(v[2]));
		assertEquals("d", str(v[3]));

	}
}
