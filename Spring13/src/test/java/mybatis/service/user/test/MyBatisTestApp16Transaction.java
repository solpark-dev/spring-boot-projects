package mybatis.service.user.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import spring.domain.User;
import spring.service.user.UserService;

/*
 * FileName : MyBatisTestApp16Transaction.java
  * :: Business Layer unit Test : Service + Persistence (Spring +mybatis + DAO)
  * :: Annotation 기반 
  * :: AOP + Spring Transaction 
  */
public class MyBatisTestApp16Transaction {
	
	///main method
	public static void main(String[] args){

		ApplicationContext context =
				new ClassPathXmlApplicationContext(
																	new String[] {	"/config/commonservice.xml"	 }
									                                                    );
		System.out.println("\n");

		//==> Bean/IoC Container 로 부터 획득한 UserService 인스턴스 획득
		UserService userService = (UserService)context.getBean("userService");
		
		System.out.println("\n");
		
		//==> Test 용 User instance 생성  
		User user = new User("user04","주몽","user04",null,0);
		
		try{
			System.out.println("\n::::::::::::::::::: Test 결과확인.... start  :::::::::::::::::::::::::::");
			System.out.println(":: 회원가입 요청함....");
			userService.addUser(user);
			System.out.println(":: 회원가입 종료함....");
			System.out.println("::::::::::::::::::::: Test 결과확인.... end  ::::::::::::::::::::::::::::");
		}catch(Exception e){
			e.printStackTrace();
		}
	}//end of main
}//end of class