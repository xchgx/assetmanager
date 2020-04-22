package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import com.xchgx.cloud.sso8.assetmanager.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
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
     * 管理员者后台
     * 通过登录成功后，跳转（重定向）到 redirect:/admin
     * 实际上，访问的就是 /admin
     * @param model
     * @return
     */
    @GetMapping("/admin") //IndexController控制器没有前缀网址
    public String admin(Model model){

        List<Asset> assets = assetRepository.findAll();//查询了所有的资产
        model.addAttribute("assets",assets);//在模型中添加数据

        //查询 了所有的入库单
        List<AssetRuKuDan> assetRuKuDans = assetRuKuDanRepository.findAll();
        model.addAttribute("rukudans", assetRuKuDans);

        //版本15.0 更新内容 begin
        //查询了所有的申请单
        List<Application> applications = applicationService.allApplication();
        //版本15.0 更新内容 end

        model.addAttribute("applications", applications);

        //版本17.0 begin
        List<Application> repairApplications = applicationService.repairApplications();
        model.addAttribute("repairApplications", repairApplications);
        //版本17.0 end
        return "admin/admin";//影射到 /admin/admin.html 视图文件
    }


    /**
     * 管理员者后台
     * 通过登录成功后，跳转（重定向）到 redirect:/admin
     * 实际上，访问的就是 /admin
     * @param model
     * @return
     */
    @GetMapping("/assets") //IndexController控制器没有前缀网址
    public String assets(Model model){

        List<Asset> assets = assetRepository.findAll();//查询了所有的资产
        model.addAttribute("assets",assets);//在模型中添加数据

        return "admin/assets";//影射到 /admin/admin.html 视图文件
    }


    /**
     * 管理员者后台
     * 通过登录成功后，跳转（重定向）到 redirect:/admin
     * 实际上，访问的就是 /admin
     * @param model
     * @return
     */
    @GetMapping("/applications")
    public String applications(Model model){
        //版本15.0 更新内容 begin
        //查询了所有的申请单
        List<Application> applications = applicationService.allApplication();
        //版本15.0 更新内容 end

        model.addAttribute("applications", applications);

        //版本17.0 begin
        List<Application> repairApplications = applicationService.repairApplications();
        model.addAttribute("repairApplications", repairApplications);
        //版本17.0 end
        List<Application> todoApplications = applicationService.allTodoApplications();
        model.addAttribute("todoApplications", todoApplications);
        List<Application> agreeApplications = applicationService.allAgreeApplications();
        model.addAttribute("agreeApplications", agreeApplications);
        List<Application> refuseApplications = applicationService.allRefuseApplications();
        model.addAttribute("refuseApplications", refuseApplications);
        return "admin/applications";
    }

    /**
     * 管理员者后台-出库
     * @param model
     * @return
     */
    @GetMapping("/chuku")
    public String chuku(Model model){
        List<AssetRuKuDan> assetRuKuDans = assetRuKuDanRepository.findAll();
        model.addAttribute("rukudans", assetRuKuDans);
        return "admin/chuku";
    }
}
