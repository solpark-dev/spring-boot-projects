package com.model2.mvc.web.product;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.ProductFile;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	@Value("${page.unit}")
	int pageUnit;
	
	@Value("${page.size}")
	int pageSize;
	
	// â­ï¸ 1. common.properties íŒŒì¼ì˜ ê²½ë¡œë¥¼ ì£¼ìž…ë°›ì„ í•„ë“œ
	@Value("${product.upload.path}")
	private String uploadPath;
	
	/**
	 * ìƒí’ˆ ë“±ë¡ í™”ë©´ìœ¼ë¡œ ì´ë™
	 */
	@RequestMapping("/addProductView")
	public String addProductView() throws Exception {
	    System.out.println("/product/addProductView : GET");
	    return "product/addProductView";
	}
	
	/**
	 * ìƒí’ˆ ë“±ë¡ ì²˜ë¦¬ (íŒŒì¼ ì—…ë¡œë“œ í¬í•¨)
	 */
	@RequestMapping("/addProduct")
	public String addProduct(@ModelAttribute("product") Product product, 
	                         @RequestParam(value="uploadFiles", required=false) List<MultipartFile> uploadFiles,
	                         Model model, HttpSession session) throws Exception {
	    
	    System.out.println("/product/addProduct : POST");
	    
	    productService.addProduct(product);
	    System.out.println("DBì— ìƒí’ˆ ì •ë³´ ì €ìž¥ ì™„ë£Œ, ìƒí’ˆë²ˆí˜¸: " + product.getProdNo());

	    // â­ï¸ 2. íŒŒì¼ ì—…ë¡œë“œ ì²˜ë¦¬ (ì£¼ìž…ë°›ì€ uploadPath ì‚¬ìš©)
	    if (uploadFiles != null && !uploadFiles.isEmpty()) {
	        System.out.println("ì—…ë¡œë“œí•  íŒŒì¼ ê°œìˆ˜: " + uploadFiles.size());
	        
	        // ì§€ì •ëœ ì™¸ë¶€ ê²½ë¡œì— ë””ë ‰í† ë¦¬ ìƒì„±
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdirs();
	            System.out.println("ì—…ë¡œë“œ ë””ë ‰í† ë¦¬ ìƒì„±: " + uploadPath);
	        }
	        
	        for (MultipartFile file : uploadFiles) {
	            if (!file.isEmpty()) {
	                String originalFileName = file.getOriginalFilename();
	                String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("\\s", "_");

	                File targetFile = new File(uploadPath, savedFileName);
	                file.transferTo(targetFile);

	                ProductFile productFile = new ProductFile();
	                productFile.setProdNo(product.getProdNo());
	                productFile.setOriginalName(originalFileName);
	                productFile.setSavedName(savedFileName);
	                productFile.setFileSize(file.getSize());
	                productFile.setFileType(file.getContentType());
	                
	                productService.addProductFile(productFile);
	                System.out.println("íŒŒì¼ ì €ìž¥ ë° DB ê¸°ë¡ ì™„ë£Œ: " + originalFileName);
	            }
	        }
	    } else {
	        System.out.println("ì—…ë¡œë“œí•  íŒŒì¼ì´ ì—†ìŠµë‹ˆë‹¤.");
	    }

	    Product registeredProduct = productService.getProduct(product.getProdNo());
	    model.addAttribute("product", registeredProduct);
	    model.addAttribute("message", "ìƒí’ˆ ë“±ë¡ì´ ì™„ë£Œë˜ì—ˆìŠµë‹ˆë‹¤.");
	    
	    return "product/addProductView";
	}
	
	/**
	 * ìƒí’ˆ ìƒì„¸ ì¡°íšŒ
	 */
	@RequestMapping("/getProduct")
	public String getProduct(@RequestParam("prodNo") int prodNo, 
	                        @RequestParam(value="menu", required=false) String menu,
	                        Model model,
	                        HttpSession session,
	                        HttpServletRequest request, 
	                        HttpServletResponse response) throws Exception { 
	    
		// (ê¸°ì¡´ ì½”ë“œì™€ ë™ì¼)
	    System.out.println("/product/getProduct : GET");
	    
	    User sessionUser = (User) session.getAttribute("user");
	    if (sessionUser != null) {
	        model.addAttribute("user", sessionUser);
	        if ("admin".equals(sessionUser.getRole())) {
	            model.addAttribute("viewMode", "admin");
	        } else {
	            model.addAttribute("viewMode", "user");
	        }
	    } else {
	        model.addAttribute("viewMode", "user");
	    }
	    
	    Cookie[] cookies = request.getCookies();
	    String historyValue = "";
	    if (cookies != null) {
	        for (Cookie c : cookies) {
	            if ("history".equals(c.getName())) {
	                historyValue = c.getValue();
	                break;
	            }
	        }
	    }
	    List<String> historyList = new LinkedList<>(Arrays.asList(historyValue.split("-")));
	    historyList.remove(String.valueOf(prodNo));
	    historyList.add(0, String.valueOf(prodNo));
	    if (historyList.size() > 5) historyList.remove(5);
	    
	    String newHistoryValue = String.join("-", historyList);
	    if (newHistoryValue.startsWith("-")) newHistoryValue = newHistoryValue.substring(1);

	    Cookie cookie = new Cookie("history", newHistoryValue);
	    cookie.setPath("/");
	    cookie.setMaxAge(60 * 60 * 24);
	    response.addCookie(cookie);

	    Product product = productService.getProductWithStatus(prodNo);
	    model.addAttribute("product", product);

	    if ("SOLD".equals(product.getSaleStatus())) {
	        Purchase purchaseInfo = purchaseService.getPurchaseByProdNo(prodNo);
	        model.addAttribute("purchaseInfo", purchaseInfo);
	    }

	    return "product/getProduct";
	}

	/**
	 * ìƒí’ˆ ìˆ˜ì • í™”ë©´ìœ¼ë¡œ ì´ë™
	 */
	@RequestMapping("/updateProductView")
	public String updateProductView(@RequestParam("prodNo") int prodNo, 
	                               Model model) throws Exception{ 
		// (ê¸°ì¡´ ì½”ë“œì™€ ë™ì¼)
	    System.out.println("/product/updateProductView : GET or POST");
	    Product product = productService.getProductWithStatus(prodNo); 
	    model.addAttribute("product", product);
	    return "product/updateProductView";
	}

	/**
	 * ìƒí’ˆ ì •ë³´ ìˆ˜ì • ì²˜ë¦¬ (íŒŒì¼ ì¶”ê°€/ì‚­ì œ ì§€ì›)
	 */
	@RequestMapping("/updateProduct")
	public String updateProduct(@ModelAttribute("product") Product product,
	                           @RequestParam(value = "deleteFileIds", required = false) String deleteFileIds,
	                           @RequestParam(value = "uploadFiles", required = false) List<MultipartFile> uploadFiles,
	                           HttpSession session) throws Exception{

	    System.out.println("/product/updateProduct : POST");
	    
	    productService.updateProduct(product);
	    System.out.println("ìƒí’ˆ ê¸°ë³¸ ì •ë³´ ìˆ˜ì • ì™„ë£Œ.");

	    // â­ï¸ 3. ê¸°ì¡´ íŒŒì¼ ì‚­ì œ ì²˜ë¦¬ (ì£¼ìž…ë°›ì€ uploadPath ì‚¬ìš©)
	    if (deleteFileIds != null && !deleteFileIds.trim().isEmpty()) {
	        String[] fileIdArray = deleteFileIds.split(",");
	        for (String fileIdStr : fileIdArray) {
	            try {
	                int fileId = Integer.parseInt(fileIdStr.trim());
	                ProductFile fileToDelete = productService.getProductFile(fileId);
	                
	                if (fileToDelete != null) {
	                    // ë¬¼ë¦¬ì  íŒŒì¼ ì‚­ì œ
	                    File physicalFile = new File(uploadPath, fileToDelete.getSavedName());
	                    if (physicalFile.exists()) {
	                        physicalFile.delete();
	                        System.out.println("ë¬¼ë¦¬ì  íŒŒì¼ ì‚­ì œ ì™„ë£Œ: " + fileToDelete.getSavedName());
	                    }
	                    // DBì—ì„œ íŒŒì¼ ì •ë³´ ì‚­ì œ
	                    productService.deleteProductFile(fileId);
	                }
	            } catch (NumberFormatException e) {
	                System.err.println("ìž˜ëª»ëœ íŒŒì¼ ID í˜•ì‹: " + fileIdStr);
	            }
	        }
	    }

	    // â­ï¸ 4. ìƒˆë¡œìš´ íŒŒì¼ ì¶”ê°€ ì²˜ë¦¬ (ì£¼ìž…ë°›ì€ uploadPath ì‚¬ìš©)
	    if (uploadFiles != null && !uploadFiles.isEmpty()) {
	        System.out.println("ìƒˆë¡œ ì¶”ê°€í•  íŒŒì¼ ê°œìˆ˜: " + uploadFiles.size());
	        
	        // ì§€ì •ëœ ì™¸ë¶€ ê²½ë¡œì— ë””ë ‰í† ë¦¬ ìƒì„±
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdirs();
	        }
	        
	        for (MultipartFile file : uploadFiles) {
	            if (!file.isEmpty()) {
	                String originalFileName = file.getOriginalFilename();
	                String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName.replaceAll("\\s", "_");

	                File targetFile = new File(uploadPath, savedFileName);
	                file.transferTo(targetFile);

	                ProductFile productFile = new ProductFile();
	                productFile.setProdNo(product.getProdNo());
	                productFile.setOriginalName(originalFileName);
	                productFile.setSavedName(savedFileName);
	                productFile.setFileSize(file.getSize());
	                productFile.setFileType(file.getContentType());
	                
	                productService.addProductFile(productFile);
	            }
	        }
	    }

	    return "redirect:/product/getProduct?prodNo=" + product.getProdNo();
	}
	
	/**
	 * ìƒí’ˆ ëª©ë¡ ì¡°íšŒ
	 */
	@RequestMapping("/listProduct")
	public String listProduct(@ModelAttribute("search") Search search,
	                         @RequestParam(value = "menu", required = false) String menu,
	                         Model model) throws Exception {

	    System.out.println("/product/listProduct : GET/POST");

	    if (search.getCurrentPage() == 0) {
	        search.setCurrentPage(1);
	    }
	    search.setPageSize(pageSize);
	    
	    // â­ íŽ˜ì´ì§• ê³„ì‚° ì¶”ê°€
	    int startRowNum = (search.getCurrentPage() - 1) * search.getPageSize() + 1;
	    int endRowNum = search.getCurrentPage() * search.getPageSize();
	    search.setStartRowNum(startRowNum);
	    search.setEndRowNum(endRowNum);
	    
	    System.out.println("==> íŽ˜ì´ì§•: startRow=" + startRowNum + ", endRow=" + endRowNum);
	    
	    Map<String, Object> map = productService.getProductListWithStatus(search);

	    Page resultPage = new Page(
	        search.getCurrentPage(),
	        ((Integer) map.get("totalCount")).intValue(),
	        pageUnit,
	        pageSize
	    );

	    model.addAttribute("list", map.get("list"));
	    model.addAttribute("resultPage", resultPage);
	    model.addAttribute("search", search);
	    model.addAttribute("menu", menu);

	    return "product/listProduct";
	}
	
	/**
	 * ìƒí’ˆ ì •ë³´ ìˆ˜ì • (JSON ì‘ë‹µ - Ajaxìš©)
	 * POST /product/json/updateProduct
	 */
	@RequestMapping(value="/json/updateProduct", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateProductJson(@RequestParam("prodNo") int prodNo,
	                                             @RequestParam("prodName") String prodName,
	                                             @RequestParam("prodDetail") String prodDetail,
	                                             @RequestParam("manuDate") String manuDate,
	                                             @RequestParam("price") int price,
	                                             @RequestParam(value="deleteFileIds", required=false) String deleteFileIds,
	                                             @RequestParam(value="uploadFiles", required=false) List<MultipartFile> uploadFiles,
	                                             HttpSession session) throws Exception {
	    
	    System.out.println("/product/json/updateProduct : POST");
	    
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        // Product ê°ì²´ ìƒì„± ë° ì„¤ì •
	        Product product = new Product();
	        product.setProdNo(prodNo);
	        product.setProdName(prodName);
	        product.setProdDetail(prodDetail);
	        product.setManuDate(manuDate);
	        product.setPrice(price);
	        
	        // ìƒí’ˆ ê¸°ë³¸ ì •ë³´ ìˆ˜ì •
	        productService.updateProduct(product);
	        System.out.println("ìƒí’ˆ ê¸°ë³¸ ì •ë³´ ìˆ˜ì • ì™„ë£Œ: " + prodName);
	        
	        // íŒŒì¼ ì‚­ì œ ì²˜ë¦¬
	        if (deleteFileIds != null && !deleteFileIds.trim().isEmpty()) {
	            String[] fileIdArray = deleteFileIds.split(",");
	            for (String fileIdStr : fileIdArray) {
	                try {
	                    int fileId = Integer.parseInt(fileIdStr.trim());
	                    
	                    // DBì—ì„œ íŒŒì¼ ì •ë³´ ì¡°íšŒ
	                    ProductFile fileInfo = productService.getProductFile(fileId);
	                    
	                    if (fileInfo != null) {
	                        // ì‹¤ì œ íŒŒì¼ ì‚­ì œ
	                        File file = new File(uploadPath + fileInfo.getSavedName());
	                        if (file.exists()) {
	                            file.delete();
	                            System.out.println("íŒŒì¼ ì‚­ì œ ì™„ë£Œ: " + fileInfo.getSavedName());
	                        }
	                        
	                        // DBì—ì„œ íŒŒì¼ ì •ë³´ ì‚­ì œ
	                        productService.deleteProductFile(fileId);
	                    }
	                } catch (NumberFormatException e) {
	                    System.err.println("ìž˜ëª»ëœ íŒŒì¼ ID í˜•ì‹: " + fileIdStr);
	                }
	            }
	        }
	        
	        // ìƒˆ íŒŒì¼ ì—…ë¡œë“œ ì²˜ë¦¬
	        if (uploadFiles != null && !uploadFiles.isEmpty()) {
	            for (MultipartFile uploadFile : uploadFiles) {
	                if (!uploadFile.isEmpty()) {
	                    // ì›ë³¸ íŒŒì¼ëª…
	                    String originalFileName = uploadFile.getOriginalFilename();
	                    
	                    // ì €ìž¥í•  íŒŒì¼ëª… (UUID ì‚¬ìš©)
	                    String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
	                    
	                    // íŒŒì¼ ì €ìž¥
	                    File destFile = new File(uploadPath + savedFileName);
	                    uploadFile.transferTo(destFile);
	                    System.out.println("ìƒˆ íŒŒì¼ ì—…ë¡œë“œ ì™„ë£Œ: " + savedFileName);
	                    
	                    // DBì— íŒŒì¼ ì •ë³´ ì €ìž¥
	                    ProductFile productFile = new ProductFile();
	                    productFile.setProdNo(prodNo);
	                    productFile.setOriginalName(originalFileName);
	                    productFile.setSavedName(savedFileName);
	                    productFile.setFileSize(uploadFile.getSize());
	                    productFile.setFileType(uploadFile.getContentType());
	                    
	                    productService.addProductFile(productFile);
	                }
	            }
	        }
	        
	        // ì„±ê³µ ì‘ë‹µ
	        response.put("success", true);
	        response.put("message", "ìƒí’ˆì´ ì„±ê³µì ìœ¼ë¡œ ìˆ˜ì •ë˜ì—ˆìŠµë‹ˆë‹¤.");
	        response.put("prodNo", prodNo);
	        
	    } catch (Exception e) {
	        System.err.println("ìƒí’ˆ ìˆ˜ì • ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
	        e.printStackTrace();
	        
	        response.put("success", false);
	        response.put("message", "ìƒí’ˆ ìˆ˜ì •ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
	    }
	    
	    return response;
	}
}