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
        Asset asset= assetRepository.findById(application.getAssetId()).orElse(null);
        if(asset == null){return null;}//如果没有找到资产就返回null
        //TODO 注意，我们没有判断该资产是否为空闲
        if(asset.getStatus()=="空闲" || asset.getStatus()=="预定"){
            asset.setStatus("预定");//设置申请单中的状态为 预定
            assetRepository.save(asset);//保存到数据库中
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
        return null; //否则就返回null
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
        List<Asset> freeAssets2 = assetRepository.findAllByRukudanIdAndStatus(rukudanId, "预定");
        freeAssets.addAll(freeAssets2);//将两个集合合并成1个
        return freeAssets;//返回给前端，让使用者查看并选择哪一个资产作为领用申请单上的资产ID
    }

    /**
     * //处理申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("application/agree")
    public Application applicationAgree(long applicationId,String result){//处理同意申请单的方法
        //从数据库中查询申请单对象
        Application application = applicationRepository.findById(applicationId).orElse(null);
        //TODO 这个申请是不是待处理状态？ 只能是待处理状态才可以同意。
        //处理资产状态
        //只有资产状态一定是预定的，才可以同意。
        long assetId = application.getAssetId();
        //从数据库中寻找资产
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if(asset == null){//没有找到资产
            return  null;
        }
        String status = asset.getStatus();//获得资产当前状态
        if(status != "预定"){//你没有预定就直接同意，不合适
            return null;//不作处理，直接返回空
        }
        asset.setStatus("已使用"); //设置为已使用
        //以下是同意的处理流程
        application.setStatus("同意");//设置申请单状态为 同意
        application.setManager("黄主任");//TODO 应该是当前登录的用户（admin）
        application.setResultContent(result);//TODO 如果没有写处理意见呢？
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }
    //TODO 管理员可以直接修改资产状态

    /**
     * //同意维修申请，面向使用者的，使用者提出申请，一定是资产是他自己的。
     * 同意维修申请
     * @return
     */
    @GetMapping("application/agreeRepair")
    public Application applicationAgreeRepair(long applicationId){//申请单号
        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application==null){return null;}//如果没有找到申请单号就返回null
        long assetId = application.getAssetId();//获得申请单中的资产ID
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null) {
            return null;//没有找到资产
        }
        String status = asset.getStatus();//获得资产状态
        String type = application.getType();//获得申请单的类型
        if(type != "维修"){
            System.out.println("申请单类型不是维修，无法同意维修");
            return null;
        }
        if (status != "已使用"){//TODO 判断资产的使用者是不是申请单的申请人。
            System.out.println("资产不是已使用状态，无法同意维修");
            return null;
        }
        asset.setStatus("维修"); //设置申请单中的资产状态为 维修
        assetRepository.save(asset);
        //以下处理申请单
        application.setStatus("同意");//设置申请单状态为 同意
        application.setManager("黄主任");//TODO 应该是当前登录的用户（admin）
        application.setResultContent("黄主任已同意维修");//TODO 如果没有写处理意见呢？
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }


    //refuse拒绝申请单
    /**
     * //拒绝申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("application/refuse")
    public Application applicationRefuse(long applicationId,String result){//处理同意申请单的方法
        //从数据库中查询申请单对象
        Application application = applicationRepository.findById(applicationId).orElse(null);
        //TODO 是否考虑资产的状态，空闲可以拒绝吗？。
        application.setStatus("拒绝");//设置申请单状态为 同意
        application.setManager("黄主任");//TODO 应该是当前登录的用户（admin）
        application.setResultContent(result);//TODO 如果没有写处理意见呢？
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }

}
