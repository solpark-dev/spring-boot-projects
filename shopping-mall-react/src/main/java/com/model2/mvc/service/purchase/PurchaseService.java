package com.model2.mvc.service.purchase;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseService {
    void addPurchase(Purchase purchase) throws Exception;
    Purchase getPurchase(int tranNo) throws Exception;
    Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;
    void updatePurchase(Purchase purchase) throws Exception;
    void updateTranCode(Purchase purchase) throws Exception;
    Purchase getPurchaseByProdNo(int prodNo) throws Exception;
    void cancelPurchase(int tranNo) throws Exception;
}
