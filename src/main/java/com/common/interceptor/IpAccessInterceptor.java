package com.common.interceptor;


import com.alibaba.fastjson.JSONObject;

import com.common.cache.CacheClient;
import com.common.entity.Constants;
import com.common.utils.HelperUtil;
import com.common.utils.StringUtil;
import com.constant.ErrorCode;
import com.pojo.ImageCodeCount;
import net.rubyeye.xmemcached.GetsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class IpAccessInterceptor extends HandlerInterceptorAdapter{
	
	static final Logger logger = LoggerFactory.getLogger(IpAccessInterceptor.class);
	
	@Autowired
	private CacheClient cacheClient;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getServletPath();
		String ip  = getIpAddr(request);
		String key = requestURI + "#" + ip;
		if (isNeedLoginUrl(requestURI)) {
			printParams(request);
			//判断是否达到访问上限 --如果是不让该ip访问
			GetsResponse<Object> smsCountVoGetsResponse = cacheClient.gets(key);
			ImageCodeCount ImageCodeCount;
			if (smsCountVoGetsResponse == null) {
				ImageCodeCount = new ImageCodeCount();
				ImageCodeCount.setCount(1);
				ImageCodeCount.setTimestamp(HelperUtil.getSecondStamp());
				cacheClient.set(key, Constants.IMAGE_CODE_COUNT_EXPIRE_SECOND, ImageCodeCount);
			}else{
				ImageCodeCount = (ImageCodeCount) smsCountVoGetsResponse.getValue();
				if (ImageCodeCount.getCount() < 50000) {
					ImageCodeCount.setCount(ImageCodeCount.getCount() + 1);
					cacheClient.cas(key,
							(int) ((24 * 3600) - (HelperUtil.getSecondStamp() - ImageCodeCount.getTimestamp())),
							ImageCodeCount, smsCountVoGetsResponse.getCas());
				}else{
					printParams(request);
					logger.info("访问已达上限：" + key);
					JSONObject responseJSONObject = new JSONObject();
					responseJSONObject.put("errorCode", ErrorCode.ACCESS_HAS_MAX);
					responseJSONObject.put("data", null);
					responseJSONObject.put("msg", "");
					responseJson(response, responseJSONObject);
			        return false;
				}
			}
		}
		return true;
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
	
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		logger.info("x-forwarded-for, ip=" + ip);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.info("Proxy-Client-IP, ip=" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			logger.info("WL-Proxy-Client-IP, ip=" + ip);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			logger.info("getRemoteAddr, ip=" + ip);
		}
		return ip;
	}
	
	// 打印请求参数（包括手机类型）
	private void printParams(HttpServletRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		String queryString = "";
		for (String key : params.keySet()) {
			if ("utoken".equals(key)) {
				continue;
			}
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				if (value.length() > 50) {
					// 如果参数太长了，搞短一点再打印
					value = value.substring(0, 50) + "......";
				}
				queryString += key + "=" + value + "&";
			}
		}
		logger.info(request.getServletPath() + "?" + queryString + "&userAgent="
				+ StringUtil.trim(request.getHeader("User-Agent")));
		
	}
	
	private boolean isNeedLoginUrl(String requestURI) {
		boolean flag = false;
		
		if (requestURI.endsWith("/browseShop/userGetWorkerToMinister")) {
			flag = true;
		}

		return flag;
	}
}
