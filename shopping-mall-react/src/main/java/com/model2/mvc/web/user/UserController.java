package com.model2.mvc.web.user;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.domain.GoogleUserInfo;
import com.model2.mvc.service.domain.KakaoUserInfo;
import com.model2.mvc.service.domain.NaverUserInfo;
import com.model2.mvc.service.google.GoogleService;
import com.model2.mvc.service.kakao.KakaoService;
import com.model2.mvc.service.naver.NaverService;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/user/*")
public class UserController {

    // ========================================
    // ì˜ì¡´ì„± ì£¼ìž…
    // ========================================
    @Autowired @Qualifier("userServiceImpl")
    private UserService userService;
    
    @Autowired @Qualifier("kakaoServiceImpl")
    private KakaoService kakaoService;
    
    @Autowired @Qualifier("naverServiceImpl")
    private NaverService naverService;
    
    @Autowired @Qualifier("googleServiceImpl")
    private GoogleService googleService;

    // ========================================
    // common.properties ê°’ ì£¼ìž…
    // ========================================
    @Value("${page.unit}")
    private int pageUnit;
    
    @Value("${page.size}")
    private int pageSize;

    @Value("${social.kakao.rest-api-key}")
    private String kakaoApiKey;
    
    @Value("${social.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${social.naver.client-id}")
    private String naverClientId;
    
    @Value("${social.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${social.google.client-id}")
    private String googleClientId;
    
    @Value("${social.google.redirect-uri}")
    private String googleRedirectUri;

    public UserController() {
        System.out.println("==> UserController ìƒì„±");
    }

    // ========================================
    // íšŒì›ê°€ìž…
    // ========================================
    @RequestMapping(value = "addUser", method = RequestMethod.GET)
    public String addUserView() {
        System.out.println("/user/addUser : GET (íšŒì›ê°€ìž… íŽ˜ì´ì§€ ì´ë™)");
        return "user/addUserView";
    }

    // ========================================
    // ë¡œê·¸ì¸ / ë¡œê·¸ì•„ì›ƒ
    // ========================================
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginView(HttpSession session, Model model) {
        System.out.println("/user/login : GET (ë¡œê·¸ì¸ íŽ˜ì´ì§€ ì´ë™)");

        // ë„¤ì´ë²„ ë¡œê·¸ì¸ URL ìƒì„±
        String naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + naverClientId
                + "&redirect_uri=" + naverRedirectUri
                + "&state=login";

        // Google ë¡œê·¸ì¸ URL ìƒì„±
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?response_type=code"
                + "&client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&scope=email%20profile"
                + "&state=login";

        // Kakao ë¡œê·¸ì¸ URL ìƒì„±
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + kakaoApiKey
                + "&redirect_uri=" + kakaoRedirectUri
                + "&state=login";

        model.addAttribute("naverAuthUrl", naverAuthUrl);
        model.addAttribute("googleAuthUrl", googleAuthUrl);
        model.addAttribute("kakaoAuthUrl", kakaoAuthUrl);

        return "user/loginView";
    }
    
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@ModelAttribute("user") User user, HttpSession session) throws Exception {
        System.out.println("/user/login : POST (ë¡œê·¸ì¸ ì²˜ë¦¬)");
        
        User dbUser = userService.getUser(user.getUserId());
        
        if (dbUser != null && user.getPassword().equals(dbUser.getPassword())) {
            session.setAttribute("user", dbUser);
            System.out.println("  âœ… ë¡œê·¸ì¸ ì„±ê³µ: " + dbUser.getUserId());
        } else {
            System.out.println("  âŒ ë¡œê·¸ì¸ ì‹¤íŒ¨");
        }
        
        return "redirect:/";
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        System.out.println("/user/logout : GET");
        session.invalidate();
        return "redirect:/";
    }
    
    // ========================================
    // ì†Œì…œ ë¡œê·¸ì¸ ì½œë°±
    // ========================================
    
    /**
     * ì¹´ì¹´ì˜¤ ë¡œê·¸ì¸/ì—°ê²° ì½œë°±
     */
    @RequestMapping(value = "kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code,
                                @RequestParam(value = "state", required = false) String state,
                                HttpSession session, 
                                Model model) {
        System.out.println("==> /user/kakao/callback : GET");
        System.out.println("    code: " + code);
        System.out.println("    state: " + state);
        
        try {
            String accessToken = kakaoService.getAccessToken(code);
            KakaoUserInfo kakaoUser = kakaoService.getUserInfo(accessToken);
            
            // state ê°’ìœ¼ë¡œ 'ê³„ì • ì—°ê²°'ê³¼ 'ë¡œê·¸ì¸' ë¶„ê¸°
            if ("link".equals(state)) {
                System.out.println("==> ì¹´ì¹´ì˜¤ ê³„ì • ì—°ê²° ì‹œìž‘");
                User loggedInUser = (User) session.getAttribute("user");
                if (loggedInUser == null) {
                    System.out.println("    âŒ ë¡œê·¸ì¸ë˜ì§€ ì•Šì€ ì‚¬ìš©ìž");
                    return "redirect:/user/login"; 
                }
                
                try {
                    kakaoService.linkSocialAccount(kakaoUser, loggedInUser.getUserNo());
                    System.out.println("    âœ… ì¹´ì¹´ì˜¤ ê³„ì • ì—°ê²° ì„±ê³µ!");
                } catch (Exception e) {
                    System.out.println("    âŒ ì¹´ì¹´ì˜¤ ê³„ì • ì—°ê²° ì‹¤íŒ¨: " + e.getMessage());
                    session.setAttribute("flashMessage", e.getMessage());
                }
                
                return "redirect:/mypage#social";
            } else {
                System.out.println("==> ì¹´ì¹´ì˜¤ ë¡œê·¸ì¸/íšŒì›ê°€ìž… ì‹œìž‘");
                User user = kakaoService.saveOrUpdateUser(kakaoUser);
                session.setAttribute("user", user);
                System.out.println("    âœ… ì¹´ì¹´ì˜¤ ë¡œê·¸ì¸ ì„±ê³µ!");
                return "redirect:/";
            }
            
        } catch (Exception e) {
            System.out.println("    âŒ ì¹´ì¹´ì˜¤ ì²˜ë¦¬ ì‹¤íŒ¨: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "ì¹´ì¹´ì˜¤ ì²˜ë¦¬ ì¤‘ ì˜¤ë¥˜ê°€ ë°œìƒí–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return "forward:/user/login";
        }
    }

    /**
     * ë„¤ì´ë²„ ë¡œê·¸ì¸/ì—°ê²° ì½œë°±
     */
    @RequestMapping("/naver/callback")
    public String naverCallback(@RequestParam String code, 
                                @RequestParam(value = "state", required = false) String state, 
                                HttpSession session, 
                                Model model) {
        System.out.println("==> /user/naver/callback : GET");
        System.out.println("    code: " + code);
        System.out.println("    state: " + state);
        
        try {
            String accessToken = naverService.getAccessToken(code, state);
            NaverUserInfo naverUser = naverService.getUserInfo(accessToken);
            
            if ("link".equals(state)) {
                System.out.println("==> ë„¤ì´ë²„ ê³„ì • ì—°ê²° ì‹œìž‘");
                User loggedInUser = (User) session.getAttribute("user");
                if (loggedInUser == null) {
                    System.out.println("    âŒ ë¡œê·¸ì¸ë˜ì§€ ì•Šì€ ì‚¬ìš©ìž");
                    return "redirect:/user/login";
                }
                
                try {
                    naverService.linkSocialAccount(naverUser, loggedInUser.getUserNo());
                    System.out.println("    âœ… ë„¤ì´ë²„ ê³„ì • ì—°ê²° ì„±ê³µ!");
                } catch (Exception e) {
                    System.out.println("    âŒ ë„¤ì´ë²„ ê³„ì • ì—°ê²° ì‹¤íŒ¨: " + e.getMessage());
                    session.setAttribute("flashMessage", e.getMessage());
                }
                
                return "redirect:/mypage#social";
            } else {
                System.out.println("==> ë„¤ì´ë²„ ë¡œê·¸ì¸/íšŒì›ê°€ìž… ì‹œìž‘");
                User user = naverService.saveOrUpdateUser(naverUser);
                session.setAttribute("user", user);
                System.out.println("    âœ… ë„¤ì´ë²„ ë¡œê·¸ì¸ ì„±ê³µ!");
                return "redirect:/";
            }
            
        } catch (Exception e) {
            System.out.println("    âŒ ë„¤ì´ë²„ ì²˜ë¦¬ ì‹¤íŒ¨: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "ë„¤ì´ë²„ ì²˜ë¦¬ ì¤‘ ì˜¤ë¥˜ê°€ ë°œìƒí–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return "forward:/user/login";
        }
    }

    /**
     * êµ¬ê¸€ ë¡œê·¸ì¸/ì—°ê²° ì½œë°±
     */
    @RequestMapping("/google/callback")
    public String googleCallback(@RequestParam String code,
                                 @RequestParam(value = "state", required = false) String state,
                                 HttpSession session, 
                                 Model model) {
        System.out.println("==> /user/google/callback : GET");
        System.out.println("    code: " + code);
        System.out.println("    state: " + state);
        
        try {
            String accessToken = googleService.getAccessToken(code);
            GoogleUserInfo googleUser = googleService.getUserInfo(accessToken);
            
            if ("link".equals(state)) {
                System.out.println("==> êµ¬ê¸€ ê³„ì • ì—°ê²° ì‹œìž‘");
                User loggedInUser = (User) session.getAttribute("user");
                if (loggedInUser == null) {
                    System.out.println("    âŒ ë¡œê·¸ì¸ë˜ì§€ ì•Šì€ ì‚¬ìš©ìž");
                    return "redirect:/user/login";
                }
                
                try {
                    googleService.linkSocialAccount(googleUser, loggedInUser.getUserNo());
                    System.out.println("    âœ… êµ¬ê¸€ ê³„ì • ì—°ê²° ì„±ê³µ!");
                } catch (Exception e) {
                    System.out.println("    âŒ êµ¬ê¸€ ê³„ì • ì—°ê²° ì‹¤íŒ¨: " + e.getMessage());
                    session.setAttribute("flashMessage", e.getMessage());
                }
                
                return "redirect:/mypage#social";
            } else {
                System.out.println("==> êµ¬ê¸€ ë¡œê·¸ì¸/íšŒì›ê°€ìž… ì‹œìž‘");
                User user = googleService.saveOrUpdateUser(googleUser);
                session.setAttribute("user", user);
                System.out.println("    âœ… êµ¬ê¸€ ë¡œê·¸ì¸ ì„±ê³µ!");
                return "redirect:/";
            }
            
        } catch (Exception e) {
            System.out.println("    âŒ êµ¬ê¸€ ì²˜ë¦¬ ì‹¤íŒ¨: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "êµ¬ê¸€ ì²˜ë¦¬ ì¤‘ ì˜¤ë¥˜ê°€ ë°œìƒí–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return "forward:/user/login";
        }
    }

    // ========================================
    // íšŒì› ëª©ë¡ ì¡°íšŒ
    // ========================================
    
    /**
     * íšŒì› ëª©ë¡ ì¡°íšŒ (ì´ˆê¸° íŽ˜ì´ì§€ ë¡œë“œ - GET)
     */
    @RequestMapping(value = "listUser", method = RequestMethod.GET)
    public String listUserGet(@ModelAttribute("search") Search search, Model model) throws Exception {
        System.out.println("/user/listUser : GET (íšŒì› ëª©ë¡ íŽ˜ì´ì§€)");
        
        // 1. íŽ˜ì´ì§• ì„¤ì • (ì²« íŽ˜ì´ì§€)
        if (search.getCurrentPage() == 0) {
            search.setCurrentPage(1);
        }
        search.setPageSize(pageSize);
        
        // 2. ROWNUM ê³„ì‚°
        int startRowNum = (search.getCurrentPage() - 1) * pageSize + 1;
        int endRowNum = search.getCurrentPage() * pageSize;
        search.setStartRowNum(startRowNum);
        search.setEndRowNum(endRowNum);
        
        System.out.println("  currentPage: " + search.getCurrentPage());
        System.out.println("  pageSize: " + pageSize);
        System.out.println("  startRowNum: " + startRowNum + ", endRowNum: " + endRowNum);
        
        // 3. ì„œë¹„ìŠ¤ í˜¸ì¶œ
        Map<String, Object> map = userService.getUserList(search);
        
        // 4. JSPì— ë°ì´í„° ì „ë‹¬
        model.addAttribute("list", map.get("list"));
        model.addAttribute("totalCount", map.get("totalCount"));
        model.addAttribute("search", search);
        
        System.out.println("  ì¡°íšŒëœ íšŒì› ìˆ˜: " + ((List<?>)map.get("list")).size());
        System.out.println("  ì „ì²´ íšŒì› ìˆ˜: " + map.get("totalCount"));
        
        return "user/listUser";
    }

    /**
     * íšŒì› ëª©ë¡ ì¡°íšŒ (ê²€ìƒ‰ - POST)
     */
    @RequestMapping(value = "listUser", method = RequestMethod.POST)
    public String listUserPost(@ModelAttribute("search") Search search, Model model) throws Exception {
        System.out.println("/user/listUser : POST (ê²€ìƒ‰ ìˆ˜í–‰)");
        
        // 1. íŽ˜ì´ì§• ì„¤ì •
        if (search.getCurrentPage() == 0) {
            search.setCurrentPage(1);
        }
        search.setPageSize(pageSize);
        
        // 2. ROWNUM ê³„ì‚°
        int startRowNum = (search.getCurrentPage() - 1) * pageSize + 1;
        int endRowNum = search.getCurrentPage() * pageSize;
        search.setStartRowNum(startRowNum);
        search.setEndRowNum(endRowNum);
        
        System.out.println("  searchCondition: " + search.getSearchCondition());
        System.out.println("  searchKeyword: " + search.getSearchKeyword());
        System.out.println("  startRowNum: " + startRowNum + ", endRowNum: " + endRowNum);
        
        // 3. ì„œë¹„ìŠ¤ í˜¸ì¶œ
        Map<String, Object> map = userService.getUserList(search);
        
        // 4. JSPì— ë°ì´í„° ì „ë‹¬
        model.addAttribute("list", map.get("list"));
        model.addAttribute("totalCount", map.get("totalCount"));
        model.addAttribute("search", search);
        
        System.out.println("  ì¡°íšŒëœ íšŒì› ìˆ˜: " + ((List<?>)map.get("list")).size());
        
        return "user/listUser";
    }

    // ========================================
    // íšŒì› ìƒì„¸ ì¡°íšŒ
    // ========================================
    
    /**
     * íšŒì› ìƒì„¸ ì •ë³´ ì¡°íšŒ
     */
    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public String getUser(@RequestParam(value = "userId", required = false) String userId,
                          @RequestParam(value = "userNo", required = false) Integer userNo,
                          Model model) throws Exception {
        System.out.println("/user/getUser : GET");
        System.out.println("  userId: " + userId);
        System.out.println("  userNo: " + userNo);
        
        User user = null;
        
        // userIdê°€ ìžˆìœ¼ë©´ userIdë¡œ ì¡°íšŒ, ì—†ìœ¼ë©´ userNoë¡œ ì¡°íšŒ
        if (userId != null && !userId.trim().isEmpty()) {
            user = userService.getUser(userId);
        } else if (userNo != null) {
            user = userService.getUserByUserNo(userNo);
        }
        
        if (user == null) {
            System.out.println("  âŒ íšŒì›ì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
            return "redirect:/user/listUser";
        }
        
        model.addAttribute("user", user);
        System.out.println("  âœ… íšŒì› ì¡°íšŒ ì„±ê³µ: " + user.getUserName());
        
        return "user/getUser";
    }

    // ========================================
    // íšŒì› ì •ë³´ ìˆ˜ì •
    // ========================================
    
    /**
     * íšŒì› ìˆ˜ì • íŽ˜ì´ì§€ë¡œ ì´ë™
     */
    @RequestMapping(value = "updateUser", method = RequestMethod.GET)
    public String updateUserView(@RequestParam("userId") String userId, Model model) throws Exception {
        System.out.println("/user/updateUser : GET");
        
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        
        return "user/updateUser";
    }
    
    /**
     * íšŒì› ì •ë³´ ìˆ˜ì • ì²˜ë¦¬
     */
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") User user, 
                            HttpSession session) throws Exception {
        System.out.println("/user/updateUser : POST");
        System.out.println("  ìˆ˜ì •í•  userNo: " + user.getUserNo());
        
        userService.updateUser(user);
        
        // ì„¸ì…˜ ì •ë³´ ì—…ë°ì´íŠ¸
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null && sessionUser.getUserNo() == user.getUserNo()) {
            User updatedUser = userService.getUserByUserNo(user.getUserNo());
            session.setAttribute("user", updatedUser);
            System.out.println("  âœ… ì„¸ì…˜ ì •ë³´ ì—…ë°ì´íŠ¸ ì™„ë£Œ");
        }
        
        return "redirect:/user/getUser?userId=" + user.getUserId();
    }
}