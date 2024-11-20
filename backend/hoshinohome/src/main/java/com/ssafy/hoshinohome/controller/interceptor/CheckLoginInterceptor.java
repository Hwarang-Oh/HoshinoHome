package com.ssafy.hoshinohome.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckLoginInterceptor implements HandlerInterceptor {

	// private static Logger log =
	// LoggerFactory.getLogger(CheckLoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute("userId") == null) {
			log.info("로그인 안된 상태");
			response.sendRedirect(request.getContextPath() + "/user/loginForm");
			return false;
		} else {
			log.info("로그인 유지");
		}
		return true;
	}

}
