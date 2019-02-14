package com.example.demo.util.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.util.enums.MyExceptionEnums;
import com.example.demo.util.result.Result;

@ControllerAdvice
public class GlobalDefaultExceptionHandler  {

	// 声明要捕获的异常
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public <T> Result defultExcepitonHandler(Throwable e) {
		if(e instanceof MyException) {
			return new Result(e);
		}
		else if(e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
			return new Result(MyExceptionEnums.REQUEST_METHOD_NO_FOUND);
		}
		return  new Result(e);
	}

}
