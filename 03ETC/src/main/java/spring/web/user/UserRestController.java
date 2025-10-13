package spring.web.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.domain.User;

@RestController
@RequestMapping("/userAPI/*")
public class UserRestController {
	///Field
	///Constructor
	public UserRestController(){
		System.out.println(":: UserRestController default Contructor call");
	}
	
	
	//1.1 Client 로 Domain Object 만 전송할 경우.
	//요청 미디어타입 : application/json ==> 즉 데이터교환 json 
	//http://127.0.0.1:8080/Spring13/app/userAPI/getUser?name=user02&age=10
	@RequestMapping(value="getUser" , method=RequestMethod.GET )
	public User getUser	(	@RequestParam("name") String name,
												@RequestParam("age") int age) throws Exception{
	
		System.out.println();
		System.out.println(name);
		System.out.println(age);
		
		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("GET:이순신");
		returnUser.setAge(100);
		System.out.println(returnUser);
		
		return returnUser;
	}

	// 1.1 Client 로 Domain Object + 기타 Data 를 JSON 으로 전송할 경우.
	//요청 미디어타입 : application/json ==> 즉 데이터교환 json 
	//http://127.0.0.1:8080/Spring13/app/userAPI/getUserMore/user01?name=user02&age=10
	@RequestMapping(value="getUserMore/{value}" , method=RequestMethod.GET )
	public Map getUserMore	(	@PathVariable String value,
														@RequestParam("name") String name,
														@RequestParam("age") int age) throws Exception{
		System.out.println();
		System.out.println(value);
		System.out.println(name);
		System.out.println(age);
		
		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("GET:이순신");
		returnUser.setAge(100);
		System.out.println(returnUser);
		
		Map map = new HashMap();
		map.put("user",returnUser);
		//==> 단순히 name=value 의 Data 를 저장할 경우는 ????
		map.put("message","ok");

		return map;
	}

	// 2.1 Client 로 Domain Object 만 전송할 경우.
	//http://192.168.0.29:8080/Spring13/userAPI/getUser
	// @ModelAttribute("user") User user : Client 의 Form Data => User 바인딩. 
	// @RequestBody User user : Client JSON Datga => User 바인딩
	@RequestMapping(value="getUser" , method=RequestMethod.POST )
	public User getUser	( @RequestBody User user ) throws Exception{
		System.out.println();
		System.out.println("[ Client JSON Data ]");
		System.out.println("1 : "+user);
		
		System.out.println("[To Client Data]");
		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("POST:이순신");
		System.out.println("2 : "+returnUser);
		
		return returnUser;
	}
	
	
	
	// 2.1 Client 로 Domain Object + 기타 Data 를 JSON 으로 전송할 경우.
	//http://192.168.0.29:8080/Spring13/userAPI/getUserMore/user01
	// @ModelAttribute("user") User user : Client 의 Form Data => User 바인딩. 
	// @RequestBody User user : Client JSON Datga => User 바인딩	
	@RequestMapping(value="getUserMore/{value}" , method=RequestMethod.POST )
	public Map getUserMore	(  	@PathVariable String value, 
														@RequestBody User user ) throws Exception{	
		System.out.println();
		System.out.println("[ Client JSON Data ]");
		System.out.println(value);
		System.out.println("1 : "+user);
		
		System.out.println("[To Client Data]");
		User returnUser = new User();
		returnUser.setUserId("AAA");
		returnUser.setUserName("POST:이순신");
		System.out.println("2 : "+returnUser);
		
		Map map = new HashMap();
		map.put("user",returnUser);
		//==> 단순히 name=value 의 Data 를 저장할 경우는 ????
		map.put("message","ok");

		return map;
	}	
}