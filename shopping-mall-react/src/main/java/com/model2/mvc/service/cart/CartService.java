package com.model2.mvc.service.cart;

import java.util.List;
import com.model2.mvc.service.domain.Cart;

public interface CartService {
    Cart addToCart(int userNo, int prodNo, int quantity) throws Exception;
    List<Cart> getCartList(int userNo) throws Exception;
    Cart getCartItem(int cartId) throws Exception;
    void updateCartQuantity(int cartId, int quantity) throws Exception;
    void removeFromCart(int cartId) throws Exception;
    void removeCartItems(List<Integer> cartIdList) throws Exception;
    void clearCart(int userNo) throws Exception;
    int getCartCount(int userNo) throws Exception;
}
