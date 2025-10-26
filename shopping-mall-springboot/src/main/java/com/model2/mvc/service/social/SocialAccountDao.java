package com.model2.mvc.service.social;

import java.util.List;
import com.model2.mvc.service.domain.SocialAccount;

public interface SocialAccountDao {
    void addSocialAccount(SocialAccount socialAccount) throws Exception;
    SocialAccount findByProviderAndProviderId(String provider, String providerUserId) throws Exception;
    List<SocialAccount> findByUserNo(int userNo) throws Exception;
    void deleteSocialAccount(int socialAccountId) throws Exception;
    void updateSocialAccount(SocialAccount socialAccount) throws Exception;
}
