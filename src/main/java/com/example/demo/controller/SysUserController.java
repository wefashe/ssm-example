package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.SysUser;
import com.example.demo.service.ISysUserService;
import com.example.demo.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wenfs
 * @since 2019-02-13
 */
@Api(tags="用户接口")
@Controller
@RequestMapping("user")
public class SysUserController {

	@Autowired
	private ISysUserService userService;
	
	@GetMapping("register")
	public String register() {
		return null;
	}

	@ApiOperation(value="添加用户value")
	@PostMapping("register")
	public Result register( SysUser user) {

		LambdaQueryWrapper<SysUser> lambda = new LambdaQueryWrapper();
		lambda.eq(SysUser::getUserName, user.getUserName());
		SysUser existUser = userService.getOne(lambda);

		//账户不存在
		if (null == existUser){
			ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUserId());
			/*
			 * MD5加密：
			 * 使用SimpleHash类对原始密码进行加密。
			 * 第一个参数代表使用MD5方式加密
			 * 第二个参数为原始密码
			 * 第三个参数为盐值，即用户ID
			 * 第四个参数为加密次数
			 * 最后用toHex()方法将加密后的密码转成String
			 * */
			String newPass = new SimpleHash("MD5", user.getUserPass(), credentialsSalt, 1024).toHex();
			user.setUserPass(newPass);
			userService.save(user);
		}
		return new Result();
	}
	


}

