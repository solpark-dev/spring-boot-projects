package com.model2.mvc.service.cart.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model2.mvc.service.cart.CartDao;
import com.model2.mvc.service.cart.CartService;
import com.model2.mvc.service.domain.Cart;

/**
 * Cart Service 구현체
 * 장바구니 관련 비즈니스 로직 처리
 */
@Service("cartServiceImpl")
public class CartServiceImpl implements CartService {

	@Autowired
	@Qualifier("cartDaoImpl")
	private CartDao cartDao;
	
	public void setCartDao(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	public CartServiceImpl() {
		System.out.println(this.getClass());
	}
	
	/**
	 * 장바구니에 상품 추가
	 * - 핵심 비즈니스 로직: 중복 체크!
	 */
	@Override
	@Transactional
	public Cart addToCart(int userNo, int prodNo, int quantity) throws Exception {
		System.out.println("==> CartServiceImpl.addToCart() 시작");
		System.out.println("    userNo: " + userNo + ", prodNo: " + prodNo + ", quantity: " + quantity);
		
		// 1. 유효성 검사
		if (quantity < 1) {
			throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
		}
		
		// 2. 중복 체크 (이미 장바구니에 있는지 확인)
		Cart existingCart = cartDao.getCartByUserAndProduct(userNo, prodNo);
		
		if (existingCart != null) {
			// 이미 있으면 수량만 증가
			System.out.println("==> 이미 장바구니에 존재하는 상품입니다. 수량 증가 처리");
			
			int newQuantity = existingCart.getQuantity() + quantity;
			existingCart.setQuantity(newQuantity);
			
			cartDao.updateCartQuantity(existingCart);
			System.out.println("==> 수량 증가 완료: " + existingCart.getQuantity());
			
			return existingCart;
			
		} else {
			// 없으면 새로 추가
			System.out.println("==> 새로운 상품을 장바구니에 추가합니다.");
			
			Cart newCart = new Cart();
			newCart.setUserNo(userNo);
			newCart.setProdNo(prodNo);
			newCart.setQuantity(quantity);
			
			cartDao.addCart(newCart);
			System.out.println("==> 장바구니 추가 완료, cartId: " + newCart.getCartId());
			
			return newCart;
		}
	}
	
	/**
	 * 사용자별 장바구니 목록 조회
	 */
	@Override
	public List<Cart> getCartList(int userNo) throws Exception {
		System.out.println("==> CartServiceImpl.getCartList() 시작, userNo: " + userNo);
		
		List<Cart> cartList = cartDao.getCartListByUserNo(userNo);
		
		System.out.println("==> 조회 완료, 장바구니 개수: " + (cartList != null ? cartList.size() : 0));
		return cartList;
	}
	
	/**
	 * 특정 장바구니 항목 조회
	 */
	@Override
	public Cart getCartItem(int cartId) throws Exception {
		System.out.println("==> CartServiceImpl.getCartItem() 시작, cartId: " + cartId);
		
		Cart cart = cartDao.getCartItem(cartId);
		
		if (cart == null) {
			throw new Exception("해당 장바구니 항목을 찾을 수 없습니다. cartId: " + cartId);
		}
		
		return cart;
	}
	
	/**
	 * 장바구니 수량 변경
	 */
	@Override
	@Transactional
	public void updateCartQuantity(int cartId, int quantity) throws Exception {
		System.out.println("==> CartServiceImpl.updateCartQuantity() 시작");
		System.out.println("    cartId: " + cartId + ", quantity: " + quantity);
		
		// 1. 유효성 검사
		if (quantity < 1) {
			throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
		}
		
		// 2. 장바구니 항목 존재 여부 확인
		Cart cart = cartDao.getCartItem(cartId);
		if (cart == null) {
			throw new Exception("해당 장바구니 항목을 찾을 수 없습니다. cartId: " + cartId);
		}
		
		// 3. 수량 변경
		cart.setQuantity(quantity);
		cartDao.updateCartQuantity(cart);
		
		System.out.println("==> 수량 변경 완료");
	}
	
	/**
	 * 장바구니 항목 삭제 (단일)
	 */
	@Override
	@Transactional
	public void removeFromCart(int cartId) throws Exception {
		System.out.println("==> CartServiceImpl.removeFromCart() 시작, cartId: " + cartId);
		
		// 장바구니 항목 존재 여부 확인
		Cart cart = cartDao.getCartItem(cartId);
		if (cart == null) {
			throw new Exception("해당 장바구니 항목을 찾을 수 없습니다. cartId: " + cartId);
		}
		
		cartDao.deleteCart(cartId);
		System.out.println("==> 장바구니 항목 삭제 완료");
	}
	
	/**
	 * 장바구니 항목 삭제 (여러 개)
	 */
	@Override
	@Transactional
	public void removeCartItems(List<Integer> cartIdList) throws Exception {
		System.out.println("==> CartServiceImpl.removeCartItems() 시작");
		System.out.println("    삭제할 항목 개수: " + (cartIdList != null ? cartIdList.size() : 0));
		
		if (cartIdList == null || cartIdList.isEmpty()) {
			throw new IllegalArgumentException("삭제할 항목이 없습니다.");
		}
		
		cartDao.deleteCartItems(cartIdList);
		System.out.println("==> 여러 장바구니 항목 삭제 완료");
	}
	
	/**
	 * 사용자의 전체 장바구니 비우기
	 */
	@Override
	@Transactional
	public void clearCart(int userNo) throws Exception {
		System.out.println("==> CartServiceImpl.clearCart() 시작, userNo: " + userNo);
		
		cartDao.clearCartByUserNo(userNo);
		System.out.println("==> 전체 장바구니 비우기 완료");
	}
	
	/**
	 * 장바구니 총 개수 조회
	 */
	@Override
	public int getCartCount(int userNo) throws Exception {
		System.out.println("==> CartServiceImpl.getCartCount() 시작, userNo: " + userNo);
		
		int count = cartDao.getCartCount(userNo);
		
		System.out.println("==> 장바구니 개수: " + count);
		return count;
	}
}