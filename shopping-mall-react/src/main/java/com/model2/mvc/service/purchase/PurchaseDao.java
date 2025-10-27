package com.model2.mvc.service.purchase;

import java.util.List;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {
    void addPurchase(Purchase purchase) throws Exception;
    void addAddress(Purchase purchase) throws Exception;
    void addOrderDetail(int orderNo, int prodNo, int quantity, int pricePerItem) throws Exception;
    void addPayment(int orderNo, String paymentMethod, int amount) throws Exception;
    Purchase getPurchase(int tranNo) throws Exception;
    List<Purchase> getPurchaseList(Search search, String buyerId) throws Exception;
    void updatePurchase(Purchase purchase) throws Exception;
    void updateTranCode(Purchase purchase) throws Exception;
    int getTotalCount(String buyerId) throws Exception;
    Purchase getPurchaseByProdNo(int prodNo) throws Exception;
}
