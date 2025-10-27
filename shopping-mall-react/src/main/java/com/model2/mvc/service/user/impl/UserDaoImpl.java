package com.model2.mvc.service.user.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserDao;

/**
 * 회원관리 DAO CRUD 구현
 */
@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {
    
    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;
    
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    public UserDaoImpl() {
        System.out.println(this.getClass());
    }
    
    @Override
    public void addUser(User user) throws Exception {
        System.out.println("==> UserDaoImpl.addUser() 시작");
        sqlSession.insert("UserMapper.addUser", user);
        System.out.println("==> 일반 회원 등록 완료, userNo: " + user.getUserNo());
    }
    
    // ✅ 추가: 소셜 로그인용 회원가입
    @Override
    public void addSocialUser(User user) throws Exception {
        System.out.println("==> UserDaoImpl.addSocialUser() 시작");
        sqlSession.insert("UserMapper.addSocialUser", user);
        System.out.println("==> 소셜 로그인 회원 등록 완료, userNo: " + user.getUserNo());
    }
    
    @Override
    public User getUser(String userId) throws Exception {
        System.out.println("==> UserDaoImpl.getUser() 시작, userId: " + userId);
        User user = sqlSession.selectOne("UserMapper.getUser", userId);
        System.out.println("==> 조회 결과: " + user);
        return user;
    }
    
    // ✅ 추가: userNo로 회원 조회
    @Override
    public User getUserByUserNo(int userNo) throws Exception {
        System.out.println("==> UserDaoImpl.getUserByUserNo() 시작, userNo: " + userNo);
        User user = sqlSession.selectOne("UserMapper.getUserByUserNo", userNo);
        System.out.println("==> 조회 결과: " + user);
        return user;
    }
    
    @Override
    public void updateUser(User user) throws Exception {
        System.out.println("==> UserDaoImpl.updateUser() 시작");
        sqlSession.update("UserMapper.updateUser", user);
        System.out.println("==> 회원 정보 수정 완료");
    }
    
    @Override
    public void deleteUser(String userId) throws Exception {
        System.out.println("==> UserDaoImpl.deleteUser() 시작, userId: " + userId);
        sqlSession.delete("UserMapper.deleteUser", userId);
        System.out.println("==> 회원 삭제 완료");
    }
    
    @Override
    public List<User> getUserList(Search search) throws Exception {
        System.out.println("==> UserDaoImpl.getUserList() 시작");
        List<User> list = sqlSession.selectList("UserMapper.getUserList", search);
        System.out.println("==> 조회된 회원 수: " + (list != null ? list.size() : 0));
        return list;
    }
    
    @Override
    public int getTotalCount(Search search) throws Exception {
        System.out.println("==> UserDaoImpl.getTotalCount() 시작");
        int count = sqlSession.selectOne("UserMapper.getTotalCount", search);
        System.out.println("==> 전체 회원 수: " + count);
        return count;
    }
}
