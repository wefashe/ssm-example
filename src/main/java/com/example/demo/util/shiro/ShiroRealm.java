package com.example.demo.util.shiro;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dao.ISysPermDao;
import com.example.demo.dao.ISysRoleDao;
import com.example.demo.dao.ISysUserDao;
import com.example.demo.entity.SysPerm;
import com.example.demo.entity.SysRole;
import com.example.demo.entity.SysUser;
import com.example.demo.util.enums.Status;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

  @Autowired
  ISysUserDao userDao;

  @Autowired
  ISysPermDao permDao;

  @Autowired
  ISysRoleDao roleDao;

  /**
   *  shiro登录认证
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    log.info("{}开始登录认证", authcToken.getPrincipal());
    // 获取用户输入信息
    ShiroToken token = (ShiroToken) authcToken;
    LambdaQueryWrapper<SysUser> lambda = new LambdaQueryWrapper<>();
    lambda.eq(SysUser::getUserName, token.getUsername());
    SysUser user = userDao.selectOne(lambda);
    //账户不存在
    if (null == user) {
      throw new UnknownAccountException();
    }
    //账户停用
    if (Status.DISABLE.equals(user.getUserStatus())) {
      throw new DisabledAccountException();
    }
    //更新登录时间
    userDao.updateById(new SysUser().setUserId(user.getUserId()).setLastLoginTime(System.currentTimeMillis() + ""));
    log.info("{}登录认证通过", authcToken.getPrincipal());
    //盐值
    ByteSource credentialsSalt = ByteSource.Util.bytes(user.getPassSalt());
    return new SimpleAuthenticationInfo(user, user.getUserPass(), credentialsSalt, getName());
  }

  /** 
    * shiro权限认证
    */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    SysUser user = (SysUser) principals.getPrimaryPrincipal();
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    // 获取用户角色集
    List<SysRole> roles = roleDao.findRoleIdByUserId(user.getUserId());
    Set<String> roleSet = roles.stream().map(SysRole::getRoleId).collect(Collectors.toSet());
    simpleAuthorizationInfo.setRoles(roleSet);

    //获取用户权限集
    List<SysPerm> perms = permDao.fingPermByUserId(user.getUserId());
    Set<String> permSet = perms.stream().map(SysPerm::getPermId).collect(Collectors.toSet());

    simpleAuthorizationInfo.setStringPermissions(permSet);
    return simpleAuthorizationInfo;
  }

  /**
   * 指定principalCollection 清除
   */
  @Override
  public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
    super.clearCachedAuthorizationInfo(principals);
  }

  /**
   * 清空当前用户权限信息
   */
  public void clearCachedAuthorizationInfo() {
    PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
    SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
    super.clearCachedAuthorizationInfo(principals);
  }

}
