package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;  // âœ… ì¶”ê°€
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;

	// productDao ì£¼ìž… ì¶”ê°€ (ìƒí’ˆ ê°€ê²© ì¡°íšŒë¥¼ ìœ„í•´)
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDao;
	
	// âœ… ì¶”ê°€: productService ì£¼ìž… (ìž¬ê³  ê´€ë¦¬ë¥¼ ìœ„í•´)
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	public PurchaseServiceImpl() {
		System.out.println(this.getClass());
	}
	
	/**
	 * ì£¼ë¬¸ ë“±ë¡ (ë¡œì§ ì „ì²´ ìˆ˜ì •)
	 * 1. ìƒí’ˆ ì •ë³´(ê°€ê²©) DBì—ì„œ ìž¬ì¡°íšŒ
	 * 2. ìž¬ê³  ì°¨ê°
	 * 3. ì‹ ê·œ ë°°ì†¡ì§€ ì •ë³´ user_addresses í…Œì´ë¸”ì— INSERT
	 * 4. ìƒì„±ëœ address_idë¥¼ ì‚¬ìš©í•˜ì—¬ new_orders í…Œì´ë¸”ì— INSERT
	 * 5. new_order_details, new_payments í…Œì´ë¸”ì— ì •ë³´ INSERT
	 */
	@Override
	@Transactional
	public void addPurchase(Purchase purchase) throws Exception {
	    System.out.println("==> PurchaseServiceImpl.addPurchase() ì‹œìž‘");
	    
	    // 1. [ë³´ì•ˆ] ìƒí’ˆ ì •ë³´ë¥¼ DBì—ì„œ ë‹¤ì‹œ ì¡°íšŒí•˜ì—¬ ê°€ê²©ì„ í™•ì •
	    Product product = productDao.getProduct(purchase.getPurchaseProd().getProdNo());
	    int price = product.getPrice();
	    
	    // 2. [ì‹ ê·œ] ìž¬ê³  ì°¨ê° (ì£¼ë¬¸ ì „ì— ìž¬ê³  í™•ì¸ ë° ì°¨ê°)
	    int quantity = 1; // í˜„ìž¬ëŠ” 1ê°œë§Œ ì£¼ë¬¸ ê°€ëŠ¥
	    productService.decreaseStock(product.getProdNo(), quantity);
	    System.out.println("==> ìž¬ê³  ì°¨ê° ì™„ë£Œ");
	    
	    // 3. [í•µì‹¬] í´ë¼ì´ì–¸íŠ¸ë¡œë¶€í„° ë°›ì€ ë°°ì†¡ì§€ ì •ë³´ë¥¼ user_addresses í…Œì´ë¸”ì— ë¨¼ì € INSERT
	    purchaseDao.addAddress(purchase); // ì´ í˜¸ì¶œ í›„ purchase ê°ì²´ì˜ addressId í•„ë“œì— ìƒì„±ëœ í‚¤ê°’ì´ ë‹´ê¹€
	    
	    // 4. ì£¼ë¬¸ ì •ë³´ ì„¤ì •
	    purchase.setTotalPrice(price); // DBì—ì„œ ì¡°íšŒí•œ ì •í™•í•œ ê°€ê²©ìœ¼ë¡œ ì„¤ì •
	    if (purchase.getTranCode() == null || purchase.getTranCode().trim().isEmpty()) {
	        purchase.setTranCode("PAYMENT_COMPLETE"); // ê¸°ë³¸ ìƒíƒœ ì„¤ì •
	    }

	    // 5. NEW_ORDERS í…Œì´ë¸”ì— ì£¼ë¬¸ ì •ë³´ INSERT
	    purchaseDao.addPurchase(purchase);
	    int orderNo = purchase.getTranNo();
	    System.out.println("==> ì£¼ë¬¸ ë“±ë¡ ì™„ë£Œ, orderNo: " + orderNo);
	    
	    // 6. NEW_ORDER_DETAILS í…Œì´ë¸”ì— ì£¼ë¬¸ ìƒì„¸ ì •ë³´ INSERT
	    purchaseDao.addOrderDetail(orderNo, product.getProdNo(), quantity, price);
	    
	    // 7. NEW_PAYMENTS í…Œì´ë¸”ì— ê²°ì œ ì •ë³´ INSERT
	    purchaseDao.addPayment(orderNo, purchase.getPaymentOption(), price);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		return purchaseDao.getPurchase(tranNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		List<Purchase> list = purchaseDao.getPurchaseList(search, buyerId);
		int totalCount = purchaseDao.getTotalCount(buyerId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		purchaseDao.updatePurchase(purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		// (ê¸°ì¡´ ì½”ë“œì™€ ë™ì¼)
		String tranCode = purchase.getTranCode();
		String newTranCode = convertTranCode(tranCode);
		purchase.setTranCode(newTranCode);
		purchaseDao.updateTranCode(purchase);
	}
	
	private String convertTranCode(String oldCode) {
		// (ê¸°ì¡´ ì½”ë“œì™€ ë™ì¼)
		if (oldCode == null) return "PAYMENT_COMPLETE";
		if (oldCode.contains("_")) return oldCode;
		switch (oldCode.trim()) {
			case "1": return "PAYMENT_COMPLETE";
			case "2": return "SHIPPED";
			case "3": return "DELIVERED";
			default: return oldCode;
		}
	}
	
	@Override
	public Purchase getPurchaseByProdNo(int prodNo) throws Exception {
	    return purchaseDao.getPurchaseByProdNo(prodNo);
	}
	
	/**
	 * ì£¼ë¬¸ ì·¨ì†Œ (ìž¬ê³  ë³µêµ¬ í¬í•¨)
	 */
	@Override
	@Transactional
	public void cancelPurchase(int tranNo) throws Exception {
	    System.out.println("==> PurchaseServiceImpl.cancelPurchase() ì‹œìž‘, tranNo: " + tranNo);
	    
	    // 1. ì£¼ë¬¸ ì •ë³´ ì¡°íšŒ
	    Purchase purchase = purchaseDao.getPurchase(tranNo);
	    if (purchase == null) {
	        throw new Exception("ì£¼ë¬¸ì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. tranNo: " + tranNo);
	    }
	    
	    // 2. ì´ë¯¸ ì·¨ì†Œëœ ì£¼ë¬¸ì¸ì§€ í™•ì¸
	    if ("CANCELLED".equals(purchase.getTranCode())) {
	        throw new Exception("ì´ë¯¸ ì·¨ì†Œëœ ì£¼ë¬¸ìž…ë‹ˆë‹¤.");
	    }
	    
	    // 3. ë°°ì†¡ ì™„ë£Œëœ ì£¼ë¬¸ì€ ì·¨ì†Œ ë¶ˆê°€
	    if ("DELIVERED".equals(purchase.getTranCode())) {
	        throw new Exception("ë°°ì†¡ ì™„ë£Œëœ ì£¼ë¬¸ì€ ì·¨ì†Œí•  ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
	    }
	    
	    // 4. ìž¬ê³  ë³µêµ¬
	    if (purchase.getPurchaseProd() != null) {
	        int prodNo = purchase.getPurchaseProd().getProdNo();
	        int quantity = 1; // í˜„ìž¬ëŠ” 1ê°œë§Œ ì£¼ë¬¸ ê°€ëŠ¥
	        
	        productService.increaseStock(prodNo, quantity);
	        System.out.println("==> ìž¬ê³  ë³µêµ¬ ì™„ë£Œ: prodNo=" + prodNo + ", quantity=" + quantity);
	    }
	    
	    // 5. ì£¼ë¬¸ ìƒíƒœë¥¼ CANCELLEDë¡œ ë³€ê²½
	    purchase.setTranCode("CANCELLED");
	    purchaseDao.updateTranCode(purchase);
	    
	    System.out.println("==> ì£¼ë¬¸ ì·¨ì†Œ ì™„ë£Œ");
	}
}