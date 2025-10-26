package com.model2.mvc.service.address;

import java.util.List;
import com.model2.mvc.service.domain.UserAddress;

public interface UserAddressService {
    void addAddress(UserAddress address) throws Exception;
    UserAddress getAddress(int addressId) throws Exception;
    List<UserAddress> getAddressListByUserNo(int userNo) throws Exception;
    void updateAddress(UserAddress address) throws Exception;
    void deleteAddress(int addressId) throws Exception;
    void setDefaultAddress(int userNo, int addressId) throws Exception;
    UserAddress getDefaultAddress(int userNo) throws Exception;
}
