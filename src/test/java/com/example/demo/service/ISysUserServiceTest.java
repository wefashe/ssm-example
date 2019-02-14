package com.example.demo.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dao.ISysUserDao;
import com.example.demo.entity.SysUser;
import com.example.demo.util.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ISysUserServiceTest {

	@Autowired
	private ISysUserService SysUserService;
	
	@Autowired
	private ISysUserDao userDao; 

	@Test
	public void test() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		LambdaQueryWrapper<SysUser> lambda = new LambdaQueryWrapper();
		lambda.eq(SysUser::getUserStatus, Status.ENABLE);
		lambda.orderByAsc(SysUser::getCreateTime);
		List<SysUser> menus = SysUserService.list(lambda);
		menus.get(0).toString();

	}

	@Test
	public void test2() {
		LambdaQueryWrapper<SysUser> lambda = new LambdaQueryWrapper<>();
		lambda.eq(SysUser::getUserName, "admin");
		SysUser user = userDao.selectOne(lambda);
		System.out.println(user.toString());
	}

}
