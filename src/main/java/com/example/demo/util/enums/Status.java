package com.example.demo.util.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

	ENABLE(1, "启用"), DISABLE(0, "禁用");

	@EnumValue
	private Integer userStatus;
	@JsonValue
	private String name;

}
