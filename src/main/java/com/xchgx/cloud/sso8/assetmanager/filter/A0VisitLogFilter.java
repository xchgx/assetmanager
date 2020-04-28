package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.domain.VisitLog;
import com.xchgx.cloud.sso8.assetmanager.repository.VisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "logFilter",urlPatterns = "/*")
public class A0VisitLogFilter implements Filter {
    @Autowired
    private VisitLogRepository visitLogRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        StringBuffer requestURL = request.getRequestURL();
        System.out.println("requestURL = " + requestURL);//完整URL
        System.out.println("handleUrl(requestURL) = " + handleUrl(requestURL.toString()));//完整URL

        String queryString = request.getQueryString();
        System.out.println("queryString = " + queryString);

        String remoteAddr = request.getRemoteAddr();
        System.out.println("remoteAddr = " + remoteAddr);//IP地址
        String localAddr = request.getLocalAddr();
        System.out.println("localAddr = " + localAddr);

        int remotePort = request.getRemotePort();
        System.out.println("remotePort = " + remotePort);

        String header = request.getHeader("user-agent");
        System.out.println("header = " + header);//浏览器信息
        System.out.println("handleOs(header) = " + handleOs(header));//浏览器信息

        //日志记录类需要哪些属性
        Date time = new Date();

        User user = (User) request.getSession().getAttribute("user");
        String username = "";
        String name = "";
        if(user==null){
            System.out.println("未登录 ");
            username="未登录";
            name = "无";
        }else{
            System.out.println("user = " + user);
            username=user.getUsername();
            name = user.getName();
        }
        System.out.println("new Date().toLocaleString() = " + new Date().toLocaleString());


        VisitLog visitLog = new VisitLog(remoteAddr,handleOs(header),handleUrl(requestURL.toString()),time,username,name);
        visitLog.setMethod(request.getMethod());
        visitLog.setBrowser(header);

        visitLogRepository.save(visitLog);
        servletRequest.setAttribute("log", visitLog);//将日志记录放入到过滤链的请求对象中

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 将完整URL，经过加工后，得到系统的访问接口地址 /application/add
     * http://xchgx.vicp.net/application/add
     * https://xchgx.vicp.net/application/add
     * @param url
     * @return
     */
    public String handleUrl(String url){//   http://xchgx.vicp.net/application/add
        int i = url.indexOf("//");//找出双斜杠的位置
        String url2 = url.substring(i+"//".length());//    xchgx.vicp.net/application/add
        int i2 = url2.indexOf("/");
        String url3 = url2.substring(i2); //        /application/add
        return url3;
    }

    /**
     * 从浏览器信息中获得操作系统
     * @param userAgent
     * @return
     */
    public String handleOs(String userAgent){
        if(userAgent.toLowerCase().contains("windows")) {//先全部转换成小写字母，然后使用包含方法。
            return "Windows";//这是Windows操作系统
        }
        if(userAgent.toLowerCase().contains("android")){
            return "Android";
        }
        if(userAgent.toLowerCase().contains("iphone")){
            return "IOS";
        }
        if(userAgent.toLowerCase().contains("linux")){
            return "Linux";
        }
        return "未知操作系统:"+userAgent;
    }
}//类结束大括号
