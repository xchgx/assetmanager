package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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


    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
     @GetMapping({"/index","/"})//设置访问url网址
    public String index(Model model){//定义首页方法
        //返回视图名称
         List<Asset> assets = assetRepository.findAll();
         model.addAttribute("assets",assets);//在模型中添加数据
         return "index";//这了返回的视图名称为index
    }

    /**
     * 通过资产ID查询资产视图
     * "http://xchgx.vicp.net/asset/byId?assetId="
     * @param assetId 资产ID
     * @return
     */
    @GetMapping("/asset/byId")
    public String assetById(long assetId,Model model){
        Asset asset = assetRepository.findById(assetId).orElse(null);
        model.addAttribute("asset",asset);//准备数据
        return "asset";//返回资产asset视图,数据自动进入到视图
    }


}
