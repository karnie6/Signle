package com.signle.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;

public class SignleExceptionHandler extends DefaultExceptionHandler {

	public Resolution handleGeneric(Exception e, HttpServletRequest request, HttpServletResponse response) {
        // general exception handling
		e.printStackTrace();
        return new ErrorResolution(503, "Error occurred");
    }
	
}
