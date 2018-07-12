package com.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class TokenUtil {
	
	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String generalToken(String userName,String random, long currTime){
		String t = userName+":"+random+":"+currTime;
		return Base64.encodeBase64URLSafeString(t.getBytes()).toString();
	}
	
	public static String generateString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));  
        }  
        return sb.toString();  
    }
    public static void writeCookie(String userName, HttpServletRequest request, HttpServletResponse response, String u_token){
        //把utoken写入cookie
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();

        String domain = request.getServerName();

        if (domain.equalsIgnoreCase("localhost")) {
            domain = "";
        } else {
            boolean isIP = domain.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
            if (!isIP) {
                String[] ds = domain.split("\\.");
                if (ds.length > 2) {
                    domain = domain.substring(domain.indexOf("."));
                }
            }
        }

        Date expdate = new Date();
        expdate.setTime(expdate.getTime() + (60 * 60 * 12 * 1000));
        DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.ROOT);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String cookieExpire = "Expires=" + df.format(expdate) + ";";

        StringBuilder tokenCookie = new StringBuilder();
        tokenCookie.append("utoken=").append(u_token).append("; ");
        tokenCookie.append("Path=/; ").append("Domain=").append(domain).append(";");
        tokenCookie.append(cookieExpire);
        tokenCookie.append("HttpOnly; ");

        StringBuilder userCookie = new StringBuilder();
        userCookie.append("userName=").append(userName).append("; ");
        userCookie.append("Path=/; ").append("Domain=").append(domain).append(";");
        userCookie.append(cookieExpire);
        userCookie.append("HttpOnly; ");

        response.addHeader("Set-Cookie", userCookie.toString());
        response.addHeader("Set-Cookie", tokenCookie.toString());

    }

    public static void writeCookie(String userName, String token, HttpServletRequest request, HttpServletResponse response) {
        Cookie c1 = new Cookie("userName", userName);
        c1.setMaxAge(3600);
        Cookie c2 = new Cookie("utoken", token);
        c2.setMaxAge(3600);

        String domain = request.getServerName();
        if (domain.equalsIgnoreCase("localhost")) {
            domain="";
        } else {
            boolean isIP = domain.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
            if (!isIP) {
                String[] ds = domain.split("\\.");
                if (ds.length > 2) {
                    domain = domain.substring(domain.indexOf("."));
                }
            }
            c1.setDomain(domain);
            c2.setDomain(domain);
        }

        response.addCookie(c1);
        response.addCookie(c2);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie1 : cookies) {
            System.out.println("cookie名称：" + cookie1.getName() + "       值：  " + cookie1.getValue());
        }
    }
}
