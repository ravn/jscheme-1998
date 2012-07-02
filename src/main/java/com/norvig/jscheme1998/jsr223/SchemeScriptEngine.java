package com.norvig.jscheme1998.jsr223;

import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

import com.norvig.jscheme1998.Environment;
import com.norvig.jscheme1998.InputPort;
import com.norvig.jscheme1998.Scheme;

/**
 * Duct tape implementation. Only has enough functionality for now to do what we
 * need here. See <a href=
 * "http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html"
 * >http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/
 * programmer_guide/index.html</a> for details
 * 
 */
public class SchemeScriptEngine extends AbstractScriptEngine implements
		Invocable {

	public SchemeScriptEngine() {
		scheme = new Scheme(null);
		Environment env = scheme.getGlobalEnvironment();
		int scope = ScriptContext.ENGINE_SCOPE;
		getContext().setBindings(new SchemeEnvironmentBindings(env), scope);
	}

	Scheme scheme;

	// @Override
	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		return eval(new StringReader(script), context);
	}

	// @Override
	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {

		Object result = null;
		Object x;

		InputPort inputPort = new InputPort(reader);
		while (InputPort.isEOF(x = inputPort.read()) == false) {
			result = scheme.eval(x);
		}
		return result;
	}

	// @Override
	public Bindings createBindings() {
		throw new UnsupportedOperationException();
	}

	// @Override
	public ScriptEngineFactory getFactory() {
		return new SchemeScriptEngineFactory();
	}

	// @Override
	public Object invokeMethod(Object thiz, String name, Object... args)
			throws ScriptException, NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	// @Override
	public Object invokeFunction(String name, Object... args)
			throws ScriptException, NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	// @Override
	public <T> T getInterface(Class<T> clasz) {
		throw new UnsupportedOperationException();
	}

	// @Override
	public <T> T getInterface(Object thiz, Class<T> clasz) {
		throw new UnsupportedOperationException();
	}

}
