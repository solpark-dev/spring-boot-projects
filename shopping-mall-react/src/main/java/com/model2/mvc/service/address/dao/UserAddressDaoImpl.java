package com.model2.mvc.service.address.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.service.address.UserAddressDao;
import com.model2.mvc.service.domain.UserAddress;

/**
 * 배송지 DAO 구현체
 * MyBatis SqlSession을 이용한 데이터베이스 접근
 */
@Repository("userAddressDaoImpl")
public class UserAddressDaoImpl implements UserAddressDao {

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;
    
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public UserAddressDaoImpl() {
        System.out.println(this.getClass() + " 생성자 호출");
    }
    
    /**
     * 배송지 추가
     */
    @Override
    public void addAddress(UserAddress address) throws Exception {
        System.out.println("==> UserAddressDaoImpl.addAddress() 시작");
        
        if (address == null) {
            throw new IllegalArgumentException("UserAddress 객체가 null입니다.");
        }
        
        sqlSession.insert("UserAddressMapper.addAddress", address);
        System.out.println("==> 배송지 추가 완료, addressId: " + address.getAddressId());
    }
    
    /**
     * 배송지 조회 (단건)
     */
    @Override
    public UserAddress getAddress(int addressId) throws Exception {
        System.out.println("==> UserAddressDaoImpl.getAddress() 시작, addressId: " + addressId);
        
        UserAddress address = sqlSession.selectOne("UserAddressMapper.getAddress", addressId);
        
        System.out.println("==> 조회 결과: " + (address != null ? "존재" : "없음"));
        return address;
    }
    
    /**
     * 사용자별 배송지 목록 조회
     */
    @Override
    public List<UserAddress> getAddressListByUserNo(int userNo) throws Exception {
        System.out.println("==> UserAddressDaoImpl.getAddressListByUserNo() 시작, userNo: " + userNo);
        
        List<UserAddress> addressList = sqlSession.selectList("UserAddressMapper.getAddressListByUserNo", userNo);
        
        System.out.println("==> 조회된 배송지 개수: " + (addressList != null ? addressList.size() : 0));
        return addressList;
    }
    
    /**
     * 배송지 수정
     */
    @Override
    public void updateAddress(UserAddress address) throws Exception {
        System.out.println("==> UserAddressDaoImpl.updateAddress() 시작");
        System.out.println("    addressId: " + address.getAddressId());
        
        sqlSession.update("UserAddressMapper.updateAddress", address);
        System.out.println("==> 배송지 수정 완료");
    }
    
    /**
     * 배송지 삭제
     */
    @Override
    public void deleteAddress(int addressId) throws Exception {
        System.out.println("==> UserAddressDaoImpl.deleteAddress() 시작, addressId: " + addressId);
        
        sqlSession.delete("UserAddressMapper.deleteAddress", addressId);
        System.out.println("==> 배송지 삭제 완료");
    }
    
    /**
     * 기본 배송지 설정
     */
    @Override
    public void setDefaultAddress(int userNo, int addressId) throws Exception {
        System.out.println("==> UserAddressDaoImpl.setDefaultAddress() 시작");
        System.out.println("    userNo: " + userNo + ", addressId: " + addressId);
        
        Map<String, Object> params = new HashMap<>();
        params.put("userNo", userNo);
        params.put("addressId", addressId);
        
        sqlSession.update("UserAddressMapper.setDefaultAddress", params);
        System.out.println("==> 기본 배송지 설정 완료");
    }
    
    /**
     * 사용자의 모든 배송지 기본 설정 해제
     */
    @Override
    public void clearDefaultAddress(int userNo) throws Exception {
        System.out.println("==> UserAddressDaoImpl.clearDefaultAddress() 시작, userNo: " + userNo);
        
        sqlSession.update("UserAddressMapper.clearDefaultAddress", userNo);
        System.out.println("==> 기본 배송지 해제 완료");
    }
    
    /**
     * 사용자의 기본 배송지 조회
     */
    @Override
    public UserAddress getDefaultAddress(int userNo) throws Exception {
        System.out.println("==> UserAddressDaoImpl.getDefaultAddress() 시작, userNo: " + userNo);
        
        UserAddress address = sqlSession.selectOne("UserAddressMapper.getDefaultAddress", userNo);
        
        System.out.println("==> 기본 배송지 조회 결과: " + (address != null ? "존재" : "없음"));
        return address;
    }
}