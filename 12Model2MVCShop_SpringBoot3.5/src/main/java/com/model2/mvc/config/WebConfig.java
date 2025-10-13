package com.model2.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.model2.mvc.common.web.LogonCheckInterceptor;

//======================== 추가, 변경된 부분  ==========================/
//==> Spring Boot 시 추가된 부분.  //==> No Meta Data ==> No XML
//======================== 추가, 변경된 부분  ==========================/
@Configuration
public class WebConfig implements WebMvcConfigurer {

	public WebConfig() {
		System.out.println("==> WebConfig default Constructor call.............");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		// URL Pattern 을 확인하고. interceptor 적용유무 등록함.
		registry.addInterceptor( new LogonCheckInterceptor()).addPathPatterns("/user/*");
		
	}

}
