package com.model2.mvc.service.user;

import java.util.List;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.User;

public interface UserDao {
    void addUser(User user) throws Exception;
    void addSocialUser(User user) throws Exception;
    User getUser(String userId) throws Exception;
    User getUserByUserNo(int userNo) throws Exception;
    List<User> getUserList(Search search) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(String userId) throws Exception;
    int getTotalCount(Search search) throws Exception;
}
