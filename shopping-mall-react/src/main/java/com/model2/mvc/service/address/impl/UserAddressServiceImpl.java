package com.model2.mvc.service.address.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.service.address.UserAddressDao;
import com.model2.mvc.service.address.UserAddressService;
import com.model2.mvc.service.domain.UserAddress;

/**
 * ë°°ì†¡ì§€ Service êµ¬í˜„ì²´
 * ë°°ì†¡ì§€ ê´€ë ¨ ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì²˜ë¦¬
 */
@Service("userAddressServiceImpl")
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    @Qualifier("userAddressDaoImpl")
    private UserAddressDao userAddressDao;
    
    public void setUserAddressDao(UserAddressDao userAddressDao) {
        this.userAddressDao = userAddressDao;
    }

    public UserAddressServiceImpl() {
        System.out.println(this.getClass());
    }
    
    /**
     * ë°°ì†¡ì§€ ì¶”ê°€
     */
    @Override
    @Transactional
    public void addAddress(UserAddress address) throws Exception {
        System.out.println("==> UserAddressServiceImpl.addAddress() ì‹œìž‘");
        
        // 1. ìž…ë ¥ê°’ ê²€ì¦
        if (address == null) {
            throw new IllegalArgumentException("UserAddress ê°ì²´ê°€ nullìž…ë‹ˆë‹¤.");
        }
        
        if (address.getUserNo() <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ì‚¬ìš©ìž ë²ˆí˜¸ìž…ë‹ˆë‹¤.");
        }
        
        if (address.getReceiverName() == null || address.getReceiverName().trim().isEmpty()) {
            throw new IllegalArgumentException("ìˆ˜ë ¹ì¸ ì´ë¦„ì€ í•„ìˆ˜ìž…ë‹ˆë‹¤.");
        }
        
        if (address.getAddress() == null || address.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("ì£¼ì†ŒëŠ” í•„ìˆ˜ìž…ë‹ˆë‹¤.");
        }
        
        if (address.getPhoneNumber() == null || address.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("ì „í™”ë²ˆí˜¸ëŠ” í•„ìˆ˜ìž…ë‹ˆë‹¤.");
        }
        
        // 2. ê¸°ë³¸ ë°°ì†¡ì§€ë¡œ ì„¤ì •ëœ ê²½ìš° ê¸°ì¡´ ê¸°ë³¸ ë°°ì†¡ì§€ í•´ì œ
        if ("Y".equals(address.getIsDefault())) {
            userAddressDao.clearDefaultAddress(address.getUserNo());
        }
        
        // 3. ë°°ì†¡ì§€ ë“±ë¡
        userAddressDao.addAddress(address);
        System.out.println("==> ë°°ì†¡ì§€ ë“±ë¡ ì™„ë£Œ: " + address.getAddressAlias());
    }
    
    /**
     * ë°°ì†¡ì§€ ì¡°íšŒ (ë‹¨ê±´)
     */
    @Override
    public UserAddress getAddress(int addressId) throws Exception {
        System.out.println("==> UserAddressServiceImpl.getAddress() ì‹œìž‘, addressId: " + addressId);
        
        if (addressId <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ë°°ì†¡ì§€ IDìž…ë‹ˆë‹¤: " + addressId);
        }
        
        UserAddress address = userAddressDao.getAddress(addressId);
        
        if (address == null) {
            System.out.println("==> ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤: " + addressId);
        }
        
        return address;
    }
    
    /**
     * ì‚¬ìš©ìžë³„ ë°°ì†¡ì§€ ëª©ë¡ ì¡°íšŒ
     */
    @Override
    public List<UserAddress> getAddressListByUserNo(int userNo) throws Exception {
        System.out.println("==> UserAddressServiceImpl.getAddressListByUserNo() ì‹œìž‘, userNo: " + userNo);
        
        if (userNo <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ì‚¬ìš©ìž ë²ˆí˜¸ìž…ë‹ˆë‹¤: " + userNo);
        }
        
        List<UserAddress> addressList = userAddressDao.getAddressListByUserNo(userNo);
        
        System.out.println("==> ì¡°íšŒ ì™„ë£Œ, ë°°ì†¡ì§€ ê°œìˆ˜: " + 
                         (addressList != null ? addressList.size() : 0));
        return addressList;
    }
    
    /**
     * ë°°ì†¡ì§€ ìˆ˜ì •
     */
    @Override
    @Transactional
    public void updateAddress(UserAddress address) throws Exception {
        System.out.println("==> UserAddressServiceImpl.updateAddress() ì‹œìž‘");
        
        // 1. ìž…ë ¥ê°’ ê²€ì¦
        if (address == null) {
            throw new IllegalArgumentException("UserAddress ê°ì²´ê°€ nullìž…ë‹ˆë‹¤.");
        }
        
        if (address.getAddressId() <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ë°°ì†¡ì§€ IDìž…ë‹ˆë‹¤: " + address.getAddressId());
        }
        
        // 2. ë°°ì†¡ì§€ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
        UserAddress existingAddress = userAddressDao.getAddress(address.getAddressId());
        if (existingAddress == null) {
            throw new Exception("í•´ë‹¹ ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. addressId: " + address.getAddressId());
        }
        
        // 3. ê¸°ë³¸ ë°°ì†¡ì§€ë¡œ ì„¤ì •ëœ ê²½ìš° ê¸°ì¡´ ê¸°ë³¸ ë°°ì†¡ì§€ í•´ì œ
        if ("Y".equals(address.getIsDefault())) {
            userAddressDao.clearDefaultAddress(address.getUserNo());
        }
        
        // 4. ë°°ì†¡ì§€ ìˆ˜ì •
        userAddressDao.updateAddress(address);
        System.out.println("==> ë°°ì†¡ì§€ ìˆ˜ì • ì™„ë£Œ");
    }
    
    /**
     * ë°°ì†¡ì§€ ì‚­ì œ
     */
    @Override
    @Transactional
    public void deleteAddress(int addressId) throws Exception {
        System.out.println("==> UserAddressServiceImpl.deleteAddress() ì‹œìž‘, addressId: " + addressId);
        
        if (addressId <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ë°°ì†¡ì§€ IDìž…ë‹ˆë‹¤: " + addressId);
        }
        
        // ë°°ì†¡ì§€ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
        UserAddress address = userAddressDao.getAddress(addressId);
        if (address == null) {
            throw new Exception("í•´ë‹¹ ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. addressId: " + addressId);
        }
        
        // TODO: í•´ë‹¹ ë°°ì†¡ì§€ë¥¼ ì‚¬ìš©í•˜ëŠ” ì£¼ë¬¸ì´ ìžˆëŠ”ì§€ í™•ì¸ (í•„ìš”ì‹œ)
        
        userAddressDao.deleteAddress(addressId);
        System.out.println("==> ë°°ì†¡ì§€ ì‚­ì œ ì™„ë£Œ");
    }
    
    /**
     * ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì •
     */
    @Override
    @Transactional
    public void setDefaultAddress(int userNo, int addressId) throws Exception {
        System.out.println("==> UserAddressServiceImpl.setDefaultAddress() ì‹œìž‘");
        System.out.println("    userNo: " + userNo + ", addressId: " + addressId);
        
        // 1. ìž…ë ¥ê°’ ê²€ì¦
        if (userNo <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ì‚¬ìš©ìž ë²ˆí˜¸ìž…ë‹ˆë‹¤: " + userNo);
        }
        
        if (addressId <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ë°°ì†¡ì§€ IDìž…ë‹ˆë‹¤: " + addressId);
        }
        
        // 2. ë°°ì†¡ì§€ ì¡´ìž¬ ì—¬ë¶€ ë° ì†Œìœ ìž í™•ì¸
        UserAddress address = userAddressDao.getAddress(addressId);
        if (address == null) {
            throw new Exception("í•´ë‹¹ ë°°ì†¡ì§€ë¥¼ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. addressId: " + addressId);
        }
        
        if (address.getUserNo() != userNo) {
            throw new Exception("ë³¸ì¸ì˜ ë°°ì†¡ì§€ë§Œ ê¸°ë³¸ ë°°ì†¡ì§€ë¡œ ì„¤ì •í•  ìˆ˜ ìžˆìŠµë‹ˆë‹¤.");
        }
        
        // 3. ê¸°ì¡´ ê¸°ë³¸ ë°°ì†¡ì§€ í•´ì œ
        userAddressDao.clearDefaultAddress(userNo);
        
        // 4. ìƒˆ ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì •
        userAddressDao.setDefaultAddress(userNo, addressId);
        
        System.out.println("==> ê¸°ë³¸ ë°°ì†¡ì§€ ì„¤ì • ì™„ë£Œ");
    }
    
    /**
     * ì‚¬ìš©ìžì˜ ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒ
     */
    @Override
    public UserAddress getDefaultAddress(int userNo) throws Exception {
        System.out.println("==> UserAddressServiceImpl.getDefaultAddress() ì‹œìž‘, userNo: " + userNo);
        
        if (userNo <= 0) {
            throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ì‚¬ìš©ìž ë²ˆí˜¸ìž…ë‹ˆë‹¤: " + userNo);
        }
        
        UserAddress address = userAddressDao.getDefaultAddress(userNo);
        
        System.out.println("==> ê¸°ë³¸ ë°°ì†¡ì§€ ì¡°íšŒ ê²°ê³¼: " + (address != null ? "ì¡´ìž¬" : "ì—†ìŒ"));
        
        return address;
    }
}