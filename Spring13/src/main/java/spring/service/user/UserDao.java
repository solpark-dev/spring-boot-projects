package spring.service.user;

import java.util.List;

import spring.domain.Search;
import spring.domain.User;

/* 
 * FileName : UserDao.java  ( Data Access Object ) 
 * ㅇ 데이터베이스와 직접적인 통신을 담당하는 퍼시스턴스 계층을 담당할 인터페이스
*/ 
public interface UserDao{
	
	//==> 회원정보 ::  INSERT ( 회원가입 )
	public int addUser(User user) throws Exception;
	
	//==> 회원정보 ::  SELECT  ( 회원정보 검색 ) 
	public User getUser(String userId) throws Exception ;

	//==> 회원정보 ::  UPDATE  ( 회원정보 변경  )
	public int updateUser(User user) throws Exception ;
	
	//==> 회원정보 ::  DELETE  ( 회원정보 삭제 )
	public int removeUser(String userId) throws Exception;
	
	//==> 회원정보 ::  SELECT  ( 회원 정보 검색 )
	public List<User> getUserList(Search search) throws Exception;
	
}//end of class