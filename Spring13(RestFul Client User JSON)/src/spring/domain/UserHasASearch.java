package spring.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/*
 * FileName : User.java  
 * ㅇ User 의 정보를 갖는 Value Object (도메인객체,비지니스객체)
 * ㅇ USERS TABLE 의 1개의 ROW 의 정보를 갖는다.  
*/ 
public class UserHasASearch implements Serializable {

	///Field
    private String userId; 			// 회원 ID 
    private String userName;		// 회원 이름 
    private String password;     // 비밀번호 
    private Integer age;    			// 나이 :: Integer wrapper class 사용 :: 추후 용도 확인  
    private int grade;    				// 등급 :: int primitive 사용 :: :: 추후 용도 확인 
    private Timestamp regDate	; // 가입일자 
    //private Timestamp regDate	= new Timestamp(new Date().getTime()); // 가입일자
    private boolean active; 
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Association ( ~ Has A ~ ) Relation Test 로 추가된 Field
   
    //1. User Has A Search
    private Search search;
    
    public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	
	///Constructor
    public UserHasASearch() {
	}
    public UserHasASearch (	String userId, String userName,String password, 
    							Integer age, int grade ) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.age = age;
		this.grade = grade;
	}    

	///Method (getter/setter)
	public String getUserId(){
		return this.userId;
	}
	public void setUserId( String userId ){
	   this.userId= userId;
	}
	public String getPassword(){
	   return this.password;
	}
	public void setPassword( String password ){
	   this.password= password;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public java.sql.Timestamp getRegDate() {
		return regDate;
	}
	public void setRegDate(java.sql.Timestamp regDate) {
		this.regDate = regDate;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isActive() {
		return active;
	}
	@Override
	public String toString() {
		return "UserHasASearch [userId=" + userId + ", userName=" + userName + ", password=" + password + ", age=" + age
				+ ", grade=" + grade + ", regDate=" + regDate + ", active=" + active + ", search=" + search + "]";
	}
	
}//end of class