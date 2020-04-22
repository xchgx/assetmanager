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
        List<Integer> countList = new ArrayList<>();
        for (int i = 0; i < assets.size(); i++) {
            Asset asset = assets.get(i);
            long assetId = asset.getId();
            //查询该ID的申请数量--
            int countPeoples = applicationController.countPeoples(assetId, "待处理", "已使用");
            countList.add(countPeoples);
        }
        model.addAttribute("assets",assets);//在模型中添加数据
        model.addAttribute("countList",countList);
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

}
