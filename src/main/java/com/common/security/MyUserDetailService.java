package com.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.service.IUserSvc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

public class MyUserDetailService implements UserDetailsService {
    private static final Logger logger = LogManager.getLogger(MyUserDetailService.class);
    @Resource
    private IUserSvc userSvcImpl;


    /**
     * 根据用户名获取用户 - 用户的角色、权限等信息
     */
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails userDetails = null;
        try {
            com.pojo.User user = userSvcImpl.getUserByUserName(1,username);
            List<GrantedAuthority> authList= getAuthorities(user);
            String password;
            if(user==null){
                password=null;
            }else {
                password=user.getPassword();
            }
            userDetails = new User(username, password,true,true,true,true,authList);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return userDetails;
    }

    /**
     * 获取用户的角色权限,
     * @param
     * @return
     */
    private List<GrantedAuthority> getAuthorities(com.pojo.User user){
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        String id;
        if(user!=null&&user.getUserId()!=null){
            id=user.getAuthId();
            String[] ids=id.split(";");
            for(String auth:ids){
                authList.add(new SimpleGrantedAuthority(auth));
            }
            //authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        }else{
            authList.add(null);
        }
        return authList;
    }
}
