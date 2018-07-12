package com.common.security;

import com.pojo.User;
import com.service.IUserSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private IUserSvc userSvcImpl;
    private String defaultSuccessUrl;

    private final static Logger logger = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        RequestCache requestCache = new HttpSessionRequestCache();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = null;
        try {

            user = userSvcImpl.getUserByUserName(userDetails.getUsername());
            request.getSession().setAttribute("username",user.getUserName());
            request.getSession().setAttribute("userId",user.getUserId());
            logger.info("myUserDetailsService.loadUserByUsername","认证模块","低",
                    "登录","成功","邮箱为" + user.getUserName() + "的用户登录成功，登录IP为" + request.getRemoteAddr(),user.getUserId());
        }catch (Exception e){
            logger.error("MyAuthenticationSuccessHandler.onAuthenticationSuccess","认证模块","高","登录","失败","保存session失败,mail为" + user.getUserName(),user.getUserId());
        }
        String url = null;
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if(savedRequest != null){
            url = savedRequest.getRedirectUrl();
            getRedirectStrategy().sendRedirect(request,response,url);

        }
        if(url == null){
            getRedirectStrategy().sendRedirect(request,response,defaultSuccessUrl);
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public IUserSvc getUserSvcImpl() {
        return userSvcImpl;
    }

    public void setUserSvcImpl(IUserSvc userSvcImpl) {
        this.userSvcImpl = userSvcImpl;
    }

    public String getDefaultSuccessUrl() {
        return defaultSuccessUrl;
    }

    public void setDefaultSuccessUrl(String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
    }

    public static Logger getLogger() {
        return logger;
    }
}