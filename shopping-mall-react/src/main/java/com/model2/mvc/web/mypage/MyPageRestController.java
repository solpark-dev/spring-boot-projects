package com.model2.mvc.web.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.cart.CartService;
import com.model2.mvc.service.domain.Cart;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.SocialAccount;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.social.SocialAccountDao;
import com.model2.mvc.service.user.UserService;

/**
 * ë§ˆì´íŽ˜ì´ì§€ REST API ì»¨íŠ¸ë¡¤ëŸ¬
 * JSON í˜•ì‹ìœ¼ë¡œ ë°ì´í„° ì£¼ê³ ë°›ê¸°
 */
@RestController
@RequestMapping("/mypage/json")
public class MyPageRestController {
    
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    
    @Autowired
    @Qualifier("cartServiceImpl")
    private CartService cartService;
    
    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;
    
    @Autowired
    @Qualifier("socialAccountDaoImpl")
    private SocialAccountDao socialAccountDao;
    
    
    // ========================================
    // í”„ë¡œí•„ ê´€ë ¨ API
    // ========================================
    
    /**
     * í”„ë¡œí•„ ì •ë³´ ìˆ˜ì • (ì´ë¦„, ì´ë©”ì¼)
     * PUT /mypage/json/updateProfile
     */
    @PutMapping("/updateProfile")
    public Map<String, Object> updateProfile(
            @RequestBody Map<String, String> requestData,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /mypage/json/updateProfile");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìš”ì²­ ë°ì´í„° ì¶”ì¶œ
            String userName = requestData.get("userName");
            String email = requestData.get("email");
            
            // ìœ íš¨ì„± ê²€ì‚¬
            if (userName == null || userName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "ì´ë¦„ì„ ìž…ë ¥í•´ì£¼ì„¸ìš”.");
                return response;
            }
            
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "ì´ë©”ì¼ì„ ìž…ë ¥í•´ì£¼ì„¸ìš”.");
                return response;
            }
            
            // User ê°ì²´ ìƒì„± ë° ì—…ë°ì´íŠ¸
            User user = new User();
            user.setUserNo(sessionUser.getUserNo());
            user.setUserName(userName.trim());
            user.setEmail(email.trim());
            
            // ì„œë¹„ìŠ¤ í˜¸ì¶œ
            userService.updateUser(user);
            
            // ì„¸ì…˜ ì—…ë°ì´íŠ¸
            User updatedUser = userService.getUserByUserNo(sessionUser.getUserNo());
            session.setAttribute("user", updatedUser);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "í”„ë¡œí•„ì´ ìˆ˜ì •ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("user", updatedUser);
            
        } catch (Exception e) {
            System.out.println("í”„ë¡œí•„ ìˆ˜ì • ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "í”„ë¡œí•„ ìˆ˜ì •ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    // ========================================
    // ìž¥ë°”êµ¬ë‹ˆ ê´€ë ¨ API
    // ========================================
    
    /**
     * ìž¥ë°”êµ¬ë‹ˆ ëª©ë¡ ì¡°íšŒ
     * GET /mypage/json/getCart
     */
    @GetMapping("/getCart")
    public Map<String, Object> getCart(HttpSession session) throws Exception {
        
        System.out.println("==> GET /mypage/json/getCart");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìž¥ë°”êµ¬ë‹ˆ ëª©ë¡ ì¡°íšŒ
            List<Cart> cartList = cartService.getCartList(sessionUser.getUserNo());
            
            // ì´ ê¸ˆì•¡ ê³„ì‚°
            int totalPrice = 0;
            if (cartList != null) {
                for (Cart cart : cartList) {
                    if (cart.getProduct() != null) {
                        totalPrice += cart.getProduct().getPrice() * cart.getQuantity();
                    }
                }
            }
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("cartList", cartList);
            response.put("totalCount", cartList != null ? cartList.size() : 0);
            response.put("totalPrice", totalPrice);
            
            System.out.println("==> ìž¥ë°”êµ¬ë‹ˆ ì¡°íšŒ ì™„ë£Œ: " + (cartList != null ? cartList.size() : 0) + "ê°œ");
            
        } catch (Exception e) {
            System.out.println("ìž¥ë°”êµ¬ë‹ˆ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ìž¥ë°”êµ¬ë‹ˆ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ìž¥ë°”êµ¬ë‹ˆì— ìƒí’ˆ ì¶”ê°€
     * POST /mypage/json/addToCart
     */
    @PostMapping("/addToCart")
    public Map<String, Object> addToCart(
            @RequestBody Map<String, Object> requestData,
            HttpSession session) throws Exception {
        
        System.out.println("==> POST /mypage/json/addToCart");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìš”ì²­ ë°ì´í„° ì¶”ì¶œ
            Integer prodNo = (Integer) requestData.get("prodNo");
            Integer quantity = requestData.get("quantity") != null 
                    ? (Integer) requestData.get("quantity") 
                    : 1;
            
            // ìœ íš¨ì„± ê²€ì‚¬
            if (prodNo == null) {
                response.put("success", false);
                response.put("message", "ìƒí’ˆ ë²ˆí˜¸ê°€ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìž¥ë°”êµ¬ë‹ˆì— ì¶”ê°€
            Cart cart = cartService.addToCart(sessionUser.getUserNo(), prodNo, quantity);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ìž¥ë°”êµ¬ë‹ˆì— ì¶”ê°€ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("cart", cart);
            
            System.out.println("==> ìž¥ë°”êµ¬ë‹ˆ ì¶”ê°€ ì™„ë£Œ: cartId=" + cart.getCartId());
            
        } catch (Exception e) {
            System.out.println("ìž¥ë°”êµ¬ë‹ˆ ì¶”ê°€ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ìž¥ë°”êµ¬ë‹ˆ ì¶”ê°€ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ìž¥ë°”êµ¬ë‹ˆ ìˆ˜ëŸ‰ ë³€ê²½
     * PUT /mypage/json/updateCartQuantity
     */
    @PutMapping("/updateCartQuantity")
    public Map<String, Object> updateCartQuantity(
            @RequestBody Map<String, Object> requestData,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /mypage/json/updateCartQuantity");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìš”ì²­ ë°ì´í„° ì¶”ì¶œ
            Integer cartId = (Integer) requestData.get("cartId");
            Integer quantity = (Integer) requestData.get("quantity");
            
            // ìœ íš¨ì„± ê²€ì‚¬
            if (cartId == null || quantity == null) {
                response.put("success", false);
                response.put("message", "ìž¥ë°”êµ¬ë‹ˆ IDì™€ ìˆ˜ëŸ‰ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            if (quantity < 1) {
                response.put("success", false);
                response.put("message", "ìˆ˜ëŸ‰ì€ 1 ì´ìƒì´ì–´ì•¼ í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìˆ˜ëŸ‰ ë³€ê²½
            cartService.updateCartQuantity(cartId, quantity);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ìˆ˜ëŸ‰ì´ ë³€ê²½ë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ìˆ˜ëŸ‰ ë³€ê²½ ì™„ë£Œ: cartId=" + cartId + ", quantity=" + quantity);
            
        } catch (Exception e) {
            System.out.println("ìˆ˜ëŸ‰ ë³€ê²½ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ìˆ˜ëŸ‰ ë³€ê²½ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ìž¥ë°”êµ¬ë‹ˆ í•­ëª© ì‚­ì œ (ë‹¨ì¼)
     * DELETE /mypage/json/removeFromCart
     */
    @DeleteMapping("/removeFromCart")
    public Map<String, Object> removeFromCart(
            @RequestParam("cartId") int cartId,
            HttpSession session) throws Exception {
        
        System.out.println("==> DELETE /mypage/json/removeFromCart");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìž¥ë°”êµ¬ë‹ˆ í•­ëª© ì‚­ì œ
            cartService.removeFromCart(cartId);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ìž¥ë°”êµ¬ë‹ˆì—ì„œ ì‚­ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ìž¥ë°”êµ¬ë‹ˆ ì‚­ì œ ì™„ë£Œ: cartId=" + cartId);
            
        } catch (Exception e) {
            System.out.println("ìž¥ë°”êµ¬ë‹ˆ ì‚­ì œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì‚­ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ìž¥ë°”êµ¬ë‹ˆ í•­ëª© ì‚­ì œ (ì—¬ëŸ¬ ê°œ)
     * POST /mypage/json/removeCartItems
     */
    @PostMapping("/removeCartItems")
    public Map<String, Object> removeCartItems(
            @RequestBody Map<String, Object> requestData,
            HttpSession session) throws Exception {
        
        System.out.println("==> POST /mypage/json/removeCartItems");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ìš”ì²­ ë°ì´í„° ì¶”ì¶œ
            @SuppressWarnings("unchecked")
            List<Integer> cartIdList = (List<Integer>) requestData.get("cartIdList");
            
            // ìœ íš¨ì„± ê²€ì‚¬
            if (cartIdList == null || cartIdList.isEmpty()) {
                response.put("success", false);
                response.put("message", "ì‚­ì œí•  í•­ëª©ì´ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì—¬ëŸ¬ í•­ëª© ì‚­ì œ
            cartService.removeCartItems(cartIdList);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", cartIdList.size() + "ê°œ í•­ëª©ì´ ì‚­ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ì—¬ëŸ¬ í•­ëª© ì‚­ì œ ì™„ë£Œ: " + cartIdList.size() + "ê°œ");
            
        } catch (Exception e) {
            System.out.println("ì—¬ëŸ¬ í•­ëª© ì‚­ì œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì‚­ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ìž¥ë°”êµ¬ë‹ˆ ì „ì²´ ë¹„ìš°ê¸°
     * DELETE /mypage/json/clearCart
     */
    @DeleteMapping("/clearCart")
    public Map<String, Object> clearCart(HttpSession session) throws Exception {
        
        System.out.println("==> DELETE /mypage/json/clearCart");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ì „ì²´ ìž¥ë°”êµ¬ë‹ˆ ë¹„ìš°ê¸°
            cartService.clearCart(sessionUser.getUserNo());
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ìž¥ë°”êµ¬ë‹ˆê°€ ë¹„ì›Œì¡ŒìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ìž¥ë°”êµ¬ë‹ˆ ì „ì²´ ë¹„ìš°ê¸° ì™„ë£Œ");
            
        } catch (Exception e) {
            System.out.println("ìž¥ë°”êµ¬ë‹ˆ ë¹„ìš°ê¸° ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ìž¥ë°”êµ¬ë‹ˆ ë¹„ìš°ê¸°ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    // ========================================
    // ì£¼ë¬¸ ë‚´ì—­ ê´€ë ¨ API (ìƒˆë¡œ ì¶”ê°€)
    // ========================================
    
    /**
     * ì£¼ë¬¸ ë‚´ì—­ ì¡°íšŒ
     * GET /mypage/json/getOrders
     * 
     * @param currentPage í˜„ìž¬ íŽ˜ì´ì§€ (ê¸°ë³¸ê°’: 1)
     * @param pageSize íŽ˜ì´ì§€ë‹¹ í•­ëª© ìˆ˜ (ê¸°ë³¸ê°’: 10)
     * @param statusFilter ì£¼ë¬¸ ìƒíƒœ í•„í„° (ì˜µì…˜: ALL, PAYMENT_COMPLETE, SHIPPED, DELIVERED)
     * @param session
     * @return { success, orderList, totalCount, currentPage, totalPages }
     */
    @GetMapping("/getOrders")
    public Map<String, Object> getOrders(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "statusFilter", defaultValue = "ALL") String statusFilter,
            HttpSession session) throws Exception {
        
        System.out.println("==> GET /mypage/json/getOrders");
        System.out.println("    currentPage: " + currentPage + ", pageSize: " + pageSize + ", statusFilter: " + statusFilter);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // Search ê°ì²´ ìƒì„±
            Search search = new Search();
            search.setCurrentPage(currentPage);
            search.setPageSize(pageSize);
            
            // ìƒíƒœ í•„í„° ì„¤ì • (ALLì´ ì•„ë‹Œ ê²½ìš°ë§Œ)
            if (!"ALL".equals(statusFilter)) {
                search.setSearchCondition("orderStatus");
                search.setSearchKeyword(statusFilter);
            }
            
            // ì£¼ë¬¸ ëª©ë¡ ì¡°íšŒ
            Map<String, Object> result = purchaseService.getPurchaseList(search, sessionUser.getUserId());
            
            @SuppressWarnings("unchecked")
            List<Purchase> orderList = (List<Purchase>) result.get("list");
            int totalCount = (Integer) result.get("totalCount");
            
            // íŽ˜ì´ì§€ ê³„ì‚°
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("orderList", orderList);
            response.put("totalCount", totalCount);
            response.put("currentPage", currentPage);
            response.put("pageSize", pageSize);
            response.put("totalPages", totalPages);
            
            System.out.println("==> ì£¼ë¬¸ ë‚´ì—­ ì¡°íšŒ ì™„ë£Œ: " + (orderList != null ? orderList.size() : 0) + "ê°œ");
            
        } catch (Exception e) {
            System.out.println("ì£¼ë¬¸ ë‚´ì—­ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì£¼ë¬¸ ë‚´ì—­ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ
     * GET /mypage/json/getOrderDetail
     * 
     * @param orderNo ì£¼ë¬¸ ë²ˆí˜¸
     * @param session
     * @return { success, order }
     */
    @GetMapping("/getOrderDetail")
    public Map<String, Object> getOrderDetail(
            @RequestParam("orderNo") int orderNo,
            HttpSession session) throws Exception {
        
        System.out.println("==> GET /mypage/json/getOrderDetail, orderNo: " + orderNo);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ
            Purchase order = purchaseService.getPurchase(orderNo);
            
            if (order == null) {
                response.put("success", false);
                response.put("message", "ì£¼ë¬¸ ì •ë³´ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ê¶Œí•œ ì²´í¬ (ë³¸ì¸ ì£¼ë¬¸ë§Œ ì¡°íšŒ ê°€ëŠ¥)
            if (order.getBuyer() == null || order.getBuyer().getUserNo() != sessionUser.getUserNo()) {
                response.put("success", false);
                response.put("message", "ì ‘ê·¼ ê¶Œí•œì´ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("order", order);
            
            System.out.println("==> ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ ì™„ë£Œ");
            
        } catch (Exception e) {
            System.out.println("ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    // ========================================
    // ì†Œì…œ ê³„ì • ê´€ë ¨ API (ìƒˆë¡œ ì¶”ê°€)
    // ========================================
    
    /**
     * ì†Œì…œ ê³„ì • ëª©ë¡ ì¡°íšŒ
     * GET /mypage/json/getSocialAccounts
     * 
     * @param session
     * @return { success, socialAccounts }
     */
    @GetMapping("/getSocialAccounts")
    public Map<String, Object> getSocialAccounts(HttpSession session) throws Exception {
        
        System.out.println("==> GET /mypage/json/getSocialAccounts");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ì†Œì…œ ê³„ì • ëª©ë¡ ì¡°íšŒ
            List<SocialAccount> socialAccounts = socialAccountDao.findByUserNo(sessionUser.getUserNo());
            
            // ì—°ê²° ìƒíƒœë¥¼ ì‰½ê²Œ í™•ì¸í•  ìˆ˜ ìžˆë„ë¡ ê°€ê³µ
            Map<String, Boolean> connectionStatus = new HashMap<>();
            connectionStatus.put("KAKAO", false);
            connectionStatus.put("NAVER", false);
            connectionStatus.put("GOOGLE", false);
            
            if (socialAccounts != null) {
                for (SocialAccount account : socialAccounts) {
                    connectionStatus.put(account.getProvider(), true);
                }
            }
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("socialAccounts", socialAccounts != null ? socialAccounts : new ArrayList<>());
            response.put("connectionStatus", connectionStatus);
            response.put("totalCount", socialAccounts != null ? socialAccounts.size() : 0);
            
            System.out.println("==> ì†Œì…œ ê³„ì • ì¡°íšŒ ì™„ë£Œ: " + (socialAccounts != null ? socialAccounts.size() : 0) + "ê°œ");
            
        } catch (Exception e) {
            System.out.println("ì†Œì…œ ê³„ì • ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì†Œì…œ ê³„ì • ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    
    /**
     * ì†Œì…œ ê³„ì • ì—°ê²° í•´ì œ
     * DELETE /mypage/json/disconnectSocialAccount
     * 
     * @param socialAccountId ì†Œì…œ ê³„ì • ID
     * @param session
     * @return { success, message }
     */
    @DeleteMapping("/disconnectSocialAccount")
    public Map<String, Object> disconnectSocialAccount(
            @RequestParam("socialAccountId") int socialAccountId,
            HttpSession session) throws Exception {
        
        System.out.println("==> DELETE /mypage/json/disconnectSocialAccount, id: " + socialAccountId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ì†Œì…œ ê³„ì • ì—°ê²° í•´ì œ
            socialAccountDao.deleteSocialAccount(socialAccountId);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì†Œì…œ ê³„ì • ì—°ê²°ì´ í•´ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ì†Œì…œ ê³„ì • ì—°ê²° í•´ì œ ì™„ë£Œ");
            
        } catch (Exception e) {
            System.out.println("ì†Œì…œ ê³„ì • ì—°ê²° í•´ì œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì†Œì…œ ê³„ì • ì—°ê²° í•´ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
}