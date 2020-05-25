package com.application.service;

import com.application.dao.entities.UserInfo;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    UserInfo authenticateAndFetchUserInfo(HttpServletRequest httpServletRequest);
}
