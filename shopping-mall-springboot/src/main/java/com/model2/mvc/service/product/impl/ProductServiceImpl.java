package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.ProductFile;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {

	///Field
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDao;
	
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	///Constructor
	public ProductServiceImpl() {
		System.out.println(this.getClass());
	}

	///Method
	@Override
	public void addProduct(Product product) throws Exception {
		System.out.println("==> ProductServiceImpl.addProduct() ì‹œìž‘");
		
		// ìž…ë ¥ê°’ ê²€ì¦
		if (product == null) {
			throw new IllegalArgumentException("Product ê°ì²´ê°€ nullìž…ë‹ˆë‹¤.");
		}
		
		if (product.getProdName() == null || product.getProdName().trim().isEmpty()) {
			throw new IllegalArgumentException("ìƒí’ˆëª…ì€ í•„ìˆ˜ìž…ë‹ˆë‹¤.");
		}
		
		if (product.getPrice() <= 0) {
			throw new IllegalArgumentException("ìƒí’ˆ ê°€ê²©ì€ 0ë³´ë‹¤ ì»¤ì•¼ í•©ë‹ˆë‹¤.");
		}
		
		productDao.addProduct(product);
		System.out.println("==> ìƒí’ˆ ë“±ë¡ ì™„ë£Œ: " + product.getProdName() + ", prodNo: " + product.getProdNo());
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
	    System.out.println("==> ProductServiceImpl.getProduct() ì‹œìž‘, prodNo: " + prodNo);
	    
	    if (prodNo <= 0) {
	        throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
	    }
	    
	    // 1. ê¸°ë³¸ ìƒí’ˆ ì •ë³´ ì¡°íšŒ
	    Product product = productDao.getProduct(prodNo);
	    
	    if (product != null) {
	        System.out.println("==> ìƒí’ˆ ì¡°íšŒ ì„±ê³µ: " + product.getProdName());
	        
	        // 2. íŒŒì¼ ì •ë³´ë„ í•¨ê»˜ ì¡°íšŒí•´ì„œ Product ê°ì²´ì— ì„¤ì •
	        List<ProductFile> productFiles = productDao.getProductFiles(prodNo);
	        if (productFiles != null && !productFiles.isEmpty()) {
	            product.setProductFiles(productFiles);
	            System.out.println("==> íŒŒì¼ ì •ë³´ë„ í•¨ê»˜ ì¡°íšŒë¨: " + productFiles.size() + "ê°œ");
	        } else {
	            System.out.println("==> ë“±ë¡ëœ íŒŒì¼ì´ ì—†ìŠµë‹ˆë‹¤.");
	        }
	    } else {
	        System.out.println("==> ìƒí’ˆë²ˆí˜¸ " + prodNo + "ì— í•´ë‹¹í•˜ëŠ” ìƒí’ˆì´ ì—†ìŠµë‹ˆë‹¤.");
	    }
	    
	    return product;
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		System.out.println("==> ProductServiceImpl.getProductList() ì‹œìž‘");
		
		List<Product> list = productDao.getProductList(search);
		int totalCount = productDao.getTotalCount(search);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", new Integer(totalCount));
		
		System.out.println("==> ìƒí’ˆ ëª©ë¡ ì¡°íšŒ ì™„ë£Œ, ì´ " + totalCount + "ê°œ");
		
		return map;
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		System.out.println("==> ProductServiceImpl.updateProduct() ì‹œìž‘");
		
		// ìž…ë ¥ê°’ ê²€ì¦
		if (product == null) {
			throw new IllegalArgumentException("Product ê°ì²´ê°€ nullìž…ë‹ˆë‹¤.");
		}
		
		if (product.getProdNo() <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + product.getProdNo());
		}
		
		productDao.updateProduct(product);
		System.out.println("==> ìƒí’ˆ ìˆ˜ì • ì™„ë£Œ: " + product.getProdName());
	}

	@Override
	public Product getProductWithStatus(int prodNo) throws Exception {
		System.out.println("==> ProductServiceImpl.getProductWithStatus() ì‹œìž‘, prodNo: " + prodNo);
		
		if (prodNo <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
		}
		
		Product product = productDao.getProductWithStatus(prodNo);
		
		if (product != null) {
			System.out.println("==> ìƒí’ˆ ì¡°íšŒ ì„±ê³µ (íŒë§¤ìƒíƒœ í¬í•¨): " + product.getProdName() + 
							 ", ìƒíƒœ: " + product.getSaleStatus());
		}
		
		return product;
	}

	@Override
	public Map<String, Object> getProductListWithStatus(Search search) throws Exception {
	    System.out.println("==> ProductServiceImpl.getProductListWithStatus() ì‹œìž‘");
	    
	    if (search.getPageSize() == 0) {
	        search.setPageSize(8);
	    }
	    
	    search.setStartRowNum((search.getCurrentPage() - 1) * search.getPageSize() + 1);
	    search.setEndRowNum(search.getCurrentPage() * search.getPageSize());
	    
	    List<Product> list = productDao.getProductListWithStatus(search);
	    
	    // ðŸ‘‡ ì´ ë¶€ë¶„ì„ ìˆ˜ì •í•©ë‹ˆë‹¤!
	    // int totalCount = productDao.getTotalCount(search); // ê¸°ì¡´ ì½”ë“œ
	    int totalCount = productDao.getFilteredTotalCount(search); // ìˆ˜ì •ëœ ì½”ë“œ
	    
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("list", list);
	    map.put("totalCount", new Integer(totalCount));
	    
	    System.out.println("==> ìƒí’ˆ ëª©ë¡ ì¡°íšŒ ì™„ë£Œ (íŒë§¤ìƒíƒœ í¬í•¨), ì´ " + totalCount + "ê°œ");
	    
	    return map;
	}

	// ===============================================
	// íŒŒì¼ ê´€ë ¨ ë¹„ì¦ˆë‹ˆìŠ¤ ë¡œì§ ë©”ì„œë“œë“¤ (ë‹¤ì¤‘ íŒŒì¼ ì—…ë¡œë“œ ì§€ì›)
	// ===============================================

	@Override
	public void addProductFile(ProductFile productFile) throws Exception {
		System.out.println("==> ProductServiceImpl.addProductFile() ì‹œìž‘");
		
		// ìž…ë ¥ê°’ ê²€ì¦
		if (productFile == null) {
			throw new IllegalArgumentException("ProductFile ê°ì²´ê°€ nullìž…ë‹ˆë‹¤.");
		}
		
		if (productFile.getProdNo() <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + productFile.getProdNo());
		}
		
		if (productFile.getOriginalName() == null || productFile.getOriginalName().trim().isEmpty()) {
			throw new IllegalArgumentException("ì›ë³¸ íŒŒì¼ëª…ì€ í•„ìˆ˜ìž…ë‹ˆë‹¤.");
		}
		
		if (productFile.getSavedName() == null || productFile.getSavedName().trim().isEmpty()) {
			throw new IllegalArgumentException("ì €ìž¥ íŒŒì¼ëª…ì€ í•„ìˆ˜ìž…ë‹ˆë‹¤.");
		}
		
		// íŒŒì¼ í¬ê¸° ê²€ì¦ (10MB ì œí•œ)
		if (productFile.getFileSize() != null && productFile.getFileSize() > 10485760) {
			throw new IllegalArgumentException("íŒŒì¼ í¬ê¸°ê°€ 10MBë¥¼ ì´ˆê³¼í•  ìˆ˜ ì—†ìŠµë‹ˆë‹¤.");
		}
		
		productDao.addProductFile(productFile);
		System.out.println("==> íŒŒì¼ ì •ë³´ ë“±ë¡ ì™„ë£Œ: " + productFile.getOriginalName());
	}

	@Override
	public List<ProductFile> getProductFiles(int prodNo) throws Exception {
		System.out.println("==> ProductServiceImpl.getProductFiles() ì‹œìž‘, prodNo: " + prodNo);
		
		if (prodNo <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
		}
		
		List<ProductFile> productFiles = productDao.getProductFiles(prodNo);
		
		System.out.println("==> ìƒí’ˆ íŒŒì¼ ì¡°íšŒ ì™„ë£Œ, íŒŒì¼ ê°œìˆ˜: " + 
						 (productFiles != null ? productFiles.size() : 0));
		
		return productFiles;
	}

	@Override
	public ProductFile getProductFile(int fileId) throws Exception {
		System.out.println("==> ProductServiceImpl.getProductFile() ì‹œìž‘, fileId: " + fileId);
		
		if (fileId <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ íŒŒì¼ IDìž…ë‹ˆë‹¤: " + fileId);
		}
		
		ProductFile productFile = productDao.getProductFile(fileId);
		
		if (productFile != null) {
			System.out.println("==> íŒŒì¼ ì¡°íšŒ ì„±ê³µ: " + productFile.getOriginalName());
		} else {
			System.out.println("==> íŒŒì¼ ID " + fileId + "ì— í•´ë‹¹í•˜ëŠ” íŒŒì¼ì´ ì—†ìŠµë‹ˆë‹¤.");
		}
		
		return productFile;
	}

	@Override
	public void deleteProductFile(int fileId) throws Exception {
		System.out.println("==> ProductServiceImpl.deleteProductFile() ì‹œìž‘, fileId: " + fileId);
		
		if (fileId <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ íŒŒì¼ IDìž…ë‹ˆë‹¤: " + fileId);
		}
		
		// íŒŒì¼ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸ (ì„ íƒì‚¬í•­)
		ProductFile existingFile = productDao.getProductFile(fileId);
		if (existingFile != null) {
			System.out.println("==> ì‚­ì œí•  íŒŒì¼: " + existingFile.getOriginalName());
		}
		
		productDao.deleteProductFile(fileId);
		System.out.println("==> íŒŒì¼ ì‚­ì œ ì™„ë£Œ, fileId: " + fileId);
	}

	@Override
	public void deleteProductFiles(int prodNo) throws Exception {
		System.out.println("==> ProductServiceImpl.deleteProductFiles() ì‹œìž‘, prodNo: " + prodNo);
		
		if (prodNo <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
		}
		
		// ë¨¼ì € í•´ë‹¹ ìƒí’ˆì˜ íŒŒì¼ ê°œìˆ˜ í™•ì¸
		List<ProductFile> existingFiles = productDao.getProductFiles(prodNo);
		int fileCount = (existingFiles != null) ? existingFiles.size() : 0;
		
		if (fileCount > 0) {
			System.out.println("==> ì‚­ì œí•  íŒŒì¼ ê°œìˆ˜: " + fileCount);
			
			// ê° íŒŒì¼ì„ ê°œë³„ ì‚­ì œ (ì‹¤ì œ íŒŒì¼ ì‚­ì œëŠ” Controllerì—ì„œ ì²˜ë¦¬)
			for (ProductFile file : existingFiles) {
				productDao.deleteProductFile(file.getFileId());
			}
			
			System.out.println("==> ìƒí’ˆì˜ ëª¨ë“  íŒŒì¼ ì‚­ì œ ì™„ë£Œ, prodNo: " + prodNo);
		} else {
			System.out.println("==> ì‚­ì œí•  íŒŒì¼ì´ ì—†ìŠµë‹ˆë‹¤, prodNo: " + prodNo);
		}
	}
	
	@Override
    public void deleteProduct(int prodNo) throws Exception {
        productDao.deleteProduct(prodNo);
    }
	
	// ProductServiceImpl.javaì— ì¶”ê°€
	@Override
	public List<Product> getTopProducts(int limit) throws Exception {
	    System.out.println("==> ProductServiceImpl.getTopProducts() ì‹œìž‘, limit: " + limit);
	    
	    if (limit <= 0) {
	        throw new IllegalArgumentException("limitì€ 0ë³´ë‹¤ ì»¤ì•¼ í•©ë‹ˆë‹¤.");
	    }
	    
	    List<Product> products = productDao.getTopProducts(limit);
	    System.out.println("==> ì¡°íšŒëœ ìƒí’ˆ ê°œìˆ˜: " + (products != null ? products.size() : 0));
	    
	    return products;
	}
	
	/**
	 * ìž¬ê³  ì°¨ê° (ì£¼ë¬¸ ì‹œ í˜¸ì¶œ)
	 */
	@Override
	@Transactional
	public void decreaseStock(int prodNo, int quantity) throws Exception {
	    System.out.println("==> ProductServiceImpl.decreaseStock() ì‹œìž‘");
	    System.out.println("    prodNo: " + prodNo + ", quantity: " + quantity);
	    
	    // 1. ìž…ë ¥ê°’ ê²€ì¦
	    if (prodNo <= 0) {
	        throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
	    }
	    
	    if (quantity <= 0) {
	        throw new IllegalArgumentException("ì°¨ê° ìˆ˜ëŸ‰ì€ 1 ì´ìƒì´ì–´ì•¼ í•©ë‹ˆë‹¤: " + quantity);
	    }
	    
	    // 2. ìƒí’ˆ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
	    Product product = productDao.getProduct(prodNo);
	    if (product == null) {
	        throw new Exception("ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. prodNo: " + prodNo);
	    }
	    
	    // 3. í˜„ìž¬ ìž¬ê³  í™•ì¸
	    int currentStock = product.getStock();
	    System.out.println("==> í˜„ìž¬ ìž¬ê³ : " + currentStock);
	    
	    // 4. ìž¬ê³  ë¶€ì¡± ì²´í¬
	    if (currentStock < quantity) {
	        throw new Exception("ìž¬ê³ ê°€ ë¶€ì¡±í•©ë‹ˆë‹¤. (í˜„ìž¬ ìž¬ê³ : " + currentStock + 
	                          ", ìš”ì²­ ìˆ˜ëŸ‰: " + quantity + ")");
	    }
	    
	    // 5. ìž¬ê³  ì°¨ê°
	    int newStock = currentStock - quantity;
	    productDao.updateStock(prodNo, newStock);
	    
	    System.out.println("==> ìž¬ê³  ì°¨ê° ì™„ë£Œ: " + currentStock + " â†’ " + newStock);
	}

	/**
	 * ìž¬ê³  ì¦ê°€ (ì£¼ë¬¸ ì·¨ì†Œ ì‹œ í˜¸ì¶œ)
	 */
	@Override
	@Transactional
	public void increaseStock(int prodNo, int quantity) throws Exception {
	    System.out.println("==> ProductServiceImpl.increaseStock() ì‹œìž‘");
	    System.out.println("    prodNo: " + prodNo + ", quantity: " + quantity);
	    
	    // 1. ìž…ë ¥ê°’ ê²€ì¦
	    if (prodNo <= 0) {
	        throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
	    }
	    
	    if (quantity <= 0) {
	        throw new IllegalArgumentException("ì¦ê°€ ìˆ˜ëŸ‰ì€ 1 ì´ìƒì´ì–´ì•¼ í•©ë‹ˆë‹¤: " + quantity);
	    }
	    
	    // 2. ìƒí’ˆ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
	    Product product = productDao.getProduct(prodNo);
	    if (product == null) {
	        throw new Exception("ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. prodNo: " + prodNo);
	    }
	    
	    // 3. ìž¬ê³  ì¦ê°€
	    int currentStock = product.getStock();
	    int newStock = currentStock + quantity;
	    productDao.updateStock(prodNo, newStock);
	    
	    System.out.println("==> ìž¬ê³  ì¦ê°€ ì™„ë£Œ: " + currentStock + " â†’ " + newStock);
	}

	/**
	 * ìž¬ê³  í™•ì¸
	 */
	@Override
	public int getStock(int prodNo) throws Exception {
	    System.out.println("==> ProductServiceImpl.getStock() ì‹œìž‘, prodNo: " + prodNo);
	    
	    if (prodNo <= 0) {
	        throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
	    }
	    
	    Product product = productDao.getProduct(prodNo);
	    if (product == null) {
	        throw new Exception("ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. prodNo: " + prodNo);
	    }
	    
	    int stock = product.getStock();
	    System.out.println("==> í˜„ìž¬ ìž¬ê³ : " + stock);
	    
	    return stock;
	}

	/**
	 * ìž¬ê³  ì§ì ‘ ì„¤ì • (ê´€ë¦¬ìžìš©)
	 */
	@Override
	@Transactional
	public void setStock(int prodNo, int stock) throws Exception {
	    System.out.println("==> ProductServiceImpl.setStock() ì‹œìž‘");
	    System.out.println("    prodNo: " + prodNo + ", stock: " + stock);
	    
	    // 1. ìž…ë ¥ê°’ ê²€ì¦
	    if (prodNo <= 0) {
	        throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ ìƒí’ˆë²ˆí˜¸ìž…ë‹ˆë‹¤: " + prodNo);
	    }
	    
	    if (stock < 0) {
	        throw new IllegalArgumentException("ìž¬ê³ ëŠ” 0 ì´ìƒì´ì–´ì•¼ í•©ë‹ˆë‹¤: " + stock);
	    }
	    
	    // 2. ìƒí’ˆ ì¡´ìž¬ ì—¬ë¶€ í™•ì¸
	    Product product = productDao.getProduct(prodNo);
	    if (product == null) {
	        throw new Exception("ìƒí’ˆì„ ì°¾ì„ ìˆ˜ ì—†ìŠµë‹ˆë‹¤. prodNo: " + prodNo);
	    }
	    
	    // 3. ìž¬ê³  ì„¤ì •
	    productDao.updateStock(prodNo, stock);
	    
	    System.out.println("==> ìž¬ê³  ì„¤ì • ì™„ë£Œ: " + stock);
	}
	
	// ProductServiceImpl.javaì˜ getProductListWithStatus ë©”ì†Œë“œ ìˆ˜ì •

	
}