package com.model2.mvc.service.product;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.ProductFile;

/**
 * 상품 관리 Service 인터페이스
 */
public interface ProductService {
    
    void addProduct(Product product) throws Exception;
    
    Product getProduct(int prodNo) throws Exception;
    
    Map<String, Object> getProductList(Search search) throws Exception;
    
    void updateProduct(Product product) throws Exception;
    
    Product getProductWithStatus(int prodNo) throws Exception;
    
    Map<String, Object> getProductListWithStatus(Search search) throws Exception;
    
    void addProductFile(ProductFile productFile) throws Exception;
    
    List<ProductFile> getProductFiles(int prodNo) throws Exception;
    
    ProductFile getProductFile(int fileId) throws Exception;
    
    void deleteProductFile(int fileId) throws Exception;
    
    void deleteProductFiles(int prodNo) throws Exception;
    
    void deleteProduct(int prodNo) throws Exception;
    
    List<Product> getTopProducts(int limit) throws Exception;
    
    void decreaseStock(int prodNo, int quantity) throws Exception;
    
    void increaseStock(int prodNo, int quantity) throws Exception;
    
    int getStock(int prodNo) throws Exception;
    
    void setStock(int prodNo, int stock) throws Exception;
}
