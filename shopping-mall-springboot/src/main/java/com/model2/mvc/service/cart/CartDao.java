package com.model2.mvc.service.cart;

import java.util.List;
import com.model2.mvc.service.domain.Cart;

public interface CartDao {
    void addCart(Cart cart) throws Exception;
    List<Cart> getCartListByUserNo(int userNo) throws Exception;
    Cart getCartItem(int cartId) throws Exception;
    Cart getCartByUserAndProduct(int userNo, int prodNo) throws Exception;
    void updateCartQuantity(Cart cart) throws Exception;
    void deleteCart(int cartId) throws Exception;
    void deleteCartItems(List<Integer> cartIdList) throws Exception;
    void clearCartByUserNo(int userNo) throws Exception;
    int getCartCount(int userNo) throws Exception;
}
