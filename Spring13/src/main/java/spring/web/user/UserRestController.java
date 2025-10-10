package spring.web.user;

import java.util.HashMap;
import java.util.Map;
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
    
    ///Constructor
    public UserRestController(){
        System.out.println(":: UserRestController default Constructor call");
    }
    
    //==> http://127.0.0.1:8080/Spring13/app/userAPI/getUser?name=홍길동&age=10
    //( O ) ==> Client 로 Domain Object 인 JSON 전송은 확인
    @RequestMapping( value="getUser", method=RequestMethod.GET )
    public User getUser( @RequestParam("name") String name,
                         @RequestParam("age") int age) throws Exception{
        
        System.out.println();
        System.out.println("[ Client 에서 받은 Data 확인 ]");
        System.out.println("name : "+name);
        System.out.println("age : "+age);
        
        User user = new User();
        user.setUserId("AAA");
        user.setUserName("GET:이순신");
        user.setAge(100);
        
        System.out.println("[ Server 에서 Client 로 전송하는 Data 확인 ]");
        System.out.println(user);
        
        return user;
    }

    //==> http://127.0.0.1:8080/Spring13/user/api/getUser/user01?name=홍길동&age=10
    //( O ) ==> Client 로 Domain Object + 기타 Data 를 JSON 으로 전송할 경우.
    @RequestMapping( value="getUserMore/{value}", method=RequestMethod.GET )
    public Map<String,Object> getUser( @PathVariable String value,
                                       @RequestParam("name") String name,
                                       @RequestParam("age") int age) throws Exception{
        
        System.out.println();
        System.out.println("[ Client 에서 받은 Data 확인 ]");
        System.out.println("value : "+value);
        System.out.println("name : "+name);
        System.out.println("age : "+age);
        
        User user = new User();
        user.setUserId("AAA");
        user.setUserName("GET:"+name);
        user.setAge(age);
        System.out.println("[ Server 에서 Client 로 전송하는 Data 확인 ]");
        System.out.println(user);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user",user);
        //==> 단순희 name=value 의 Data 를 저장할 경우는 ????
        map.put("message","ok");

        return map;
    }
    
    //( O ) ==> http://192.168.0.29:8080/Spring13/user/api/getUserPost
    // Client 로 Domain Object 인 JSON 전송할 경우
    @RequestMapping( value="getUserPost", method=RequestMethod.POST )
    public User getUser( @RequestBody User user ) throws Exception{
        
        System.out.println();
        System.out.println("[ From Client Data ]");
        System.out.println("1. : "+user);
        System.out.println("2. : "+user);

        System.out.println("[To Client Data]");
        user = new User();
        user.setUserId("AAA");
        user.setUserName("POST:이순신");
        System.out.println("1 : "+user);
        System.out.println("2 : "+user);
        
        return user;
    }
    
    //( O ) ==> http://192.168.0.29:8080/Spring13/user/api/getUserMorePost/user01
    // Client 로 Domain Object + 기타 Data 를 JSON 으로 전송할 경우.
    @RequestMapping( value="getUserMorePost/{value}", method=RequestMethod.POST )
    public Map<String,Object> getUserMore( @PathVariable String value,
                                           @RequestBody User user ) throws Exception{
        
        System.out.println();
        System.out.println("[ From Client Data ]");
        System.out.println("1. : "+user);
        System.out.println("2. : "+user);
        
        System.out.println("[To Client Data]");
        user = new User();
        user.setUserId("AAA");
        user.setUserName("POST:이순신");
        System.out.println("1 : "+user);
        System.out.println("2 : "+user);
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user",user);

        //==> 단순희 name=value 의 Data 를 저장할 경우는 ????
        map.put("message","ok");

        return map;
    }
}