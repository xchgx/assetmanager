package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ApplicationController {
@Autowired
private ApplicationRepository applicationRepository;
@Autowired
private AssetRepository assetRepository;
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



    /**
     * //同意领用申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("application/agree")
    public Application applicationAgree(long applicationId, String result){//处理同意申请单的方法
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
     * //拒绝领用申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("application/refuse")
    public Application applicationRefuse(long applicationId,String result){//处理同意申请单的方法
        //从数据库中查询申请单对象
        Application application = applicationRepository.findById(applicationId).orElse(null);

        //新增内容 2020-4-1 14:39:21 begin
        long assetId = application.getAssetId();//获得资产ID
        Asset asset = assetRepository.findById(assetId).orElse(null); //查询资产对象
        if (asset == null) { //如果没找到
            return null;//就直接返回
        }
        String status = asset.getStatus();//获得资产当前状态
        if (status == "预定") {//必须是“预定”状态下才可以回到空闲。
            asset.setStatus("空闲");
            assetRepository.save(asset);
        }//否则不对资产对象做任何处理，原来是什么就是什么。
        //新增内容 2020-4-1 14:39:21 end

        //TODO 是否考虑资产的状态，空闲可以拒绝吗？。
        application.setStatus("拒绝");//设置申请单状态为 同意
        application.setManager("黄主任");//TODO 应该是当前登录的用户（admin）
        application.setResultContent(result);//TODO 如果没有写处理意见呢？
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }

    /**
     * //拒绝维修申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("application/refuseRepair")
    public Application applicationRefuseRepair(long applicationId,String result){//处理同意申请单的方法
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

    /**
     * //同意维修申请，面向使用者的，使用者提出申请，一定是资产是他自己的。
     * 同意维修申请
     * @return
     */
    @GetMapping("application/agreeScrap")
    public Application applicationAgreeScrap(long applicationId){//申请单号
        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application==null){return null;}//如果没有找到申请单号就返回null
        long assetId = application.getAssetId();//获得申请单中的资产ID
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null) {
            return null;//没有找到资产
        }
        String status = asset.getStatus();//获得资产状态
        String type = application.getType();//获得申请单的类型
        if(type != "报废"){
            System.out.println("申请单类型不是维修，无法同意维修");
            return null;
        }
        if (status != "已使用"){//TODO 判断资产的使用者是不是申请单的申请人。
            System.out.println("资产不是已使用状态，无法同意维修");
            return null;
        }
        asset.setStatus("报废"); //设置申请单中的资产状态为 维修
        assetRepository.save(asset);
        //以下处理申请单
        application.setStatus("同意");//设置申请单状态为 同意
        application.setManager("黄主任");//TODO 应该是当前登录的用户（admin）
        application.setResultContent("黄主任已同意报废");//TODO 如果没有写处理意见呢？
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }

    /**
     * //拒绝维修申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("application/refuseScrap")
    public Application applicationRefuseScrap(long applicationId,String result){//处理拒绝报废申请单的方法
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
