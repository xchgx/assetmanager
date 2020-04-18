package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.Log;
import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "logFilter", urlPatterns = "/*")
public class ALogFilter implements Filter {
    @Autowired
    private LogRepository logRepository;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute("user");

        Log log = new Log();
        log.setClientIp(request.getRemoteAddr());
        log.setClientHost(request.getRemoteHost());
        log.setClientPort(request.getRemotePort());
        log.setServerIp(request.getLocalAddr());
        log.setServerName(request.getLocalName());
        log.setUrl(request.getRequestURL().toString());
        log.setUsername(user==null?"未登录":user.getUsername());
        log.setVisit(new Date());
        log.setUserAgent(request.getHeader("user-agent"));
        logRepository.save(log);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
