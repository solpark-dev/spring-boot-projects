package com.model2.mvc.web.user;

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

import com.model2.mvc.service.address.UserAddressService;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.domain.UserAddress;

/**
 * ë°°ì†¡ì§€ ê´€ë¦¬ REST API ì»¨íŠ¸ë¡¤ëŸ¬
 * JSON í˜•ì‹ìœ¼ë¡œ ë°ì´í„° ì£¼ê³ ë°›ê¸°
 */
@RestController
@RequestMapping("/address/json")
public class AddressRestController {
    
    @Autowired
    @Qualifier("userAddressServiceImpl")
    private UserAddressService userAddressService;
    
    /**
     * ë°°ì†¡ì§€ ëª©ë¡ ì¡°íšŒ
     * GET /address/json/list
     */
    @GetMapping("/list")
    public Map<String, Object> getAddressList(HttpSession session) throws Exception {
        
        System.out.println("==> GET /address/json/list");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ë°°ì†¡ì§€ ëª©ë¡ ì¡°íšŒ
            List<UserAddress> addressList = userAddressService.getAddressListByUserNo(sessionUser.getUserNo());
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("addressList", addressList);
            response.put("totalCount", addressList != null ? addressList.size() : 0);
            
            System.out.println("==> ë°°ì†¡ì§€ ì¡°íšŒ ì™„ë£Œ: " + (addressList != null ? addressList.size() : 0) + "ê°œ");
            
        } catch (Exception e) {
            System.out.println("ë°°ì†¡ì§€ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ë°°ì†¡ì§€ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ë°°ì†¡ì§€ ìƒì„¸ ì¡°íšŒ
     * GET /address/json/{addressId}
     */
    @GetMapping("/{addressId}")
    public Map<String, Object> getAddress(
            @PathVariable("addressId") int addressId,
            HttpSession session) throws Exception {
        
        System.out.println("==> GET /address/json/" + addressId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ë°°ì†¡ì§€ ì¡°íšŒ
            UserAddress address = userAddressService.getAddress(addressId);
            
            if (address == null) {
                response.put("success", false);
                response.put("message", "ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ë³¸ì¸ì˜ ë°°ì†¡ì§€ì¸ì§€ í™•ì¸
            if (address.getUserNo() != sessionUser.getUserNo()) {
                response.put("success", false);
                response.put("message", "ì ‘ê·¼ ê¶Œí•œì´ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("address", address);
            
            System.out.println("==> ë°°ì†¡ì§€ ìƒì„¸ ì¡°íšŒ ì™„ë£Œ");
            
        } catch (Exception e) {
            System.out.println("ë°°ì†¡ì§€ ìƒì„¸ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ë°°ì†¡ì§€ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒ
     * GET /address/json/default
     */
    @GetMapping("/default")
    public Map<String, Object> getDefaultAddress(HttpSession session) throws Exception {
        
        System.out.println("==> GET /address/json/default");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒ
            UserAddress address = userAddressService.getDefaultAddress(sessionUser.getUserNo());
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("address", address);
            response.put("hasDefault", address != null);
            
            System.out.println("==> ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒ ì™„ë£Œ: " + (address != null ? "ì¡´ìž¬" : "ì—†ìŒ"));
            
        } catch (Exception e) {
            System.out.println("ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ë°°ì†¡ì§€ ì¶”ê°€
     * POST /address/json/add
     */
    @PostMapping("/add")
    public Map<String, Object> addAddress(
            @RequestBody UserAddress address,
            HttpSession session) throws Exception {
        
        System.out.println("==> POST /address/json/add");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // userNo ì„¤ì •
            address.setUserNo(sessionUser.getUserNo());
            
            // isDefaultê°€ nullì´ë©´ 'N'ìœ¼ë¡œ ì„¤ì •
            if (address.getIsDefault() == null || address.getIsDefault().trim().isEmpty()) {
                address.setIsDefault("N");
            }
            
            // ë°°ì†¡ì§€ ì¶”ê°€
            userAddressService.addAddress(address);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ë°°ì†¡ì§€ê°€ ì¶”ê°€ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("addressId", address.getAddressId());
            
            System.out.println("==> ë°°ì†¡ì§€ ì¶”ê°€ ì™„ë£Œ: addressId=" + address.getAddressId());
            
        } catch (Exception e) {
            System.out.println("ë°°ì†¡ì§€ ì¶”ê°€ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ë°°ì†¡ì§€ ì¶”ê°€ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ë°°ì†¡ì§€ ìˆ˜ì •
     * PUT /address/json/update
     */
    @PutMapping("/update")
    public Map<String, Object> updateAddress(
            @RequestBody UserAddress address,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /address/json/update");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ê¸°ì¡´ ë°°ì†¡ì§€ ì¡°íšŒ (ì†Œìœ ìž í™•ì¸)
            UserAddress existingAddress = userAddressService.getAddress(address.getAddressId());
            if (existingAddress == null) {
                response.put("success", false);
                response.put("message", "ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            if (existingAddress.getUserNo() != sessionUser.getUserNo()) {
                response.put("success", false);
                response.put("message", "ë³¸ì¸ì˜ ë°°ì†¡ì§€ë§Œ ìˆ˜ì •í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // userNo ì„¤ì •
            address.setUserNo(sessionUser.getUserNo());
            
            // ë°°ì†¡ì§€ ìˆ˜ì •
            userAddressService.updateAddress(address);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ë°°ì†¡ì§€ê°€ ìˆ˜ì •ë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ë°°ì†¡ì§€ ìˆ˜ì • ì™„ë£Œ: addressId=" + address.getAddressId());
            
        } catch (Exception e) {
            System.out.println("ë°°ì†¡ì§€ ìˆ˜ì • ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ë°°ì†¡ì§€ ìˆ˜ì •ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ë°°ì†¡ì§€ ì‚­ì œ
     * DELETE /address/json/delete/{addressId}
     */
    @DeleteMapping("/delete/{addressId}")
    public Map<String, Object> deleteAddress(
            @PathVariable("addressId") int addressId,
            HttpSession session) throws Exception {
        
        System.out.println("==> DELETE /address/json/delete/" + addressId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ê¸°ì¡´ ë°°ì†¡ì§€ ì¡°íšŒ (ì†Œìœ ìž í™•ì¸)
            UserAddress existingAddress = userAddressService.getAddress(addressId);
            if (existingAddress == null) {
                response.put("success", false);
                response.put("message", "ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return response;
            }
            
            if (existingAddress.getUserNo() != sessionUser.getUserNo()) {
                response.put("success", false);
                response.put("message", "ë³¸ì¸ì˜ ë°°ì†¡ì§€ë§Œ ì‚­ì œí•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
                return response;
            }
            
            // ë°°ì†¡ì§€ ì‚­ì œ
            userAddressService.deleteAddress(addressId);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ë°°ì†¡ì§€ê°€ ì‚­ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ë°°ì†¡ì§€ ì‚­ì œ ì™„ë£Œ: addressId=" + addressId);
            
        } catch (Exception e) {
            System.out.println("ë°°ì†¡ì§€ ì‚­ì œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ë°°ì†¡ì§€ ì‚­ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì •
     * PUT /address/json/setDefault/{addressId}
     */
    @PutMapping("/setDefault/{addressId}")
    public Map<String, Object> setDefaultAddress(
            @PathVariable("addressId") int addressId,
            HttpSession session) throws Exception {
        
        System.out.println("==> PUT /address/json/setDefault/" + addressId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ì„¸ì…˜ì—ì„œ ì‚¬ìš©ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
            User sessionUser = (User) session.getAttribute("user");
            if (sessionUser == null) {
                response.put("success", false);
                response.put("message", "ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
                return response;
            }
            
            // ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì •
            userAddressService.setDefaultAddress(sessionUser.getUserNo(), addressId);
            
            // ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ê¸°ë³¸ ë°°ì†¡ì§€ë¡œ ì„¤ì •ë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            System.out.println("==> ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì • ì™„ë£Œ: addressId=" + addressId);
            
        } catch (Exception e) {
            System.out.println("ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì • ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì •ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
        }
        
        return response;
    }
}