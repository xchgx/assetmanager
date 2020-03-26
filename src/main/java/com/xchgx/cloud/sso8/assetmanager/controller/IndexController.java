package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController//Rest风格的控制器
public class IndexController {//首页控制器
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRepository assetRepository;

    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
    @GetMapping({"/index","/"})//设置访问url网址
    public String index(){//定义首页方法
        return "大家好，欢迎来到Spring Boot项目现场，我是首页index"; //直接返回一句话（字符串）
    }

    @GetMapping("/add") //添加资产，资产入库
    public Asset add(@RequestParam String name,@RequestParam int number){ //接收前端传送过来的参数
        Asset asset = new Asset(); //创建一个资产对象
        asset.setName(name); //设置资产名称-来自参数 name
        asset.setNumber(number);  //设置资产数量-来自参数 number
        Asset resultAsset = assetRepository.save(asset);//保存资产对象到数据库中，然后返回存储后的资产对象
        return resultAsset; //返回存储后的资产对象
    }

    @GetMapping("/list") //访问网址是 http://localhost:8080/list
    public List<Asset> list(){//列出所有资产
        return assetRepository.findAll();//查询所有资产
    }
}
