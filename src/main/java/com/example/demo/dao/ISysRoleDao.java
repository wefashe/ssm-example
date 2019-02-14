package com.example.demo.dao;

import com.example.demo.entity.SysRole;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author wenfs
 * @since 2019-02-13
 */
public interface ISysRoleDao extends BaseMapper<SysRole> {

	List<SysRole> findRoleIdByUserId(String userId);

}
