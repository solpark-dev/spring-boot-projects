package com.model2.mvc.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserDao;
import com.model2.mvc.service.user.UserService;

/**
 * 회원관리 ServiceImpl
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
    
    @Autowired
    @Qualifier("userDaoImpl")
    private UserDao userDao;
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public UserServiceImpl() {
        System.out.println(this.getClass());
    }
    
    @Override
    public void addUser(User user) throws Exception {
        System.out.println("==> UserServiceImpl.addUser() 시작");
        
        // role이 없으면 기본값 설정
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("user");
        }
        
        userDao.addUser(user);
        System.out.println("==> 일반 회원가입 완료");
    }
    
    // ✅ 추가: 소셜 로그인용 회원가입
    @Override
    public void addSocialUser(User user) throws Exception {
        System.out.println("==> UserServiceImpl.addSocialUser() 시작");
        
        // role이 없으면 기본값 설정
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("user");
        }
        
        userDao.addSocialUser(user);
        System.out.println("==> 소셜 로그인 회원가입 완료");
    }
    
    @Override
    public User getUser(String userId) throws Exception {
        System.out.println("==> UserServiceImpl.getUser() 시작");
        User user = userDao.getUser(userId);
        
        if (user == null) {
            System.out.println("==> 회원을 찾을 수 없습니다: " + userId);
        }
        
        return user;
    }
    
    // ✅ 추가: userNo로 회원 조회
    @Override
    public User getUserByUserNo(int userNo) throws Exception {
        System.out.println("==> UserServiceImpl.getUserByUserNo() 시작");
        User user = userDao.getUserByUserNo(userNo);
        
        if (user == null) {
            System.out.println("==> 회원을 찾을 수 없습니다: userNo=" + userNo);
        }
        
        return user;
    }
    
    @Override
    public Map<String, Object> getUserList(Search search) throws Exception {
        System.out.println("==> UserServiceImpl.getUserList() 시작");
        
        List<User> list = userDao.getUserList(search);
        int totalCount = userDao.getTotalCount(search);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("totalCount", new Integer(totalCount));
        
        System.out.println("==> 회원 목록 조회 완료, 총 " + totalCount + "명");
        
        return map;
    }
    
    @Override
    public void updateUser(User user) throws Exception {
        System.out.println("==> UserServiceImpl.updateUser() 시작");
        userDao.updateUser(user);
        System.out.println("==> 회원 정보 수정 완료");
    }
    
    @Override
    public boolean checkDuplication(String userId) throws Exception {
        System.out.println("==> UserServiceImpl.checkDuplication() 시작");
        
        boolean result = false;
        User user = userDao.getUser(userId);
        
        if (user != null) {
            result = true;
            System.out.println("==> 중복된 ID입니다: " + userId);
        } else {
            System.out.println("==> 사용 가능한 ID입니다: " + userId);
        }
        
        return result;
    }
    
    @Override
    public void deleteUser(String userId) throws Exception {
        System.out.println("==> UserServiceImpl.deleteUser() 시작");
        userDao.deleteUser(userId);
        System.out.println("==> 회원 삭제 완료");
    }
}
