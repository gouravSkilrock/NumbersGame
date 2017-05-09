package com.skilrock.lms.common;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.util.ValueStack;

public class MyTextProvider implements TextProvider, LocaleProvider {
	
	private final transient TextProvider textProvider = new TextProviderFactory().createInstance(getClass(), this);
	
	@Override
	public Locale getLocale() {
		ActionContext ctx = ActionContext.getContext();
		if (ctx != null) {
			return ctx.getLocale();
		} else {
			return null;
		}
	}

	@Override
	public String getText(String arg0, List arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String arg1, List arg2, ValueStack arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String arg1, List arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String arg1, String[] arg2,
			ValueStack arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(String text) {
		return textProvider.getText(text);
	}

	@Override
	public ResourceBundle getTexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceBundle getTexts(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
