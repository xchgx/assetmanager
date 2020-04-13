package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 过滤所有的请求
 */
@WebFilter(filterName = "applicationFilter", urlPatterns = {"/application/addQuick"})
@Order(2)
public class ApplicationFilter implements Filter {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private AssetRepository assetRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("ApplicationFilter.doFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        System.out.println("type = " + type);
        long assetId = Long.parseLong(request.getParameter("assetId"));
        System.out.println("assetId = " + assetId);
        if (type == null) {
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null) {
            System.out.println("资产不存在");
            PrintWriter writer = response.getWriter();
            writer.println("<html><head><meta charset=utf8></head><body>");
            writer.println("<h1>资产ID无效</h1>");
            writer.println("<h1>3秒后回到首页</h1>");
            response.setContentType("text/html;charset=utf-8");
            response.setHeader("refresh","3;url=/index");
            writer.close();
            return;
        }

        switch (type){
            case "领用":
            case "借用":
                if (asset.getStatus().equals("空闲")){
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
            case "维修":
                if (asset.getStatus().equals("已使用") ||asset.getStatus().equals("借用")  ){
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
            case "报废":
                if (asset.getStatus().equals("维修")){
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
        }

        PrintWriter writer = response.getWriter();
        writer.println("<html><head><meta charset=utf8></head><body>");
        writer.println("<h1>提交申请无效</h1>");
        writer.println("<h1>5秒后回到首页</h1>");
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("refresh","5;url=/index");
        writer.close();
        return;
    }
}
