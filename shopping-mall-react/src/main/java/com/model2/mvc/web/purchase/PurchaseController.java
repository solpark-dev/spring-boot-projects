package com.model2.mvc.web.purchase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl") 
	private ProductService productService;
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	@Value("${page.unit}")
	int pageUnit;
	
	@Value("${page.size}")
	int pageSize;
	
	/**
	 * êµ¬ë§¤ í™”ë©´ìœ¼ë¡œ ì´ë™
	 */
	@RequestMapping("/addPurchaseView")
	public ModelAndView addPurchaseView(@RequestParam("prodNo") int prodNo) throws Exception {

		System.out.println("/purchase/addPurchaseView : GET");
		
		ModelAndView modelAndView = new ModelAndView();
		Product product = productService.getProduct(prodNo);
		modelAndView.addObject("product", product);
		modelAndView.setViewName("purchase/addPurchaseView");

		return modelAndView;
	}
	
	/**
	 * êµ¬ë§¤ ì²˜ë¦¬
	 * - NEW_ORDERS ê¸°ë°˜ìœ¼ë¡œ ìˆ˜ì •
	 */
	@RequestMapping(value="/addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase,
	                                @RequestParam("prodNo") int prodNo,
	                                HttpSession session) throws Exception {

	    System.out.println("====== /purchase/addPurchase : POST ======");

	    ModelAndView modelAndView = new ModelAndView();

	    // ì„¸ì…˜ì—ì„œ êµ¬ë§¤ìž ì •ë³´ ê°€ì ¸ì˜¤ê¸°
	    User buyer = (User) session.getAttribute("user");
	    if (buyer == null) {
	        throw new Exception("ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
	    }
	    purchase.setBuyer(buyer);
	    
	    // ìƒí’ˆ ì •ë³´ ì¡°íšŒ
	    Product product = productService.getProduct(prodNo);
	    purchase.setPurchaseProd(product);
	    
	    // ì´ ê¸ˆì•¡ ê³„ì‚°
	    int totalPrice = product.getPrice();
	    purchase.setTotalPrice(totalPrice);
	    
	    // â­ addressId ì²˜ë¦¬
	    // ë°©ë²• 1: í¼ì—ì„œ addressIdë¥¼ ë°›ëŠ” ê²½ìš°
	    if (purchase.getAddressId() <= 0) {
	        // ë°©ë²• 2: ê¸°ë³¸ ë°°ì†¡ì§€ê°€ ì—†ìœ¼ë©´ ìž„ì‹œ ë°°ì†¡ì§€ ìƒì„±
	        // ì‹¤ì œë¡œëŠ” user_addresses í…Œì´ë¸”ì—ì„œ is_default='Y'ì¸ ì£¼ì†Œ ì¡°íšŒ
	        // ì—¬ê¸°ì„œëŠ” ìž„ì‹œë¡œ í•˜ë“œì½”ë”© (ë‚˜ì¤‘ì— UserServiceì— getDefaultAddress() ì¶”ê°€ í•„ìš”)
	        
	        // ìž„ì‹œ: ë°°ì†¡ì§€ ì •ë³´ë¥¼ ì§ì ‘ ìž…ë ¥ë°›ì€ ê²½ìš°
	        if (purchase.getReceiverName() != null && !purchase.getReceiverName().isEmpty()) {
	            // ìƒˆ ë°°ì†¡ì§€ ë“±ë¡ í›„ addressId ë°›ê¸° (ì¶”í›„ êµ¬í˜„)
	            // ì§€ê¸ˆì€ ìž„ì‹œë¡œ addressId=1 ì‚¬ìš©
	            purchase.setAddressId(1); // âš ï¸ ìž„ì‹œ ì²˜ë¦¬
	        } else {
	            throw new Exception("ë°°ì†¡ì§€ ì •ë³´ê°€ í•„ìš”í•©ë‹ˆë‹¤.");
	        }
	    }
	    
	    // ê¸°ë³¸ ìƒíƒœ ì½”ë“œ ì„¤ì •
	    if (purchase.getTranCode() == null || purchase.getTranCode().isEmpty()) {
	        purchase.setTranCode("PAYMENT_COMPLETE");
	    }
	    
	    System.out.println("êµ¬ë§¤ìž: " + buyer.getUserId());
	    System.out.println("ë°°ì†¡ì§€ ID: " + purchase.getAddressId());
	    System.out.println("ìƒí’ˆë²ˆí˜¸: " + prodNo);
	    System.out.println("ì´ ê¸ˆì•¡: " + totalPrice);
	    
	    try {
	        // ì£¼ë¬¸ ë“±ë¡ (íŠ¸ëžœìž­ì…˜ìœ¼ë¡œ 3ê°œ í…Œì´ë¸” ì €ìž¥)
	        purchaseService.addPurchase(purchase);
	        
	        System.out.println("âœ… ì£¼ë¬¸ ì™„ë£Œ! orderNo: " + purchase.getTranNo());
	        
	        if (purchase.getTranNo() > 0) {
	            modelAndView.setViewName("redirect:/purchase/getPurchase?tranNo=" + purchase.getTranNo());
	        } else {
	            System.out.println("âŒ tranNoê°€ 0ìž…ë‹ˆë‹¤! DB í™•ì¸ í•„ìš”");
	            modelAndView.setViewName("redirect:/purchase/listPurchase");
	        }
	        
	    } catch (Exception e) {
	        System.out.println("âŒ ì£¼ë¬¸ ë“±ë¡ ì‹¤íŒ¨!");
	        e.printStackTrace();
	        modelAndView.addObject("errorMessage", "ì£¼ë¬¸ ì²˜ë¦¬ ì¤‘ ì˜¤ë¥˜ê°€ ë°œìƒí–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
	        modelAndView.setViewName("common/error");
	    }
	    
	    return modelAndView;
	}
	
	/**
	 * ì£¼ë¬¸ ìƒì„¸ ì¡°íšŒ
	 */
	@RequestMapping("/getPurchase")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception {

		System.out.println("/purchase/getPurchase : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("purchase/getPurchase");

		return modelAndView;
	}
	
	/**
	 * êµ¬ë§¤ ëª©ë¡ ì¡°íšŒ
	 */
	@RequestMapping("/listPurchase")
	public String listPurchase(@ModelAttribute("search") Search search,
	                           HttpSession session,
	                           Model model) throws Exception {

		System.out.println("/purchase/listPurchase : GET/POST");
		
		User user = (User) session.getAttribute("user");
		if (user == null) {
			throw new Exception("ë¡œê·¸ì¸ì´ í•„ìš”í•©ë‹ˆë‹¤.");
		}
		
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// íŽ˜ì´ì§• ê³„ì‚°
		int startRowNum = (search.getCurrentPage() - 1) * search.getPageSize() + 1;
		int endRowNum = search.getCurrentPage() * search.getPageSize();
		search.setStartRowNum(startRowNum);
		search.setEndRowNum(endRowNum);
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page(
			search.getCurrentPage(), 
			((Integer)map.get("totalCount")).intValue(), 
			pageUnit, 
			pageSize
		);
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "purchase/listPurchase";
	}
	
	/**
	 * ë°°ì†¡ì§€ ì •ë³´ ìˆ˜ì • í™”ë©´
	 */
	@RequestMapping("/updatePurchaseView")
	public ModelAndView updatePurchaseView(@RequestParam("tranNo") int tranNo) throws Exception {

		System.out.println("/purchase/updatePurchaseView : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("purchase/updatePurchaseView");

		return modelAndView;
	}
	
	/**
	 * ë°°ì†¡ì§€ ì •ë³´ ìˆ˜ì • ì²˜ë¦¬
	 */
	@RequestMapping(value="/updatePurchase", method=RequestMethod.POST)
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase) throws Exception {

		System.out.println("/purchase/updatePurchase : POST");
		
		purchaseService.updatePurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/getPurchase?tranNo=" + purchase.getTranNo());

		return modelAndView;
	}
	
	/**
	 * ì£¼ë¬¸ ìƒíƒœ ë³€ê²½ (ë°°ì†¡ ì¤‘ìœ¼ë¡œ ë³€ê²½)
	 */
	@RequestMapping("/updateTranCode")
	public ModelAndView updateTranCode(@RequestParam("tranNo") int tranNo,
	                                   @RequestParam("tranCode") String tranCode) throws Exception {

		System.out.println("/purchase/updateTranCode : GET");
		System.out.println("tranNo: " + tranNo + ", tranCode: " + tranCode);
		
		Purchase purchase = new Purchase();
		purchase.setTranNo(tranNo);
		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/getPurchase?tranNo=" + tranNo);

		return modelAndView;
	}
}