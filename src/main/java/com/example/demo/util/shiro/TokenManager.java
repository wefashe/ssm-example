package com.example.demo.util.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.example.demo.entity.SysUser;

public class TokenManager {

  public static SysUser getToken() {
    SysUser token = (SysUser) SecurityUtils.getSubject().getPrincipal();
    return token;
  }

  public static SysUser login(String userName, String passWord, Boolean rememberMe) {
    ShiroToken token = new ShiroToken(userName, passWord);
    token.setRememberMe(rememberMe);
    Subject user = SecurityUtils.getSubject();
    user.login(token);
    return getToken();
  }

  //  DisabledAccountException （禁用的帐号）
  //  LockedAccountException （锁定的帐号）
  //  UnknownAccountException（错误的帐号）
  //  ExcessiveAttemptsException（登录失败次数过多）
  //  IncorrectCredentialsException （错误的凭证）
  //  ExpiredCredentialsException （过期的凭证）

}