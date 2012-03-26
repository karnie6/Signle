package com.signle.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/signle/test")
public class TestActionBean extends SignleActionBean {
	
	@DefaultHandler
	public Resolution test() {
		return new StreamingResolution("text/html", "hello world!");
		
	}
}
