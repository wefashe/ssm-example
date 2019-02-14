package com.example.demo.controller;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.SysUser;
import com.example.demo.util.enums.MyExceptionEnums;
import com.example.demo.util.exception.MyException;
import com.example.demo.util.result.Result;
import com.example.demo.util.shiro.TokenManager;

@Controller
@Validated
public class SysIndexController {

	@GetMapping("/")
	public String redirectIndex() {
		return "redirect:index";
	}

	@GetMapping("index")
	public String index(Model model) {
		SysUser user = TokenManager.getToken();
		model.addAttribute("user", user);
		return "index";
	}

	@GetMapping("login")
	public String login() {
		return "login";
	}

	@PostMapping("login")
	@ResponseBody
	public Result login(@NotBlank(message = "name 不能为空")
    @Length(min = 2, max = 3, message = "name 长度必须在 {min} - {max} 之间") String userName, String passWord, String captcha, Boolean rememberMe) {
		if (StringUtils.isBlank(userName)) {
			throw new MyException(MyExceptionEnums.USERNAME_EMPTY);
		} else if (StringUtils.isBlank(passWord)) {
			throw new MyException(MyExceptionEnums.PASSWORD_EMPTY);
		} else if (StringUtils.isBlank(captcha)) {
			throw new MyException(MyExceptionEnums.CAPTCHA_EMPTY);
		}
		try {
			SysUser user = TokenManager.login(userName, passWord, rememberMe);
			return new Result(user);
		} catch (UnknownAccountException elogin) {
			throw new MyException(MyExceptionEnums.USER_NOT_EXISTS);
		} catch (DisabledAccountException e) {
			throw new MyException(MyExceptionEnums.USER_STOP_USING);
		} catch (IncorrectCredentialsException e) {
			throw new MyException(MyExceptionEnums.PASSWORD_ERROR);
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
