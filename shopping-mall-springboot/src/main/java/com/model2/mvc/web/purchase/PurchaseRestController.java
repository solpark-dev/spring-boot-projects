package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;

/**
 * ì£¼ë¬¸ ê´€ë¦¬ REST API ì»¨íŠ¸ë¡¤ëŸ¬
 * JSON í˜•ì‹ìœ¼ë¡œ ë°ì´í„° ì£¼ê³ ë°›ê¸°
 */
@RestController
@RequestMapping("/purchase/json")
public class PurchaseRestController {
    
    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;
    
    /**
     * ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ
     * GET /purchase/json/{tranNo}
     */
    @GetMapping("/{tranNo}")
    public Map<String, Object> getPurchase(
            @PathVariable("tranNo") int tranNo,
            HttpSession session) throws Exception {
        
        System.out.println("==> GET /purchase/json/" + tranNo);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ì£¼ë¬¸ ì¡°íšŒ
            Purchase purchase = purchaseService.getPurchase(tranNo);
            
            if (purchase == null) {
                response.put("success", false);
                response.put("message", "ì£¼ë¬¸ì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ë³¸ì¸ì˜ ì£¼ë¬¸ì¸ì§€ í™•ì¸ (ê´€ë¦¬ìžëŠ” ëª¨ë“  ì£¼ë¬¸ ì¡°íšŒ ê°€ëŠ¥)
            if (!"admin".equals(sessionUser.getRole()) && 
                (purchase.getBuyer() == null || purchase.getBuyer().getUserNo() != sessionUser.getUserNo())) {
                response.put("success", false);
                response.put("message", "ì ‘ê·¼ ê¶Œí•œì´ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("purchase", purchase);
            
            System.out.println("==> ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ ì™„ë£Œ");
            
        } catch (Exception e) {
            System.out.println("ì£¼ë¬¸ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì£¼ë¬¸ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì£¼ë¬¸ ë“±ë¡
     * POST /purchase/json/add
     */
    @PostMapping("/add")
    public Map<String, Object> addPurchase(
            @RequestBody Purchase purchase,
            HttpSession session) throws Exception {
        
        System.out.println("==> POST /purchase/json/add");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // êµ¬ë§¤ìž ì •ë³´ ì„¤ì •
            User buyer = new User();
            buyer.setUserNo(sessionUser.getUserNo());
            purchase.setBuyer(buyer);
            
            // ì£¼ë¬¸ ë“±ë¡ (ìž¬ê³  ì°¨ê° í¬í•¨)
            purchaseService.addPurchase(purchase);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì£¼ë¬¸ì´ ì™„ë£Œë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("tranNo", purchase.getTranNo());
            
            System.out.println("==> ì£¼ë¬¸ ë“±ë¡ ì™„ë£Œ: tranNo=" + purchase.getTranNo());
            
        } catch (Exception e) {
            System.out.println("ì£¼ë¬¸ ë“±ë¡ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì£¼ë¬¸ ë“±ë¡ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì£¼ë¬¸ ì·¨ì†Œ (ìž¬ê³  ë³µêµ¬ í¬í•¨)
     * PUT /purchase/json/cancel/{tranNo}
     */
    @PutMapping("/cancel/{tranNo}")
    public Map<String, Object> cancelPurchase(
            @PathVariable("tranNo") int tranNo,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /purchase/json/cancel/" + tranNo);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ì£¼ë¬¸ ì •ë³´ ì¡°íšŒ (ê¶Œí•œ í™•ì¸)
            Purchase purchase = purchaseService.getPurchase(tranNo);
            if (purchase == null) {
                response.put("success", false);
                response.put("message", "ì£¼ë¬¸ì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ë³¸ì¸ì˜ ì£¼ë¬¸ì¸ì§€ í™•ì¸
            if (purchase.getBuyer() == null || purchase.getBuyer().getUserNo() != sessionUser.getUserNo()) {
                response.put("success", false);
                response.put("message", "ë³¸ì¸ì˜ ì£¼ë¬¸ë§Œ ì·¨ì†Œí•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì£¼ë¬¸ ì·¨ì†Œ (ìž¬ê³  ë³µêµ¬ í¬í•¨)
            purchaseService.cancelPurchase(tranNo);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì£¼ë¬¸ì´ ì·¨ì†Œë˜ì—ˆìŠµë‹ˆë‹¤. ìž¬ê³ ê°€ ë³µêµ¬ë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ì£¼ë¬¸ ì·¨ì†Œ ì™„ë£Œ: tranNo=" + tranNo);
            
        } catch (Exception e) {
            System.out.println("ì£¼ë¬¸ ì·¨ì†Œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì£¼ë¬¸ ì·¨ì†Œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì£¼ë¬¸ ìƒíƒœ ë³€ê²½ (ê´€ë¦¬ìžìš©)
     * PUT /purchase/json/updateStatus
     */
    @PutMapping("/updateStatus")
    public Map<String, Object> updateTranCode(
            @RequestBody Purchase purchase,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /purchase/json/updateStatus");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ê´€ë¦¬ìž ê¶Œí•œ í™•ì¸
            if (!"admin".equals(sessionUser.getRole())) {
                response.put("success", false);
                response.put("message", "ê´€ë¦¬ìžë§Œ ì£¼ë¬¸ ìƒíƒœë¥¼ ë³€ê²½í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì£¼ë¬¸ ìƒíƒœ ë³€ê²½
            purchaseService.updateTranCode(purchase);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì£¼ë¬¸ ìƒíƒœê°€ ë³€ê²½ë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ì£¼ë¬¸ ìƒíƒœ ë³€ê²½ ì™„ë£Œ: tranNo=" + purchase.getTranNo());
            
        } catch (Exception e) {
            System.out.println("ì£¼ë¬¸ ìƒíƒœ ë³€ê²½ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì£¼ë¬¸ ìƒíƒœ ë³€ê²½ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
}