package com.project.mainPage.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("logincheck testing");
		String prevPage = request.getHeader("Referer");
		HttpSession session = request.getSession();
		Object loginCheck_obj=session.getAttribute("loginUser");
		System.out.println(session);
		System.out.println(loginCheck_obj);
		if(loginCheck_obj!=null) {
			return true;
		}else {
			session.setAttribute("redirectPage", prevPage);
			response.sendRedirect("/user/login.do");
			return false;
		}
		
	}
}
