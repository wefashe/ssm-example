package com.example.demo.controller;

import com.baomidou.kaptcha.Kaptcha;
import com.example.demo.entity.SysUser;
import com.example.demo.service.ISysPermService;
import com.example.demo.util.result.Result;
import com.example.demo.util.shiro.TokenManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SysIndexController extends BaseController{

	@Autowired
	private Kaptcha kaptcha;
	@Autowired
	private ISysPermService permService;

	@GetMapping("/")
	public String index() {
		return "redirect:index";
	}

	@GetMapping("index")
	public String index(Model model) {
		SysUser user = TokenManager.getToken();
		model.addAttribute("user", user);
		model.addAttribute("menus",permService.getPerm(user.getUserId(),null,1));
		return "index";
	}

	@GetMapping("login")
	public String login() {
		return "login";
	}


	@GetMapping("/render")
	@ResponseBody
	public void render() {
		kaptcha.render();
	}


	@PostMapping("login")
	@ResponseBody
	public Result login(String userName, String passWord, String captcha, boolean rememberMe) {
    kaptcha.validate(captcha, 60);
    SysUser user = TokenManager.login(userName, passWord,rememberMe);
    return new Result(user);
	}

}
