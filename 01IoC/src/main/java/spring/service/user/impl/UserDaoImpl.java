package spring.service.user.impl;

import java.util.List;

//import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import spring.domain.Search;
import spring.domain.User;
import spring.service.user.UserDao;

/* 
 * FileName : UserDaoImpl14.java  ( Data Access Object ) 
 * ㅇ 데이터베이스와 직접적인 통신을 담당하는 퍼시스턴스 계층을 담당할 UserDao 인터페이스 구현 
*/ 
@Repository("userDao")
public class UserDaoImpl implements UserDao{

//	///Field
//	@Autowired
//	@Qualifier("sqlSessionTemplate")
//	private SqlSession sqlSession;
//	
//	public void setSqlSession(SqlSession sqlSession) {
//		System.out.println("::"+getClass()+".setSqlSession()  Call.....");
//		this.sqlSession = sqlSession;
//	}

	///Constructor
	public UserDaoImpl() {
		System.out.println("::"+getClass()+" default Constructor Call.....");
	}
	
	///Method
	//==> 회원정보 ::  INSERT ( 회원가입 )
	public int addUser(User user) throws Exception{
		//return sqlSession.insert("UserMapper10.addUser",user);
		return 0;
	}//end of method
	
	//==> 회원정보 ::  SELECT  ( 회원정보를 검색 ) 
	public User getUser(String userId) throws Exception {
		 //return (User)sqlSession.selectOne("UserMapper10.getUser",userId);
		return null;
	}//end of method

	//==> 회원정보 ::  UPDATE  ( 회원정보 변경  )
	public int updateUser(User user) throws Exception {
		//return sqlSession.update("UserMapper10.updateUser",user);
		return 0;
	}//end of method
	
	//==> 회원정보 ::  DELETE  ( 회원정보 삭제 )
	public int removeUser(String userId) throws Exception{
		//return sqlSession.delete("UserMapper10.removeUser", userId );
		return 0;
	}
	
	//==> 회원정보 ::  SELECT  ( 모든 회원 정보 검색 )
	public List<User> getUserList(Search search) throws Exception {
		//return sqlSession.selectList("UserMapper10.getUserList",search);
		return null;
	}//end of Method
	
}//end of class