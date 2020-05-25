package com.application.service.impl;

import com.google.common.base.Preconditions;
import com.application.service.UserService;
import com.application.commons.config.crypto.CryptoConfig;
import com.application.dao.entities.UserInfo;
import com.application.dao.jparepository.UserInfoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    UserInfoDao userInfoDao;

    @Autowired
    CryptoConfig cryptoConfig;

    @Override
    public UserInfo authenticateAndFetchUserInfo(HttpServletRequest httpServletRequest){
        String clientEmail = httpServletRequest.getHeader("x-client-email");
        String password = httpServletRequest.getHeader("x-client-password");
        UserInfo userInfo = userInfoDao.findByClientEmailAndIsActiveTrue(clientEmail);
        String encodedPassword = cryptoConfig.encrypt(password);
        Preconditions.checkArgument(userInfo.getEncodedClientSecret().equals(encodedPassword), " Invalid passwords");
        return userInfo;
    }


}
