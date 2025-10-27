package com.model2.mvc.web.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.common.Search;

/**
 * UserRestController.java
 * 회원 관리를 위한 RESTful API 컨트롤러
 * URL Prefix : /user/json/
 */
@RestController
@RequestMapping("/user/json")
public class UserRestController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    public UserRestController() {
        System.out.println("==> UserRestController 실행됨 : " + this.getClass());
    }

    /**
     * 회원가입 요청 처리
     * ✅ 수정: 프론트엔드 기대 형식에 맞게 응답 변경
     * @param user 회원 정보 (JSON 형식으로 전달됨)
     * @return { success: boolean, message: string }
     */
    @PostMapping("/addUser")
    public Map<String, Object> addUser(@RequestBody User user) throws Exception {
        System.out.println("/user/json/addUser : POST 호출됨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ✅ 일반 회원가입
            userService.addUser(user);
            
            response.put("success", true);
            response.put("message", "회원가입이 완료되었습니다.");
            
        } catch (Exception e) {
            System.out.println("회원가입 중 오류: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "회원가입에 실패했습니다: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * 로그인 요청 처리
     * @param user 아이디 및 패스워드 정보 포함
     * @param session 로그인 성공 시 세션에 저장
     * @return DB에 저장된 사용자 정보 반환
     */
    @PostMapping("/login")
    public User login(@RequestBody User user, HttpSession session) throws Exception {
        System.out.println("/user/json/login : POST 호출됨");

        // ✅ userId로 사용자 조회
        User dbUser = userService.getUser(user.getUserId());

        // 패스워드 일치 시 세션 저장
        if (dbUser != null && user.getPassword().equals(dbUser.getPassword())) {
            session.setAttribute("user", dbUser);
        }

        return dbUser;
    }

    /**
     * 로그아웃 요청 처리
     * @param session HTTP 세션
     * @return 성공 메시지
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) throws Exception {
        System.out.println("/user/json/logout : GET 호출됨");
        
        session.invalidate();
        
        return "logout success";
    }

    /**
     * 회원 정보 조회
     * @param userId 조회할 사용자 ID
     * @return 사용자 정보
     */
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) throws Exception {
        System.out.println("/user/json/" + userId + " : GET 호출됨");
        
        // ✅ userId로 조회
        return userService.getUser(userId);
    }

    /**
     * 회원 정보 수정
     * @param user 수정할 회원 정보 (JSON 형식)
     * @return 수정된 사용자 정보
     */
    @PutMapping
    public User updateUser(@RequestBody User user, HttpSession session) throws Exception {
        System.out.println("/user/json/updateUser : PUT 호출됨");
        
        // ✅ 수정
        userService.updateUser(user);
        
        // 세션 정보 업데이트
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null && sessionUser.getUserNo() == user.getUserNo()) {
            User updatedUser = userService.getUserByUserNo(user.getUserNo());
            session.setAttribute("user", updatedUser);
            return updatedUser;
        }
        
        return userService.getUserByUserNo(user.getUserNo());
    }

    /**
     * 회원 목록 조회 (무한 스크롤용 - GET 방식)
     * GET /user/json/getUserList?currentPage=2&pageSize=10&searchCondition=0&searchKeyword=
     */
    @GetMapping("/getUserList")
    public Map<String, Object> getUserList(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchCondition", required = false) String searchCondition,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword
    ) throws Exception {
        
        System.out.println("/user/json/getUserList : GET 호출됨");
        System.out.println("  currentPage: " + currentPage);
        System.out.println("  pageSize: " + pageSize);
        System.out.println("  searchCondition: " + searchCondition);
        System.out.println("  searchKeyword: " + searchKeyword);
        
        // Search 객체 생성
        Search search = new Search();
        search.setCurrentPage(currentPage);
        search.setPageSize(pageSize);
        
        // 검색 조건 설정
        if (searchCondition != null && !searchCondition.trim().isEmpty()) {
            search.setSearchCondition(searchCondition);
        }
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            search.setSearchKeyword(searchKeyword);
        }
        
        // 페이징 계산
        int startRowNum = (currentPage - 1) * pageSize + 1;
        int endRowNum = currentPage * pageSize;
        search.setStartRowNum(startRowNum);
        search.setEndRowNum(endRowNum);
        
        System.out.println("  startRowNum: " + startRowNum);
        System.out.println("  endRowNum: " + endRowNum);
        
        // 서비스 호출
        Map<String, Object> result = userService.getUserList(search);
        
        System.out.println("  조회된 회원 수: " + ((List<?>)result.get("list")).size());
        System.out.println("  전체 회원 수: " + result.get("totalCount"));
        
        return result;
    }

    /**
     * ID 중복 확인
     * ✅ 수정: 프론트엔드 기대 형식에 맞게 응답 변경
     * @param requestData { userId: string }
     * @return { isDuplicate: boolean, message: string }
     */
    @PostMapping("/checkDuplication")
    public Map<String, Object> checkDuplication(@RequestBody Map<String, String> requestData) throws Exception {
        System.out.println("/user/json/checkDuplication : POST 호출됨");
        
        String userId = requestData.get("userId");
        System.out.println("  확인할 ID: " + userId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean isDuplicate = userService.checkDuplication(userId);
            
            response.put("isDuplicate", isDuplicate);
            response.put("message", isDuplicate ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.");
            
        } catch (Exception e) {
            System.out.println("중복 확인 중 오류: " + e.getMessage());
            e.printStackTrace();
            
            response.put("isDuplicate", true);  // 에러 시 안전하게 중복으로 처리
            response.put("message", "중복 확인 중 오류가 발생했습니다.");
        }
        
        return response;
    }
    
    @GetMapping("/checkLogin")
    public User checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user;  // 로그인 상태면 User 반환, 아니면 null
    }
}