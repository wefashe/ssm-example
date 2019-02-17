package com.example.demo.controller;


import com.example.demo.entity.SysUser;
import com.example.demo.service.ISysPermService;
import com.example.demo.util.result.Result;
import com.example.demo.util.shiro.TokenManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author wenfs
 * @since 2019-02-13
 */
@Api("权限接口")
@Controller
@RequestMapping("perm")
public class SysPermController {

  @Autowired
  private ISysPermService permService;

  @GetMapping("/")
  public String index(Model model) {
    SysUser user = TokenManager.getToken();
    model.addAttribute("perms",permService.getPerm(user.getUserId(),null,null));
    return "system/perm/perm";
  }

  @GetMapping("getPerm")
  @ResponseBody
  public Result list(String userId,String permName,Integer permType){
    return new Result(permService.getPerm(userId,permName,permType));
  }

}

