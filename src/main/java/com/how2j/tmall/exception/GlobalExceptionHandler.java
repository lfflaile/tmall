package com.how2j.tmall.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
	public String defaultErrorHandler(HttpServletRequest req,Exception e) throws Exception{
		e.printStackTrace();
		Class constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");
		if(null!=e.getCause() && constraintViolationException==e.getCause().getClass()){
			return "Υ����Լ��,��������Լ��";
		}
		return e.getMessage();
	}
}
