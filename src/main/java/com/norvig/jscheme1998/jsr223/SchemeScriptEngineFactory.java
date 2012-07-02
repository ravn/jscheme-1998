package com.norvig.jscheme1998.jsr223;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public final class SchemeScriptEngineFactory implements ScriptEngineFactory {
	public ScriptEngine getScriptEngine() {
		return new SchemeScriptEngine();
	}

	public String getProgram(String... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOutputStatement(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getNames() {
		List<String> l = new ArrayList<String>();
		l.add("scheme1998");
		l.add("scheme");
		return l;
	}

	public List<String> getMimeTypes() {
		return Collections.emptyList();
	}

	public String getMethodCallSyntax(String arg0, String arg1, String... arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLanguageVersion() {
		return "1998";
	}

	public String getLanguageName() {
		return "JScheme 1998";
	}

	public List<String> getExtensions() {
		List<String> l = new ArrayList<String>();
		l.add("scm");
		return l;
	}

	public String getEngineVersion() {
		return "maven version here";
	}

	public String getEngineName() {
		return SchemeScriptEngine.class.getCanonicalName();
	}
}