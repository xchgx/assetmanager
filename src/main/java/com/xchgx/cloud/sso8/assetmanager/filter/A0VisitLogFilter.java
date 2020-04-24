package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "logFilter",urlPatterns = "/*")
public class A0VisitLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL = " + requestURL);

        String queryString = request.getQueryString();
        System.out.println("queryString = " + queryString);

        String remoteAddr = request.getRemoteAddr();
        System.out.println("remoteAddr = " + remoteAddr);

        int remotePort = request.getRemotePort();
        System.out.println("remotePort = " + remotePort);

        String header = request.getHeader("user-agent");
        System.out.println("header = " + header);

        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            System.out.println("无人登录 ");
        }else{
            System.out.println("user = " + user);
        }
        System.out.println("new Date().toLocaleString() = " + new Date().toLocaleString());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
