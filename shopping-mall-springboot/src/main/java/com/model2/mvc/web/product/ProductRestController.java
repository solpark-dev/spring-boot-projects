package com.model2.mvc.web.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.ProductFile;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.common.Search;

/**
 * ProductRestController.java
 * ìƒí’ˆ ê´€ë¦¬ë¥¼ ìœ„í•œ RESTful API ì»¨íŠ¸ë¡¤ëŸ¬
 * URL Prefix : /product/json/
 * 
 * API ì„¤ê³„ íŒ¨í„´:
 * - GET /product/json/getProductList : ìƒí’ˆ ëª©ë¡ ì¡°íšŒ
 * - GET /product/json/getProduct/{id} : íŠ¹ì • ìƒí’ˆ ì¡°íšŒ
 * - POST /product/json/addProduct : ìƒˆ ìƒí’ˆ ë“±ë¡
 * - POST /product/json/updateProduct : ìƒí’ˆ ìˆ˜ì •
 * - POST /product/json/deleteProduct/{id} : ìƒí’ˆ ì‚­ì œ
 */
@RestController
@RequestMapping("/product/json")
public class ProductRestController {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    public ProductRestController() {
        System.out.println("==> ProductRestController ì‹¤í–‰ë¨ : " + this.getClass());
    }

