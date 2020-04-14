package com.xchgx.cloud.sso8.assetmanager.filter;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "applicationAddFilter",urlPatterns = {"/application/addQuick"})
public class BApplicationAddFilter implements Filter {
    @Autowired //自动注入资产持久化对象
    private AssetRepository assetRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //申请单提交的规则
        //     String type, long assetId
        String type = request.getParameter("type");//申请单的类型，领用？维修？
        long assetId = Long.parseLong(request.getParameter("assetId"));//申请单的类型，领用？维修？
        response.setCharacterEncoding("UTF-8");
        if (type == null) {
            PrintWriter writer = response.getWriter();
            response.setContentType("text/htm;charset=utf-8");
            writer.println("<h1>申请单类型为空，拒绝该申请</h1>");
            writer.println("<h1>3秒后回到首页</h1>");
            response.setHeader("refresh","3;url=/index");
            writer.close();
            return;
        }
        //通过资产ID查询资产
        Asset asset = assetRepository.findById(assetId).orElse(null);
        String status = asset.getStatus();
        switch (type){//申请单的类型就是申请单的结束状态，领用-->已使用
            case "空闲":
                if(status.equals("已使用") || status.equals("借用")){
                    //允许访问控制器，允许通过过滤链，转到下一个过滤链
                   filterChain.doFilter(servletRequest,servletResponse);
                   return;
                }
                break;
            case "领用":
            case "已使用":
                if(status.equals("空闲") || status.equals("维修")){
                    //允许访问控制器，允许通过过滤链，转到下一个过滤链
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
            case "借用":
                if(status.equals("空闲") || status.equals("维修")){
                    //允许访问控制器，允许通过过滤链，转到下一个过滤链
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
            case "维修":
                if(status.equals("已使用") || status.equals("借用")){
                    //允许访问控制器，允许通过过滤链，转到下一个过滤链
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
            case "报废":
                if(status.equals("维修")){
                    //允许访问控制器，允许通过过滤链，转到下一个过滤链
                    filterChain.doFilter(servletRequest,servletResponse);
                    return;
                }
                break;
        }

        PrintWriter writer = response.getWriter();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        writer.println("<html><head><beta charset=utf-8></head><body>");
        writer.println("<h1>你的申请单不符合规则！！</h1>");
        writer.println("<h1>3秒后回到首页</h1>");
        response.setHeader("refresh","3;url=/index");
        writer.close();
        return;

    }
}

