package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.User;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器，匹配规则的就执行过滤器
 *  http://localhost:8080/rukudan/add
 *  http://localhost:8080/asset/add
 *  http://localhost:8080/asset1/add //不检查，因为不匹配
 */
@WebFilter(filterName = "userFilter", urlPatterns = {"/user/*"})
@Order(1)//优先执行，第一个执行的过滤器
public class A2UserFilter implements Filter {

    /**
     * 匹配上面的三个规则就执行该过滤器
     * @param servletRequest 请求
     * @param servletResponse 响应
     * @param filterChain 过滤链
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute("user");
        System.out.println("A2UserFilter.doFilter");
        if(user == null || !user.getRole().equals("user")){
            System.out.println("user = " + user);
            response.sendRedirect("/login/");//进入到登录页
            return;
        }
        System.out.println(user);
        filterChain.doFilter(servletRequest, servletResponse);//放行
    }

}