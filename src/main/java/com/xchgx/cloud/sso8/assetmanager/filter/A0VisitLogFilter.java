package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.domain.VisitLog;
import com.xchgx.cloud.sso8.assetmanager.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "logFilter",urlPatterns = "/*")
public class A0VisitLogFilter implements Filter {
    @Autowired
    VisitLogRepository visitLogRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute("user");
        System.out.println("A0LogFilter.doFilter");
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL = " + requestURL);
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        String queryString = request.getQueryString();
        System.out.println("queryString = " + queryString);

        String method = request.getMethod();
        System.out.println("method = " + method);
        String remoteAddr = request.getRemoteAddr();
        System.out.println("remoteAddr = " + remoteAddr);
        int remotePort = request.getRemotePort();
        System.out.println("remotePort = " + remotePort);
        String header = request.getHeader("user-agent");
        System.out.println("header = " + header);
        String os = "windows";
        if (header.toLowerCase().contains("windows")){
            os = "windows";
        }else if(header.toLowerCase().contains("linux")){
            os = "linux";
        }else if(header.toLowerCase().contains("iphone")){
            os = "ios";
        }else if(header.toLowerCase().contains("android")){
            os = "android";
        }else{
            os = "other os";
        }

        if (user == null) {
            VisitLog log = new VisitLog(requestURL.toString(), remoteAddr, header, "未登录", "无名氏",queryString==null?"":queryString,method,os);
            visitLogRepository.save(log);
        }else{
            VisitLog log = new VisitLog(requestURL.toString(), remoteAddr, header, user.getUsername(), user.getName(),queryString==null?"":queryString,method,os);
            visitLogRepository.save(log);
        }

//        if(user == null){
//            System.out.println("user = " + user);
//            response.sendRedirect("/login/");//进入到登录页
//            return;
//        }
        System.out.println(user);
        filterChain.doFilter(servletRequest, servletResponse);//放行
    }
}
