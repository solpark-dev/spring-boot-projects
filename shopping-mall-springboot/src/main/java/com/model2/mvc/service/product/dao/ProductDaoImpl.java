package com.model2.mvc.service.product.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.ProductFile;
import com.model2.mvc.service.product.ProductDao;

@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao {

	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	///Constructor
	public ProductDaoImpl() {
		System.out.println(this.getClass());
	}
	
	///Method
	@Override
	public void addProduct(Product product) throws Exception {
		sqlSession.insert("ProductMapper.addProduct", product);
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		return sqlSession.selectOne("ProductMapper.getProduct", prodNo);
	}

	@Override
	public List<Product> getProductList(Search search) throws Exception {
		return sqlSession.selectList("ProductMapper.getProductList", search);
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		sqlSession.update("ProductMapper.updateProduct", product);
	}

	@Override
	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	}
	
	// íŒë§¤ìƒíƒœ í¬í•¨ ì¡°íšŒ ë©”ì„œë“œë“¤
	@Override
	public Product getProductWithStatus(int prodNo) throws Exception {
	    return sqlSession.selectOne("ProductMapper.getProductWithStatus", prodNo);
	}

	@Override
	public List<Product> getProductListWithStatus(Search search) throws Exception {
	    return sqlSession.selectList("ProductMapper.getProductListWithStatus", search);
	}

	// ===============================================
	// íŒŒì¼ ê´€ë ¨ ë©”ì„œë“œë“¤ (ë‹¤ì¤‘ íŒŒì¼ ì—…ë¡œë“œ ì§€ì›)
	// ===============================================
	
	@Override
	public void addProductFile(ProductFile productFile) throws Exception {
		System.out.println("==> ProductDaoImpl.addProductFile() ì‹œìž‘");
		System.out.println("    íŒŒì¼ ì •ë³´: " + productFile.getOriginalName());
		
		int result = sqlSession.insert("ProductMapper.addProductFile", productFile);
		
		if (result == 1) {
			System.out.println("==> íŒŒì¼ ì •ë³´ DB ì €ìž¥ ì„±ê³µ: " + productFile.getOriginalName());
		} else {
			throw new Exception("íŒŒì¼ ì •ë³´ ì €ìž¥ì— ì‹¤íŒ¨í–ˆìŠµë‹ˆë‹¤.");
		}
	}

	@Override
	public List<ProductFile> getProductFiles(int prodNo) throws Exception {
		System.out.println("==> ProductDaoImpl.getProductFiles() ì‹œìž‘, prodNo: " + prodNo);
		
		List<ProductFile> productFiles = sqlSession.selectList("ProductMapper.getProductFiles", prodNo);
		
		System.out.println("==> ì¡°íšŒëœ íŒŒì¼ ê°œìˆ˜: " + (productFiles != null ? productFiles.size() : 0));
		
		return productFiles;
	}

	@Override
	public ProductFile getProductFile(int fileId) throws Exception {
		System.out.println("==> ProductDaoImpl.getProductFile() ì‹œìž‘, fileId: " + fileId);
		
		ProductFile productFile = sqlSession.selectOne("ProductMapper.getProductFile", fileId);
		
		if (productFile != null) {
			System.out.println("==> íŒŒì¼ ì¡°íšŒ ì„±ê³µ: " + productFile.getOriginalName());
		} else {
			System.out.println("==> íŒŒì¼ ID " + fileId + "ì— í•´ë‹¹í•˜ëŠ” íŒŒì¼ì´ ì—†ìŠµë‹ˆë‹¤.");
		}
		
		return productFile;
	}

	@Override
	public void deleteProductFile(int fileId) throws Exception {
		System.out.println("==> ProductDaoImpl.deleteProductFile() ì‹œìž‘, fileId: " + fileId);
		
		// ìž…ë ¥ê°’ ê²€ì¦
		if (fileId <= 0) {
			throw new IllegalArgumentException("ìœ íš¨í•˜ì§€ ì•Šì€ íŒŒì¼ IDìž…ë‹ˆë‹¤: " + fileId);
		}
		
		int result = sqlSession.delete("ProductMapper.deleteProductFile", fileId);
		
		if (result == 1) {
			System.out.println("==> íŒŒì¼ ì •ë³´ ì‚­ì œ ì„±ê³µ, fileId: " + fileId);
		} else {
			System.out.println("==> ì‚­ì œí•  íŒŒì¼ì´ ì—†ìŠµë‹ˆë‹¤, fileId: " + fileId);
			// íŒŒì¼ì´ ì—†ì–´ë„ ì˜ˆì™¸ë¥¼ ë˜ì§€ì§€ ì•ŠìŒ (ì´ë¯¸ ì‚­ì œëœ ê²½ìš°)
		}
	}
	
	public void deleteProduct(int prodNo) throws Exception {
	    sqlSession.delete("ProductMapper.deleteProduct", prodNo);
	}
	
	// ProductDaoImpl.javaì— ì¶”ê°€
	@Override
	public List<Product> getTopProducts(int limit) throws Exception {
	    System.out.println("==> ProductDaoImpl.getTopProducts() ì‹œìž‘, limit: " + limit);
	    return sqlSession.selectList("ProductMapper.getTopProducts", limit);
	}
	
	// ProductDaoImpl.java í´ëž˜ìŠ¤ì— ì¶”ê°€
	@Override
	public int getFilteredTotalCount(Search search) throws Exception {
	    return sqlSession.selectOne("ProductMapper.getFilteredTotalCount", search);
	}
	
	/**
	 * ìž¬ê³  ì—…ë°ì´íŠ¸
	 */
	@Override
	public void updateStock(int prodNo, int stock) throws Exception {
	    System.out.println("==> ProductDaoImpl.updateStock() ì‹œìž‘");
	    System.out.println("    prodNo: " + prodNo + ", stock: " + stock);
	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("prodNo", prodNo);
	    params.put("stock", stock);
	    
	    sqlSession.update("ProductMapper.updateStock", params);
	    System.out.println("==> ìž¬ê³  ì—…ë°ì´íŠ¸ ì™„ë£Œ");
	}
}