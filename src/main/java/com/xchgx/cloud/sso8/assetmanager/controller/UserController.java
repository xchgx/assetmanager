package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import com.xchgx.cloud.sso8.assetmanager.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRepository assetRepository;
    @Autowired //入库单持久化对象
    private AssetRuKuDanRepository assetRuKuDanRepository;
    @Autowired //自动注入 申请单持久化对象
    private ApplicationRepository applicationRepository;
    @Autowired
    private RukudanController rukudanController;
    //版本15.0 新增内容 begin
    @Autowired
    private ApplicationService applicationService;
    //版本15.0 新增内容 end
    @Autowired
    private ApplicationController applicationController;

    /**
     * 使用者后台
     * 通过登录成功后，跳转重定向到 redirect:/user 使用GET的方式访问
     * @param model
     * @return
     */
    @GetMapping("/user")
    public String user(Model model){
        List<Asset> assets = assetRepository.findAllByStatus("空闲");//查询所有的资产
        model.addAttribute("assets",assets);//在模型中添加数据
        return "user/user";//在user文件夹中的 user.html 视图文件
    }

    /**
     * 进入到使用者的申请单页
     * @return
     */
    @GetMapping("/applications")
    public String userApplications(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        List<Application> applications = applicationRepository.findAllByUsername(user.getUsername());
        model.addAttribute("applications", applications);
        return "user/applications";//返回user目录下的application.html视图文件
    }

    /**
     * 进入到使用者的资产页
     * @return
     */
    @GetMapping("/assets")
    public String userAssets(HttpServletRequest request,Model model){
        User user = (User) request.getSession().getAttribute("user");
        List<Asset> assets = assetRepository.findAllByUsername(user.getUsername());
        model.addAttribute("assets", assets);
        return "user/assets";//返回user目录下的application.html视图文件
    }
}
