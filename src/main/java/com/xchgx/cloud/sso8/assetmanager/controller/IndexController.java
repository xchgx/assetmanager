package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
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
    private AssetRepository assetRepository;
    @Autowired //入库单持久化对象
    private AssetRuKuDanRepository assetRuKuDanRepository;
    @Autowired //自动注入 申请单持久化对象
    private ApplicationRepository applicationRepository;


    //相当于我们之前分析资产管理系统时候，出现的 网页界面类
    @GetMapping({"/index","/"})//设置访问url网址
    public String index(){//定义首页方法
        return "大家好，欢迎来到Spring Boot项目现场，我是首页index"; //直接返回一句话（字符串）
    }

    @GetMapping("/add") //添加资产，资产入库
    public AssetRuKuDan add(@RequestParam String name,@RequestParam int amount){ //接收前端传送过来的参数
        AssetRuKuDan assetRuKuDan = new AssetRuKuDan(); //创建一个资产对象
        assetRuKuDan.setName(name); //设置资产名称-来自参数 name
        assetRuKuDan.setAmount(amount);  //设置资产数量-来自参数 number
        assetRuKuDan.setRemained(amount);//设置剩余数量为入库数量
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
        asset.setUsername(null);//设置资产使用者
        asset.setStatus("空闲");//或者未使用

        //以下为新增部分v2.0 2020年3月31日10:43:03
        asset.setName(ruKuDan.getName());//设置资产的名称为入库单中的资产名称
        int remainded = ruKuDan.getRemained();//获得入库单中的剩余数量
        remainded--;//减少一台
        ruKuDan.setRemained(remainded);//重新赋值剩余数量
        assetRuKuDanRepository.save(ruKuDan);//重新保存到数据库中
        //以上为新增部分v2.0 2020年3月31日10:43:12

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
    //申请单的操作
    @PostMapping("/application/add") //提交申请的接口
    public Application applicationAdd(Application application){
//        //持久化保存到申请单数据表中。
//        //查询入库单，通过前台传递过来的参数中获得入库单号，再通过入库单持久化对象去查询
//        AssetRuKuDan assetRuKuDan = assetRuKuDanRepository.findById(application.getRukudanId()).orElse(null);
//        if (assetRuKuDan == null){
//            System.out.println("没有找到入库单:"+application.getRukudanId());
//            return null;
//        } //如果入库单填写错误，没有找到，则直接返回null，不做保存处理

        Asset asset= assetRepository.findById(application.getAssetId()).orElse(null);
        if(asset == null){return null;}//如果没有找到资产就返回null

        application.setBeginDate(new Date()); //设置申请时间为当前时间
        application.setUsername("张三");//TODO 应该存放当前登录的用户
        application.setStatus("待处理");//任何人提交申请都应该是 待处理状态
        application.setResultContent("");//待处理状态下，是没有处理结果的
        application.setManager("");//提交申请，默认进入无管理员处理
        application.setAssetId(asset.getId());//记录下申请单上指定的资产ID
        application.setAssetName(asset.getName());//申请单上的资产名称应该和入库单资产名称一致
        application.setResultDate(null);//提交申请后，没有处理时间
        return applicationRepository.save(application);
    }

    /**
     * 查询所有申请单
     * @return
     */
    @GetMapping("application/list")//查询所有申请的接口
    public List<Application> applicationList(){
        return applicationRepository.findAll();
    }

//    /**
//     * 同意申请
//     * @param applicationId 申请单ID
//     * @return
//     */
//    @GetMapping("application/agree") //访问接口
//    public Application applicationAgree(long applicationId){
//        Application application = applicationRepository.findById(applicationId).orElse(null);//查找申请单
//        Asset chuku = chuku(application.getRukudanId(),"admin"); //领用申请可以直接出库
//        application.setResultDate(new Date());
//        application.setManager("admin");
//        application.setResultContent("同意领用资产");
//        application.setStatus("已使用");
//        return application;
//    }

    //查看入库单的库存量（剩余数量）
    @GetMapping("rukudan/remained")
    public int rukudanRemained(long rukudanId){
        //查询入库单对象
        AssetRuKuDan assetRuKuDan = assetRuKuDanRepository.findById(rukudanId).orElse(null);
        return assetRuKuDan.getRemained();//获得该入库单的剩余数量
    }

    //查看当前空闲的电脑
    @GetMapping("asset/free") //查询资产来自于空闲状态
    public List<Asset> assetFree(long rukudanId){//是不是把所有的空闲资产都查出来？
        //assetRepository; //TODO 先去解决查询数据库的代码然后过来
        List<Asset> freeAssets = assetRepository.findAllByRukudanIdAndStatus(rukudanId, "空闲");
        return freeAssets;//返回给前端，让使用者查看并选择哪一个资产作为领用申请单上的资产ID
    }
}
