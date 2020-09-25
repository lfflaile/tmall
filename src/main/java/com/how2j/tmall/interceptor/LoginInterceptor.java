package com.how2j.tmall.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.how2j.tmall.pojo.User;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o) throws Exception {
		HttpSession session = httpServletRequest.getSession();
		String contextPath = session.getServletContext().getContextPath();
		String[] requireAuthPages = new String[]{
				"buy",
				"alipay",
				"payed",
				"cart",
				"bought",
				"confirmPay",
				"orderConfirmed",
				
				"forebuyone",
				"forebuy",
				"foreaddCart",
				"forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
		};
		String uri = httpServletRequest.getRequestURI();
		uri = StringUtils.remove(uri,contextPath+"/");
		String page = uri;
		if(begingWith(page,requireAuthPages)){
			User user = (User)session.getAttribute("user");
			if(user==null){
				httpServletResponse.sendRedirect("login");
				return false;
			}
		}
		return true;
	}
	
	private boolean begingWith(String page,String[] requireAuthPages){
		boolean result = false;
		for(String requireAuthPage:requireAuthPages){
			if(StringUtils.startsWith(page,requireAuthPage)){
				result = true;
				break;
			}
		}
		return result;
	}
	

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
