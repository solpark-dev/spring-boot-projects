// src/main/java/com/model2/mvc/service/social/impl/SocialAccountDaoImpl.java
package com.model2.mvc.service.social.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.service.domain.SocialAccount;
import com.model2.mvc.service.social.SocialAccountDao;

/**
 * 소셜 계정 연동 정보 DAO 구현
 */
@Repository("socialAccountDaoImpl")
public class SocialAccountDaoImpl implements SocialAccountDao {
    
    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;
    
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
    
    public SocialAccountDaoImpl() {
        System.out.println(this.getClass());
    }
    
    @Override
    public void addSocialAccount(SocialAccount socialAccount) throws Exception {
        System.out.println("==> SocialAccountDaoImpl.addSocialAccount() 시작");
        sqlSession.insert("SocialAccountMapper.addSocialAccount", socialAccount);
        System.out.println("==> 소셜 계정 등록 완료: " + socialAccount);
    }
    
    @Override
    public SocialAccount findByProviderAndProviderId(String provider, String providerUserId) throws Exception {
        System.out.println("==> SocialAccountDaoImpl.findByProviderAndProviderId() 시작");
        System.out.println("    provider: " + provider + ", providerUserId: " + providerUserId);
        
        Map<String, Object> params = new HashMap<>();
        params.put("provider", provider);
        params.put("providerUserId", providerUserId);
        
        SocialAccount result = sqlSession.selectOne("SocialAccountMapper.findByProviderAndProviderId", params);
        System.out.println("==> 조회 결과: " + result);
        
        return result;
    }
    
    @Override
    public List<SocialAccount> findByUserNo(int userNo) throws Exception {
        System.out.println("==> SocialAccountDaoImpl.findByUserNo() 시작");
        System.out.println("    userNo: " + userNo);
        
        List<SocialAccount> list = sqlSession.selectList("SocialAccountMapper.findByUserNo", userNo);
        System.out.println("==> 조회된 소셜 계정 개수: " + (list != null ? list.size() : 0));
        
        return list;
    }
    
    @Override
    public void deleteSocialAccount(int socialAccountId) throws Exception {
        System.out.println("==> SocialAccountDaoImpl.deleteSocialAccount() 시작");
        System.out.println("    socialAccountId: " + socialAccountId);
        
        sqlSession.delete("SocialAccountMapper.deleteSocialAccount", socialAccountId);
        System.out.println("==> 소셜 계정 삭제 완료");
    }
    
    @Override
    public void updateSocialAccount(SocialAccount socialAccount) throws Exception {
        System.out.println("==> SocialAccountDaoImpl.updateSocialAccount() 시작");
        sqlSession.update("SocialAccountMapper.updateSocialAccount", socialAccount);
        System.out.println("==> 소셜 계정 업데이트 완료: " + socialAccount);
    }
}
