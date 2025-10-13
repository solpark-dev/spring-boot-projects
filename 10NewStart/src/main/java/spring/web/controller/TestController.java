package spring.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.web.domain.User;

@RestController
@RequestMapping("/test")
public class TestController {

	///Field

	///Constructor
	public TestController() {
		System.out.println("==> " + this.getClass().getName());
		System.out.println("==> " + this.getClass().getSimpleName());
	}
	
	///Method
	@GetMapping("/hello")
	public String hello() {
		System.out.println("==> " + this.getClass().getName());
		
		return "==>" + this.getClass().getMethods()[0];
	}
	
	@GetMapping("/getUser")
	public User getUser() {
	
		System.out.println("==> " + this.getClass().getName());
		//User user = new User("user01", "user01", 18);
		
		User user = User.builder()
				.id("user01")
				.pwd("user01")
				//.age(18)
				.build();
		return user;
	}
	
}
