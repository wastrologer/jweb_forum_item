package com.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.common.utils.UserInfoUtil;
import com.service.IUserSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 前端用户登录
 * 
 * @author Admin
 */
public class RoleInterceptor extends HandlerInterceptorAdapter {

	static final Logger logger = LoggerFactory.getLogger(RoleInterceptor.class);

	
	@Autowired
	private IUserSvc userSvcImpl;
	
	@Autowired
	protected UserInfoUtil userInfoUtil;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (handler instanceof HandlerMethod) {
	        HandlerMethod hm = (HandlerMethod) handler;
	        Object target = hm.getBean();
	        Class<?> clazz = hm.getBeanType();
	        Method m = hm.getMethod();
	        if (clazz != null && m != null) {

			}
	    }
		return true;
	}
		
	private void writeResponse(HttpServletResponse response,int errorCode,String msg) throws Exception{
		JSONObject responseJSONObject = new JSONObject();
		responseJSONObject.put("errorCode", errorCode);
		responseJSONObject.put("data", null);
		responseJSONObject.put("msg", msg);
		responseJson(response, responseJSONObject);
	}
	
	private void responseJson(HttpServletResponse response, JSONObject responseJSONObject) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(responseJSONObject.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
