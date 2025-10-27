package com.model2.mvc.service.product;

import java.util.List;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.ProductFile;

public interface ProductDao {
    void addProduct(Product product) throws Exception;
    Product getProduct(int prodNo) throws Exception;
    List<Product> getProductList(Search search) throws Exception;
    void updateProduct(Product product) throws Exception;
    int getTotalCount(Search search) throws Exception;
    Product getProductWithStatus(int prodNo) throws Exception;
    List<Product> getProductListWithStatus(Search search) throws Exception;
    void addProductFile(ProductFile productFile) throws Exception;
    List<ProductFile> getProductFiles(int prodNo) throws Exception;
    ProductFile getProductFile(int fileId) throws Exception;
    void deleteProductFile(int fileId) throws Exception;
    void deleteProduct(int prodNo) throws Exception;
    List<Product> getTopProducts(int limit) throws Exception;
    int getFilteredTotalCount(Search search) throws Exception;
    void updateStock(int prodNo, int stock) throws Exception;
}
