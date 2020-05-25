package com.application.web.security.auth;

import com.application.service.UserService;
import com.application.service.impl.UserServiceImpl;
import com.application.commons.utils.BeanUtil;
import com.application.dao.entities.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter  extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        UserService userService = BeanUtil.getBean(UserServiceImpl.class);
        UserInfo userInfo = userService.authenticateAndFetchUserInfo(request);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        log.info("Passing control to Filter Chain for Auth verification ");
        filterChain.doFilter(request, response);
        log.info("Returning control from Filter Chain Auth verification ");
    }
}
