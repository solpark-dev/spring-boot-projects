package com.model2.mvc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Welcome Page  : 여러가지 방법중 Controller 로 만듬.
 */
//======================== 추가, 변경된 부분  ==========================/
//==> Spring Boot 시 추가된 부분.  //==> No Meta Data ==> No XML (web.xml)
//======================== 추가, 변경된 부분  ==========================/
@Controller
public class MainController {
	
	///Constructor
	public MainController(){
		System.out.println("==> MainController default Constructor call.............");
	}

	@GetMapping("/")
	public String index() {
		
		System.out.println("[ MainController.index() start........]");//<== 디버깅용
		//==> web.xml : <welcome-file>~~~.html or ~~.jsp</welcome-fle> : 물리적인 파일이어야 함.
		return "/index.jsp";
		
	}
}
