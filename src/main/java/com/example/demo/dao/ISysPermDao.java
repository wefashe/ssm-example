package com.example.demo.dao;

import com.example.demo.entity.SysPerm;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author wenfs
 * @since 2019-02-13
 */
public interface ISysPermDao extends BaseMapper<SysPerm> {

	List<SysPerm> fingPermByUserId(String userId);

}
