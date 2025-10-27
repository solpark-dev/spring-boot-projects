package com.model2.mvc.service.purchase.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public PurchaseDaoImpl() {
		System.out.println(this.getClass() + " 생성자 호출");
	}

	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
	}
	
	// ▼▼▼ 추가된 메소드 구현부 ▼▼▼
	@Override
	public void addAddress(Purchase purchase) throws Exception {
		System.out.println("==> PurchaseDaoImpl.addAddress() 시작");
		sqlSession.insert("PurchaseMapper.addAddress", purchase);
		System.out.println("==> 신규 배송지 등록 완료, 생성된 addressId: " + purchase.getAddressId());
	}

	@Override
	public void addOrderDetail(int orderNo, int prodNo, int quantity, int pricePerItem) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("orderNo", orderNo);
		map.put("prodNo", prodNo);
		map.put("quantity", quantity);
		map.put("pricePerItem", pricePerItem);
		sqlSession.insert("PurchaseMapper.addOrderDetail", map);
	}
	
	@Override
	public void addPayment(int orderNo, String paymentMethod, int amount) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("orderNo", orderNo);
		map.put("paymentMethod", paymentMethod);
		map.put("amount", amount);
		sqlSession.insert("PurchaseMapper.addPayment", map);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
	}

	@Override
	public List<Purchase> getPurchaseList(Search search, String buyerId) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
	}

	@Override
	public int getTotalCount(String buyerId) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId);
	}
	
	@Override
	public Purchase getPurchaseByProdNo(int prodNo) throws Exception {
	    return sqlSession.selectOne("PurchaseMapper.getPurchaseByProdNo", prodNo);
	}
}