package com.model2.mvc.service.user;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;

/**
 * 회원 관리 Service 인터페이스
 */
public interface UserService {
    
    /**
     * 일반 회원가입
     */
    void addUser(User user) throws Exception;
    
    /**
     * 소셜 로그인 회원가입
     */
    void addSocialUser(User user) throws Exception;
    
    /**
     * 회원 조회 (userId)
     */
    User getUser(String userId) throws Exception;
    
    /**
     * 회원 조회 (userNo)
     */
    User getUserByUserNo(int userNo) throws Exception;
    
    /**
     * 회원 목록 조회
     */
    Map<String, Object> getUserList(Search search) throws Exception;
    
    /**
     * 회원 정보 수정
     */
    void updateUser(User user) throws Exception;
    
    /**
     * 아이디 중복 체크
     */
    boolean checkDuplication(String userId) throws Exception;
    
    /**
     * 회원 탈퇴
     */
    void deleteUser(String userId) throws Exception;
}
