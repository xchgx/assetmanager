package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller //Web风格
public class IndexController {//首页控制器
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRepository assetRepository;
    @Autowired //入库单持久化对象
    private AssetRuKuDanRepository assetRuKuDanRepository;
    @Autowired //自动注入 申请单持久化对象
    private ApplicationRepository applicationRepository;


    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
    @GetMapping({"/index","/"})//设置访问url网址
    public String index(Model model){//定义首页方法
        List<Asset> assets = assetRepository.findAll();
        model.addAttribute("assets", assets);
        return "index";
    }

    /**
     * 通过资产ID查询资产
     * @param assetId 资产ID
     * @param model 模式数据传输中介
     * @return
     */
    @GetMapping("/asset/byId/{assetId}")
    public String  assetById(@PathVariable long assetId, Model model) {
        Asset asset = assetRepository.findById(assetId).orElse(null); //查询资产
        model.addAttribute("asset", asset);//准备数据
        return "asset";//用asset视图呈现数据
    }


}
