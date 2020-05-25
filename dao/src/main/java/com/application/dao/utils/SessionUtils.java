package com.application.dao.utils;

import com.application.dao.entities.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionUtils {

    public static UserInfo getLoggedInUser(){
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
