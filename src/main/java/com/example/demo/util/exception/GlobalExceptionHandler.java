package com.example.demo.util.exception;

import com.baomidou.kaptcha.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.util.enums.MyExceptionEnums;
import com.example.demo.util.result.Result;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler  {


	@ExceptionHandler(MyException.class)
	@ResponseBody
	public  Result myExcepitonHandler(MyException e) {
		return  new Result(e);
	}

	@ExceptionHandler(ShiroException.class)
	@ResponseBody
	public  Result shiroExcepitonHandler(ShiroException shiroException) {
		if(shiroException instanceof UnknownAccountException){
			return new Result(MyExceptionEnums.USER_NOT_EXISTS);
		}else if (shiroException instanceof DisabledAccountException){
			return new Result(MyExceptionEnums.USER_STOP_USING);
		}else if (shiroException instanceof IncorrectCredentialsException){
			return new Result(MyExceptionEnums.PASSWORD_ERROR);
		}
		return  new Result(shiroException);
	}




	@ExceptionHandler(KaptchaException.class)
	@ResponseBody
	public  Result kaptchaExcepitonHandler(KaptchaException kaptchaException) {
		if(kaptchaException instanceof KaptchaNotFoundException){
			return new Result(MyExceptionEnums.KAPTCHA_NOTFOUND);
		}else if (kaptchaException instanceof KaptchaRenderException){
			return new Result(MyExceptionEnums.KAPTCHA_RENDERFAIL);
		}else if (kaptchaException instanceof KaptchaIncorrectException){
			return new Result(MyExceptionEnums.KAPTCHA_INCORRECT);
		}else if (kaptchaException instanceof KaptchaTimeoutException){
			return new Result(MyExceptionEnums.KAPTCHA_TIMEOUT);
		}
		return  new Result(kaptchaException);
	}


	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public  Result validationExceptionHandler(ValidationException validationException) {
		return  new Result(validationException);
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public  Result servletExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {

		BindingResult result = methodArgumentNotValidException.getBindingResult();
		if (result.hasErrors()) {

			List<ObjectError> errors = result.getAllErrors();

			errors.forEach(p ->{

				FieldError fieldError = (FieldError) p;
				log.error("Data check failure : object{"+fieldError.getObjectName()+"},field{"+fieldError.getField()+
					"},errorMessage{"+fieldError.getDefaultMessage()+"}");

			});

		}



		return  new Result(methodArgumentNotValidException);
	}


	@ExceptionHandler(value =BindException.class)
	@ResponseBody
	public Result handleBindException(BindException e) throws BindException {
		// ex.getFieldError():随机返回一个对象属性的异常信息。如果要一次性返回所有对象属性异常信息，则调用ex.getAllErrors()
		FieldError fieldError = e.getFieldError();
		StringBuilder sb = new StringBuilder();
		sb.append(fieldError.getField()).append("=[").append(fieldError.getRejectedValue()).append("]")
			.append(fieldError.getDefaultMessage());
		return new Result();
	}


	@ExceptionHandler(ServletException.class)
	@ResponseBody
	public  Result servletExceptionHandler(ServletException ServletException) {
		if(ServletException instanceof NoHandlerFoundException) {
			return new Result(MyExceptionEnums.REQUEST_METHOD_NO_FOUND);
		}
		return  new Result(ServletException);
	}




	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Result runtimeExcepitonHandler(RuntimeException runtimeException) {
		return  new Result(runtimeException);
	}

}