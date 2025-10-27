package com.model2.mvc.web.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.service.category.CategoryService;
import com.model2.mvc.service.domain.Category;
import com.model2.mvc.service.domain.User;

/**
 * ì¹´í…Œê³ ë¦¬ ê´€ë¦¬ REST API ì»¨íŠ¸ë¡¤ëŸ¬
 * JSON í˜•ì‹ìœ¼ë¡œ ë°ì´í„° ì£¼ê³ ë°›ê¸°
 */
@RestController
@RequestMapping("/category/json")
public class CategoryRestController {
    
    @Autowired
    @Qualifier("categoryServiceImpl")
    private CategoryService categoryService;
    
    /**
     * ì¹´í…Œê³ ë¦¬ ì „ì²´ ëª©ë¡ ì¡°íšŒ
     * GET /category/json/list
     */
    @GetMapping("/list")
    public Map<String, Object> getCategoryList() throws Exception {
        
        System.out.println("==> GET /category/json/list");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì¹´í…Œê³ ë¦¬ ëª©ë¡ ì¡°íšŒ
            List<Category> categoryList = categoryService.getCategoryList();
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("categoryList", categoryList);
            response.put("totalCount", categoryList != null ? categoryList.size() : 0);
            
            System.out.println("==> ì¹´í…Œê³ ë¦¬ ì¡°íšŒ ì™„ë£Œ: " + (categoryList != null ? categoryList.size() : 0) + "ê°œ");
            
        } catch (Exception e) {
            System.out.println("ì¹´í…Œê³ ë¦¬ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì¹´í…Œê³ ë¦¬ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì¹´í…Œê³ ë¦¬ ìƒì„¸ ì¡°íšŒ
     * GET /category/json/{categoryId}
     */
    @GetMapping("/{categoryId}")
    public Map<String, Object> getCategory(@PathVariable("categoryId") int categoryId) throws Exception {
        
        System.out.println("==> GET /category/json/" + categoryId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì¹´í…Œê³ ë¦¬ ì¡°íšŒ
            Category category = categoryService.getCategory(categoryId);
            
            if (category == null) {
                response.put("success", false);
                response.put("message", "ì¹´í…Œê³ ë¦¬ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("category", category);
            
            System.out.println("==> ì¹´í…Œê³ ë¦¬ ìƒì„¸ ì¡°íšŒ ì™„ë£Œ");
            
        } catch (Exception e) {
            System.out.println("ì¹´í…Œê³ ë¦¬ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì¹´í…Œê³ ë¦¬ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì¹´í…Œê³ ë¦¬ ì¶”ê°€ (ê´€ë¦¬ìž ì „ìš©)
     * POST /category/json/add
     */
    @PostMapping("/add")
    public Map<String, Object> addCategory(
            @RequestBody Category category,
            HttpSession session) throws Exception {
        
        System.out.println("==> POST /category/json/add");
        
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
                response.put("message", "ê´€ë¦¬ìžë§Œ ì¹´í…Œê³ ë¦¬ë¥¼ ì¶”ê°€í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì¹´í…Œê³ ë¦¬ ì¶”ê°€
            categoryService.addCategory(category);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì¹´í…Œê³ ë¦¬ê°€ ì¶”ê°€ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("categoryId", category.getCategoryId());
            
            System.out.println("==> ì¹´í…Œê³ ë¦¬ ì¶”ê°€ ì™„ë£Œ: categoryId=" + category.getCategoryId());
            
        } catch (Exception e) {
            System.out.println("ì¹´í…Œê³ ë¦¬ ì¶”ê°€ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì¹´í…Œê³ ë¦¬ ì¶”ê°€ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì¹´í…Œê³ ë¦¬ ìˆ˜ì • (ê´€ë¦¬ìž ì „ìš©)
     * PUT /category/json/update
     */
    @PutMapping("/update")
    public Map<String, Object> updateCategory(
            @RequestBody Category category,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /category/json/update");
        
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
                response.put("message", "ê´€ë¦¬ìžë§Œ ì¹´í…Œê³ ë¦¬ë¥¼ ìˆ˜ì •í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì¹´í…Œê³ ë¦¬ ìˆ˜ì •
            categoryService.updateCategory(category);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì¹´í…Œê³ ë¦¬ê°€ ìˆ˜ì •ë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ì¹´í…Œê³ ë¦¬ ìˆ˜ì • ì™„ë£Œ: categoryId=" + category.getCategoryId());
            
        } catch (Exception e) {
            System.out.println("ì¹´í…Œê³ ë¦¬ ìˆ˜ì • ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì¹´í…Œê³ ë¦¬ ìˆ˜ì •ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ì¹´í…Œê³ ë¦¬ ì‚­ì œ (ê´€ë¦¬ìž ì „ìš©)
     * DELETE /category/json/delete/{categoryId}
     */
    @DeleteMapping("/delete/{categoryId}")
    public Map<String, Object> deleteCategory(
            @PathVariable("categoryId") int categoryId,
            HttpSession session) throws Exception {
        
        System.out.println("==> DELETE /category/json/delete/" + categoryId);
        
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
                response.put("message", "ê´€ë¦¬ìžë§Œ ì¹´í…Œê³ ë¦¬ë¥¼ ì‚­ì œí•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì¹´í…Œê³ ë¦¬ ì‚­ì œ
            categoryService.deleteCategory(categoryId);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ì¹´í…Œê³ ë¦¬ê°€ ì‚­ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ì¹´í…Œê³ ë¦¬ ì‚­ì œ ì™„ë£Œ: categoryId=" + categoryId);
            
        } catch (Exception e) {
            System.out.println("ì¹´í…Œê³ ë¦¬ ì‚­ì œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ì¹´í…Œê³ ë¦¬ ì‚­ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
}