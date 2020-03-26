package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
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
    @Autowired
    private AssetRuKuDanRepository assetRuKuDanRepository;

    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
    @GetMapping({"/index","/"})//设置访问url网址
    public String index(){//定义首页方法
        return "大家好，欢迎来到Spring Boot项目现场，我是首页index"; //直接返回一句话（字符串）
    }

    @GetMapping("/add") //添加资产，资产入库
    public AssetRuKuDan add(@RequestParam String name,@RequestParam int number){ //接收前端传送过来的参数
        AssetRuKuDan assetRuKuDan = new AssetRuKuDan(); //创建一个资产对象
        assetRuKuDan.setName(name); //设置资产名称-来自参数 name
        assetRuKuDan.setNumber(number);  //设置资产数量-来自参数 number
        AssetRuKuDan resultAsset = assetRuKuDanRepository.save(assetRuKuDan);//保存资产对象到数据库中，然后返回存储后的资产对象
        return resultAsset; //返回存储后的资产对象
    }
    @GetMapping("/chuku") //添加资产，资产出库
    public Asset chuku(@RequestParam long rukudanId){ //从哪一个批次（入库单）的资产中出库1台
        AssetRuKuDan ruKuDan = assetRuKuDanRepository.findById(rukudanId).orElse(null);//通过ID查询入库单对象
        if(ruKuDan == null){//如果没有找到对应的入库单就返回null
            System.out.println("无法出库");
            return null;
        }
        System.out.println(ruKuDan.getId());

        Asset asset = new Asset();//创建资产对象
        asset.setRukudanId(ruKuDan.getId());//设置资产的入库单ID
        asset.setUsername("张三");//设置资产使用者
        return assetRepository.save(asset);//保存并返回资产对象
    }
    @GetMapping("/listAsset") //访问网址是 http://localhost:8080/list
    public List<Asset> list1(){//列出所有资产
        return assetRepository.findAll();//查询所有资产
    }
    @GetMapping("/listRukudan") //访问网址是 http://localhost:8080/list
    public List<AssetRuKuDan> list2(){//列出所有资产
        return assetRuKuDanRepository.findAll();//查询所有资产
    }
}
