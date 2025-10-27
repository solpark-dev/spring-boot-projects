package com.model2.mvc.service.cart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.service.cart.CartDao;
import com.model2.mvc.service.domain.Cart;

/**
 * Cart DAO 구현체
 * MyBatis SqlSession을 이용한 장바구니 데이터베이스 접근
 */
@Repository("cartDaoImpl")
public class CartDaoImpl implements CartDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public CartDaoImpl() {
		System.out.println(this.getClass() + " 생성자 호출");
	}
	
	/**
	 * 장바구니에 상품 추가
	 */
	@Override
	public void addCart(Cart cart) throws Exception {
		System.out.println("==> CartDaoImpl.addCart() 시작");
		
		if (cart == null) {
			throw new IllegalArgumentException("Cart 객체가 null입니다.");
		}
		
		sqlSession.insert("CartMapper.addCart", cart);
		System.out.println("==> 장바구니 추가 완료, cartId: " + cart.getCartId());
	}
	
	/**
	 * 사용자별 장바구니 목록 조회
	 */
	@Override
	public List<Cart> getCartListByUserNo(int userNo) throws Exception {
		System.out.println("==> CartDaoImpl.getCartListByUserNo() 시작, userNo: " + userNo);
		
		List<Cart> cartList = sqlSession.selectList("CartMapper.getCartListByUserNo", userNo);
		
		System.out.println("==> 조회된 장바구니 개수: " + (cartList != null ? cartList.size() : 0));
		return cartList;
	}
	
	/**
	 * 특정 장바구니 항목 조회
	 */
	@Override
	public Cart getCartItem(int cartId) throws Exception {
		System.out.println("==> CartDaoImpl.getCartItem() 시작, cartId: " + cartId);
		
		Cart cart = sqlSession.selectOne("CartMapper.getCartItem", cartId);
		
		System.out.println("==> 조회 결과: " + (cart != null ? "존재" : "없음"));
		return cart;
	}
	
	/**
	 * 중복 체크 (같은 사용자가 같은 상품을 이미 담았는지)
	 */
	@Override
	public Cart getCartByUserAndProduct(int userNo, int prodNo) throws Exception {
		System.out.println("==> CartDaoImpl.getCartByUserAndProduct() 시작");
		System.out.println("    userNo: " + userNo + ", prodNo: " + prodNo);
		
		// MyBatis에 여러 파라미터를 전달하려면 Map 사용
		Map<String, Object> params = new HashMap<>();
		params.put("userNo", userNo);
		params.put("prodNo", prodNo);
		
		Cart cart = sqlSession.selectOne("CartMapper.getCartByUserAndProduct", params);
		
		System.out.println("==> 중복 체크 결과: " + (cart != null ? "이미 존재" : "존재하지 않음"));
		return cart;
	}
	
	/**
	 * 장바구니 수량 변경
	 */
	@Override
	public void updateCartQuantity(Cart cart) throws Exception {
		System.out.println("==> CartDaoImpl.updateCartQuantity() 시작");
		System.out.println("    cartId: " + cart.getCartId() + ", quantity: " + cart.getQuantity());
		
		sqlSession.update("CartMapper.updateCartQuantity", cart);
		System.out.println("==> 수량 변경 완료");
	}
	
	/**
	 * 장바구니 항목 삭제 (단일)
	 */
	@Override
	public void deleteCart(int cartId) throws Exception {
		System.out.println("==> CartDaoImpl.deleteCart() 시작, cartId: " + cartId);
		
		sqlSession.delete("CartMapper.deleteCart", cartId);
		System.out.println("==> 장바구니 항목 삭제 완료");
	}
	
	/**
	 * 장바구니 항목 삭제 (여러 개)
	 */
	@Override
	public void deleteCartItems(List<Integer> cartIdList) throws Exception {
		System.out.println("==> CartDaoImpl.deleteCartItems() 시작");
		System.out.println("    삭제할 항목 개수: " + (cartIdList != null ? cartIdList.size() : 0));
		
		if (cartIdList == null || cartIdList.isEmpty()) {
			System.out.println("==> 삭제할 항목이 없습니다.");
			return;
		}
		
		sqlSession.delete("CartMapper.deleteCartItems", cartIdList);
		System.out.println("==> 여러 장바구니 항목 삭제 완료");
	}
	
	/**
	 * 사용자의 전체 장바구니 비우기
	 */
	@Override
	public void clearCartByUserNo(int userNo) throws Exception {
		System.out.println("==> CartDaoImpl.clearCartByUserNo() 시작, userNo: " + userNo);
		
		sqlSession.delete("CartMapper.clearCartByUserNo", userNo);
		System.out.println("==> 전체 장바구니 비우기 완료");
	}
	
	/**
	 * 장바구니 총 개수 조회
	 */
	@Override
	public int getCartCount(int userNo) throws Exception {
		System.out.println("==> CartDaoImpl.getCartCount() 시작, userNo: " + userNo);
		
		Integer count = sqlSession.selectOne("CartMapper.getCartCount", userNo);
		int result = (count != null) ? count : 0;
		
		System.out.println("==> 장바구니 개수: " + result);
		return result;
	}
}