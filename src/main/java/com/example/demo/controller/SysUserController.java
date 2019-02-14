package com.example.demo.controller;


import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.SysUser;
import com.example.demo.util.result.Result;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wenfs
 * @since 2019-02-13
 */
@Controller
@RequestMapping("sysUser")
public class SysUserController {
	
	@GetMapping("register")
	public String register() {
		return null;
	}
	
	@PostMapping("register")
	public Result register(@Valid SysUser user) {
		return null;
	}
	


}