    /**
     * ìƒí’ˆ ë“±ë¡
     * POST /api/products
     * @param product ìƒí’ˆ ì •ë³´ (JSON í˜•ì‹ìœ¼ë¡œ ì „ë‹¬ë¨)
     * @return ResponseEntity<Map<String, Object>> - ë“±ë¡ ê²°ê³¼
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product, 
                                                            HttpSession session) {
        System.out.println("/product/json : POST í˜¸ì¶œë¨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            
            // ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì‹¤í–‰
            productService.addProduct(product);
            
            // ì„±ê³µ ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ìƒí’ˆì´ ì„±ê³µì ìœ¼ë¡œ ë“±ë¡ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("productNo", product.getProdNo());
            response.put("data", product);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("ìƒí’ˆ ë“±ë¡ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "ìƒí’ˆ ë“±ë¡ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * ìƒí’ˆ ëª©ë¡ ì¡°íšŒ (ê²€ìƒ‰ + íŽ˜ì´ì§• ì§€ì›)
     * GET /api/products?page=1&searchKeyword=iPhone&searchCondition=0
     * @param search ê²€ìƒ‰ ì¡°ê±´ ê°ì²´
     * @return ResponseEntity<Map<String, Object>> - ìƒí’ˆ ëª©ë¡ ë° íŽ˜ì´ì§• ì •ë³´
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProductList(@ModelAttribute Search search) {
        System.out.println("/product/json : GET í˜¸ì¶œë¨ (ëª©ë¡ ì¡°íšŒ)");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ê¸°ë³¸ê°’ ì„¤ì •
            if (search.getCurrentPage() == 0) {
                search.setCurrentPage(1);
            }
            if (search.getPageSize() == 0) {
                search.setPageSize(10);  // ê¸°ë³¸ íŽ˜ì´ì§€ í¬ê¸°
            }
            
            // ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì‹¤í–‰
            Map<String, Object> result = productService.getProductListWithStatus(search);
            
            // ì„±ê³µ ì‘ë‹µ
            response.put("success", true);
            response.put("data", result);
            response.put("search", search);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ìƒí’ˆ ëª©ë¡ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "ìƒí’ˆ ëª©ë¡ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * íŠ¹ì • ìƒí’ˆ ì¡°íšŒ
     * GET /api/products/{prodNo}
     * @param prodNo ì¡°íšŒí•  ìƒí’ˆ ë²ˆí˜¸
     * @return ResponseEntity<Map<String, Object>> - ìƒí’ˆ ì •ë³´
     */
    @GetMapping("/{prodNo}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable int prodNo) {
        System.out.println("/product/json" + prodNo + " : GET í˜¸ì¶œë¨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì‹¤í–‰
            Product product = productService.getProductWithStatus(prodNo);
            
            if (product == null) {
                response.put("success", false);
                response.put("message", "í•´ë‹¹ ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // ì„±ê³µ ì‘ë‹µ
            response.put("success", true);
            response.put("data", product);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ìƒí’ˆ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "ìƒí’ˆ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ìƒí’ˆ ì •ë³´ ìˆ˜ì •
     * PUT /api/products/{prodNo}
     * @param prodNo ìˆ˜ì •í•  ìƒí’ˆ ë²ˆí˜¸
     * @param product ìˆ˜ì •í•  ìƒí’ˆ ì •ë³´ (JSON í˜•ì‹)
     * @return ResponseEntity<Map<String, Object>> - ìˆ˜ì • ê²°ê³¼
     */
    @PutMapping("/{prodNo}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable int prodNo, 
                                                           @RequestBody Product product, 
                                                           HttpSession session) {
        System.out.println("/product/json" + prodNo + " : PUT í˜¸ì¶œë¨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            
            // ìƒí’ˆ ë²ˆí˜¸ ì„¤ì •
            product.setProdNo(prodNo);
            
            // ê¸°ì¡´ ìƒí’ˆ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
            Product existingProduct = productService.getProduct(prodNo);
            if (existingProduct == null) {
                response.put("success", false);
                response.put("message", "ìˆ˜ì •í•  ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì‹¤í–‰
            productService.updateProduct(product);
            
            // ìˆ˜ì •ëœ ìƒí’ˆ ì •ë³´ ì¡°íšŒ
            Product updatedProduct = productService.getProduct(prodNo);
            
            // ì„±ê³µ ì‘ë‹µ
            response.put("success", true);
            response.put("message", "ìƒí’ˆì´ ì„±ê³µì ìœ¼ë¡œ ìˆ˜ì •ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("data", updatedProduct);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ìƒí’ˆ ìˆ˜ì • ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "ìƒí’ˆ ìˆ˜ì •ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * ìƒí’ˆì˜ íŒŒì¼ ëª©ë¡ ì¡°íšŒ
     * GET /api/products/{prodNo}/files
     * @param prodNo ìƒí’ˆ ë²ˆí˜¸
     * @return ResponseEntity<Map<String, Object>> - íŒŒì¼ ëª©ë¡
     */
    @GetMapping("/{prodNo}/files")
    public ResponseEntity<Map<String, Object>> getProductFiles(@PathVariable int prodNo) {
        System.out.println("/product/json" + prodNo + "/files : GET í˜¸ì¶œë¨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // ìƒí’ˆ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
            Product product = productService.getProduct(prodNo);
            if (product == null) {
                response.put("success", false);
                response.put("message", "í•´ë‹¹ ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // íŒŒì¼ ëª©ë¡ ì¡°íšŒ
            List<ProductFile> files = productService.getProductFiles(prodNo);
            
            response.put("success", true);
            response.put("data", files);
            response.put("fileCount", files != null ? files.size() : 0);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ìƒí’ˆ íŒŒì¼ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "íŒŒì¼ ëª©ë¡ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ìƒí’ˆì— íŒŒì¼ ì¶”ê°€
     * POST /api/products/{prodNo}/files
     * @param prodNo ìƒí’ˆ ë²ˆí˜¸
     * @param productFile íŒŒì¼ ì •ë³´ (JSON í˜•ì‹)
     * @return ResponseEntity<Map<String, Object>> - íŒŒì¼ ì¶”ê°€ ê²°ê³¼
     */
    @PostMapping("/{prodNo}/files")
    public ResponseEntity<Map<String, Object>> addProductFile(@PathVariable int prodNo, 
                                                            @RequestBody ProductFile productFile, 
                                                            HttpSession session) {
        System.out.println("/product/json" + prodNo + "/files : POST í˜¸ì¶œë¨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            
            // ìƒí’ˆ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
            Product product = productService.getProduct(prodNo);
            if (product == null) {
                response.put("success", false);
                response.put("message", "í•´ë‹¹ ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // íŒŒì¼ ì •ë³´ì— ìƒí’ˆ ë²ˆí˜¸ ì„¤ì •
            productFile.setProdNo(prodNo);
            
            // ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì‹¤í–‰
            productService.addProductFile(productFile);
            
            response.put("success", true);
            response.put("message", "íŒŒì¼ì´ ì„±ê³µì ìœ¼ë¡œ ì¶”ê°€ë˜ì—ˆìŠµë‹ˆë‹¤.");
            response.put("data", productFile);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("íŒŒì¼ ì¶”ê°€ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "íŒŒì¼ ì¶”ê°€ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * íŠ¹ì • íŒŒì¼ ì‚­ì œ
     * DELETE /api/products/files/{fileId}
     * @param fileId ì‚­ì œí•  íŒŒì¼ ID
     * @return ResponseEntity<Map<String, Object>> - ì‚­ì œ ê²°ê³¼
     */
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteProductFile(@PathVariable int fileId, 
                                                               HttpSession session) {
        System.out.println("/product/json/files/" + fileId + " : DELETE í˜¸ì¶œë¨");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            
            // íŒŒì¼ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
            ProductFile existingFile = productService.getProductFile(fileId);
            if (existingFile == null) {
                response.put("success", false);
                response.put("message", "ì‚­ì œí•  íŒŒì¼ì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            // ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ì‹¤í–‰
            productService.deleteProductFile(fileId);
            
            response.put("success", true);
            response.put("message", "íŒŒì¼ì´ ì„±ê³µì ìœ¼ë¡œ ì‚­ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("íŒŒì¼ ì‚­ì œ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            response.put("success", false);
            response.put("message", "íŒŒì¼ ì‚­ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{prodNo}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int prodNo, HttpSession session) {
        System.out.println("/product/json" + prodNo + " : DELETE í˜¸ì¶œë¨");
        Map<String, Object> response = new HashMap<>();
        
        try {
            
            productService.deleteProduct(prodNo);
            
            response.put("success", true);
            response.put("message", "ìƒí’ˆì´ ì„±ê³µì ìœ¼ë¡œ ì‚­ì œë˜ì—ˆìŠµë‹ˆë‹¤.");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "ìƒí’ˆ ì‚­ì œì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/getTopProducts")
    public List<Map<String, Object>> getTopProducts() {
        System.out.println("/product/json/getTopProducts : GET í˜¸ì¶œë¨");
        
        try {
            // âœ… ìµœì‹  ë“±ë¡ ìƒí’ˆ 3ê°œë¥¼ prod_no ë‚´ë¦¼ì°¨ìˆœìœ¼ë¡œ ì¡°íšŒ
            List<Product> products = productService.getTopProducts(3);
            
            List<Map<String, Object>> response = new java.util.ArrayList<>();
            
            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    Map<String, Object> productMap = new HashMap<>();
                    
                    productMap.put("prodNo", product.getProdNo());
                    productMap.put("prodName", product.getProdName());
                    productMap.put("prodDetail", product.getProdDetail());
                    productMap.put("price", product.getPrice());
                    
                    // ê¸°ë³¸ ì´ë¯¸ì§€
                    String imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=60";
                    
                    // ìƒí’ˆì— ì—…ë¡œë“œëœ ì´ë¯¸ì§€ê°€ ìžˆìœ¼ë©´ ì²« ë²ˆì§¸ ì´ë¯¸ì§€ ì‚¬ìš©
                    if (product.getProductFiles() != null && !product.getProductFiles().isEmpty()) {
                        for (ProductFile file : product.getProductFiles()) {
                            if (file.getFileType() != null && file.getFileType().startsWith("image/")) {
                                imageUrl = "/uploads/" + file.getSavedName();
                                break;
                            }
                        }
                    }
                    
                    productMap.put("imageUrl", imageUrl);
                    response.add(productMap);
                }
            }
            
            System.out.println("ìµœì‹  ìƒí’ˆ " + response.size() + "ê°œ ì¡°íšŒ ì™„ë£Œ");
            return response;
            
        } catch (Exception e) {
            System.err.println("ìµœì‹  ìƒí’ˆ ì¡°íšŒ ì¤‘ ì˜¤ë¥˜ ë°œìƒ: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * í•„í„°ë§ëœ ìƒí’ˆ ëª©ë¡ ì¡°íšŒ (ë¬´í•œ ìŠ¤í¬ë¡¤ìš©)
     * GET /product/json/getFilteredProducts
     */
    @GetMapping("/getFilteredProducts")
    public ResponseEntity<Map<String, Object>> getFilteredProducts(
            @RequestParam(defaultValue="1") int currentPage,
            @RequestParam(defaultValue="12") int pageSize,
            @RequestParam(required=false) String searchCondition,
            @RequestParam(required=false) String searchKeyword,
            @RequestParam(required=false) String priceRange,
            @RequestParam(required=false) String status,
            @RequestParam(defaultValue="latest") String sortBy) {
        
        System.out.println("/product/json/getFilteredProducts í˜¸ì¶œ");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Search search = new Search();
            search.setCurrentPage(currentPage);
            search.setPageSize(pageSize);
            search.setSearchCondition(searchCondition);
            search.setSearchKeyword(searchKeyword);
            
            search.setPriceRange(priceRange);
            search.setStatus(status);
            search.setSortBy(sortBy);
            
            // startRowNum, endRowNum ê³„ì‚°
            search.setStartRowNum((currentPage - 1) * pageSize + 1);
            search.setEndRowNum(currentPage * pageSize);
            
            Map<String, Object> result = productService.getProductListWithStatus(search);
            
            response.put("success", true);
            response.put("data", result);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ìƒí’ˆ ëª©ë¡ ì¡°íšŒ ì‹¤íŒ¨: " + e.getMessage());
            response.put("success", false);
            response.put("message", "ìƒí’ˆ ëª©ë¡ ì¡°íšŒì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ìƒí’ˆ ê²€ìƒ‰ ìžë™ì™„ì„±
     * GET /product/json/searchSuggestions
     */
    @GetMapping("/searchSuggestions")
    public List<Map<String, Object>> getSearchSuggestions(
            @RequestParam String searchCondition,
            @RequestParam String searchKeyword) {
        
        System.out.println("/product/json/searchSuggestions í˜¸ì¶œ");
        System.out.println("ê²€ìƒ‰ì¡°ê±´: " + searchCondition + ", í‚¤ì›Œë“œ: " + searchKeyword);
        
        if (searchKeyword == null || searchKeyword.trim().length() < 2) {
            return new ArrayList<>();
        }
        
        try {
            Search search = new Search();
            search.setCurrentPage(1);
            search.setPageSize(10);
            search.setSearchCondition(searchCondition);
            search.setSearchKeyword(searchKeyword.trim());
            search.setStartRowNum(1);
            search.setEndRowNum(10);
            
            Map<String, Object> result = productService.getProductListWithStatus(search);
            List<Product> products = (List<Product>) result.get("list");
            
            List<Map<String, Object>> suggestions = new ArrayList<>();
            
            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    Map<String, Object> suggestion = new HashMap<>();
                    
                    if ("0".equals(searchCondition)) {
                        // ìƒí’ˆë²ˆí˜¸ ê²€ìƒ‰
                        suggestion.put("value", String.valueOf(product.getProdNo()));
                        suggestion.put("label", product.getProdNo() + " - " + product.getProdName());
                        suggestion.put("displayText", String.valueOf(product.getProdNo()));
                    } else if ("1".equals(searchCondition)) {
                        // ìƒí’ˆëª… ê²€ìƒ‰
                        suggestion.put("value", product.getProdName());
                        suggestion.put("label", product.getProdName() + " (â‚©" + product.getPrice() + ")");
                        suggestion.put("displayText", product.getProdName());
                    } else if ("2".equals(searchCondition)) {
                        // ê°€ê²© ê²€ìƒ‰
                        suggestion.put("value", String.valueOf(product.getPrice()));
                        suggestion.put("label", product.getProdName() + " - â‚©" + product.getPrice());
                        suggestion.put("displayText", String.valueOf(product.getPrice()));
                    }
                    
                    suggestions.add(suggestion);
                }
            }
            
            System.out.println("ìžë™ì™„ì„± ê²°ê³¼: " + suggestions.size() + "ê°œ");
            return suggestions;
            
        } catch (Exception e) {
            System.err.println("ìžë™ì™„ì„± ì¡°íšŒ ì‹¤íŒ¨: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}