package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.*;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import com.xchgx.cloud.sso8.assetmanager.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

//@RestController//Rest风格的控制器
@Controller //Web风格控制器
public class IndexController {//首页控制器
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

    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
    @GetMapping({"/index","/"})//设置访问url网址
    public String index(Model model){//定义首页方法
        //返回视图名称
        List<Asset> assets = assetRepository.findAll();
//        List<Integer> count = applicationController.countPeoples()
        List<Integer> countList = new ArrayList<>();
        for (int i = 0; i < assets.size(); i++) {
            Asset asset = assets.get(i);
            long id = asset.getId();
            int countPeoples = applicationController.countPeoples(id, "待处理", "已使用");
            countList.add(countPeoples);
        }
        model.addAttribute("assets",assets);//在模型中添加数据
        model.addAttribute("countList",countList);//在模型中添加数据
        return "index";//这了返回的视图名称为index
    }

    /**
     * 通过资产ID查询资产视图
     * "http://xchgx.vicp.net/asset/byId?assetId="
     * @// TODO: 2020/4/7   变更管理第9条，执行变更。
     * @param assetId 资产ID
     * @return
     */
    @GetMapping("/assetById") //
    public String assetById(long assetId, Model model, HttpServletRequest request){
        Asset asset = assetRepository.findById(assetId).orElse(null);
        model.addAttribute("asset",asset);//准备数据

        //获得当前登录用户
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {//没有任何用户登录，在session中找不到user
            return "asset";//直接显示视图，此时返回时模型中只有资产对象
        }

        int countPeoples = applicationController.countPeoples(assetId, "待处理", "已使用");
        model.addAttribute("countPeoples",countPeoples);
        //所有关于该资产的申请单要放入到视图模型中
        //通过资产ID查询申请单
        List<Application> applications = applicationService.assetApplication(assetId);
        model.addAttribute("applications", applications);//动态显示操作项,operation=op

        List<Application> userChangeApplications= applicationService.userChangeApplications(assetId);
        model.addAttribute("userChangeApplications", userChangeApplications);//动态显示操作项,operation=op
        return "asset";//返回资产asset视图,数据自动进入到视图
    }

    /**
     * 使用者后台
     * 通过登录成功后，跳转重定向到 redirect:/user 使用GET的方式访问
     * @param model
     * @return
     */
    @GetMapping("/user/user")
//    @GetMapping("/user/assets")
    public String user(Model model){

        List<Asset> assets = assetRepository.findAll();//查询所有的资产
        model.addAttribute("assets",assets);//在模型中添加数据

        return "user/user";//在user文件夹中的 user.html 视图文件
//        return "user/assets";
//        return "user/applications";
    }

    /**
     * 使用者后台
     * 显示使用者的申请单
     * @param model
     * @return
     */
    @GetMapping("/user/applications")
//    @GetMapping("/user/assets")
    public String userApplications(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");

        List<Application> list = applicationRepository.findAllByUsername(user.getUsername());
        model.addAttribute("applications", list);

        return "user/applications";//在user文件夹中的 user.html 视图文件
//        return "user/assets";
//        return "user/applications";
    }

    /**
     * 使用者后台
     * 显示使用者的资产
     * @param model
     * @return
     */
    @GetMapping("/user/assets")
    public String userAssets(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");

        List<Asset> list = assetRepository.findAllByUsername(user.getUsername());
        model.addAttribute("assets", list);

        return "user/assets";
    }

    /**
     * 管理员者后台
     * 通过登录成功后，跳转（重定向）到 redirect:/admin
     * 实际上，访问的就是 /admin
     * @param model
     * @return
     */
    @GetMapping("/admin/admin") //IndexController控制器没有前缀网址
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
}
