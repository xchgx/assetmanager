package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRukudan;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRukudanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController//Rest风格的控制器
public class IndexController {//首页控制器
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRukudanRepository assetRukudanRepository;
    @Autowired
    private AssetRepository assetRepository;//自动注入 资产持久化类
    @Autowired
    private ApplicationRepository applicationRepository;

    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
    @GetMapping({"/index","/"})//设置访问url网址
    public String index(){//定义首页方法
        return "大家好，欢迎来到Spring Boot项目现场，我是首页index"; //直接返回一句话（字符串）
    }

    @GetMapping("/add") //添加资产入库单，资产入库单
    public AssetRukudan add(@RequestParam String name, @RequestParam int amount){ //接收前端传送过来的参数
        AssetRukudan assetRukudan = new AssetRukudan(); //创建一个资产对象
        assetRukudan.setName(name); //设置资产名称-来自参数 name
        assetRukudan.setAmount(amount);  //设置资产数量-来自参数 number
        AssetRukudan resultAssetRukudan = assetRukudanRepository.save(assetRukudan);//保存资产对象到数据库中，然后返回存储后的资产对象
        return resultAssetRukudan; //返回存储后的资产对象
    }

    @GetMapping("/list") //访问网址是 http://localhost:8080/list
    public List<AssetRukudan> list(){//列出所有资产
        return assetRukudanRepository.findAll();//查询所有资产
    }


    /**
     * //    资产出库，你要什么资产？电脑、投影仪、笔记本、手机、桌子
     * @param rukudan 入库单主键ID
     * @param username 使用者用户名，唯一特性
     * @return 保存后的资产对象
     */

    @GetMapping("/chuku") //接口地址，访问的网址
    public Asset chuku(long rukudan,String username){ //出库的方法
        //出库的内容来自于入库单
        //通过入库单ID查询入库单记录，如果找不到就返回null
        AssetRukudan assetRukudan=assetRukudanRepository.findById(rukudan).orElse(null);

        Asset asset = new Asset();//创建资产对象
        asset.setName(assetRukudan.getName());//设置资产名称
        asset.setPrice(assetRukudan.getPrice());//设置单价
        asset.setRukudanId(assetRukudan.getId());//设置我们的资产来自于哪一个入库单ID
        asset.setUsername(username);//设置使用者
        Asset save = assetRepository.save(asset);//把资产保存到数据库中
        return save;//返回保存后的资产
    }

    @PostMapping("/application/add")
    public Application applicationAdd(Application application){
//        Application application = new Application();
        application.setBeginDate(new Date());
//        application.setContent("我要一台电脑");
        application.setStatus("待处理");
//        application.setType("领用");
//        application.setUsername("张三");
        return applicationRepository.save(application);
    }

    /**
     * 同意申请
     * @param applicationId 申请类主键ID
     * @return 同意后的申请
     */
    @GetMapping("/application/agree") //同意申请的访问接口
    public Application applicationAgree(long applicationId){ //同意申请的功能方法
        Application application = applicationRepository.findById(applicationId).orElse(null);//查找这个申请ID
        application.setOpinion("同意"); //设置该申请的处理意见为： 同意。
        return applicationRepository.save(application);//重新将该申请保存到数据库中（更新数据），并返回到前端
    }
    @GetMapping("/application/list")
    public List<Application> applicationList(){
        return applicationRepository.findAll();
    }

}
