package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 申请单控制器
 */
@RestController //REST风格 通过JSON格式传递数据
@RequestMapping("/application") //统一网址前缀
public class ApplicationController {
    @Autowired
    private AssetRepository assetRepository;//资产持久化对象
    @Autowired
    private ApplicationRepository applicationRepository;//申请单持久化对象

    /**
     * //提交申请单的操作
     * @param application 申请单对象
     * @return
     */
    @PostMapping("/add") //提交申请的接口
    public Application applicationAdd(Application application,HttpServletRequest request){
        Asset asset= assetRepository.findById(application.getAssetId()).orElse(null);
        if(asset == null){return null;}//如果没有找到资产就返回null
        //版本14.0 新增内容 begin
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;//未登录，则不作处理
        }
        //版本14.0 新增内容 end

//        //作废
//        if(asset.getStatus()=="空闲" || asset.getStatus()=="预定"){
//            asset.setStatus("预定");//设置申请单中的状态为 预定
//            assetRepository.save(asset);//保存到数据库中
//            application.setBeginDate(new Date()); //设置申请时间为当前时间
//            application.setUsername("张三");// 应该存放当前登录的用户
//            application.setStatus("待处理");//任何人提交申请都应该是 待处理状态
//            application.setResultContent("");//待处理状态下，是没有处理结果的
//            application.setManager("");//提交申请，默认进入无管理员处理
//            application.setAssetId(asset.getId());//记录下申请单上指定的资产ID
//            application.setAssetName(asset.getName());//申请单上的资产名称应该和入库单资产名称一致
//            application.setResultDate(null);//提交申请后，没有处理时间
//            return applicationRepository.save(application);
//        }
//        return null; //否则就返回null


        //版本14.0 新增内容 begin
        application.setStart(asset.getStatus());//资产的状态不能让参数决定，应该是由系统内的读取数据为准。
        application.setStop(application.getType().equals("领用")?"已使用":application.getType()); //TODO 建议申请单的类型和资产保持同步
        application.setBeginDate(new Date());
        application.setUsername(user.getUsername()); //当前登录用户名
        application.setAssetName(asset.getName());
        application.setAssetId(asset.getId());
        application.setStatus("待处理"); //刚开始应该是待处理

        application.setManager(null); //管理员，处理该申请单的人
        application.setResultDate(null); //处理时间
        application.setResultContent(null); //处理意见
        return applicationRepository.save(application);
        //版本14.0 新增内容 end

    }


    /**
     * 查询所有申请单
     * @return
     */
    @GetMapping("/list")//查询所有申请的接口
    public List<Application> applicationList(){
        //判断用户是否登录
        //从session里面获得当前登录的用户对象
        return applicationRepository.findAll();
    }



    /**
     * //处理同意申请单
     * @param applicationId 申请单ID
     */
    @GetMapping("/agree")
    public Application applicationAgree(long applicationId,String result,HttpServletRequest request){//处理同意申请单的方法
        //从数据库中查询申请单对象
        Application application = applicationRepository.findById(applicationId).orElse(null);
        //版本14.0 新增内容 begin
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;//用户未登录
        }
        //版本14.0 新增内容 end

        // 处理资产状态
        //只有资产状态一定是预定的，才可以同意。
        long assetId = application.getAssetId();
        //从数据库中寻找资产
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if(asset == null){//没有找到资产
            return  null;
        }

        //版本14.0 新增内容 begin
        asset.setStatus(application.getStop()); //设置为申请单的停止状态
        //版本14.0 新增内容 end
        asset.setUsername(application.getUsername());//设置使用者
        assetRepository.save(asset);//BUG 修改资产状态要持久化到数据库
//过期

//        String status = asset.getStatus();//获得资产当前状态
//        if(status != "预定"){//你没有预定就直接同意，不合适
//            return null;//不作处理，直接返回空
//        }


        //以下是同意的处理流程
        application.setStatus("同意");//设置申请单状态为 同意
        //版本14.0 新增内容 begin
        application.setManager(user.getUsername());//当前登录的用户（admin）
        application.setResultContent(result==null?"管理员很懒，没有填写处理意见。":result);//如果没有写处理意见呢？
        //版本14.0 新增内容 end
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
    @GetMapping("/agreeRepair")
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
    @GetMapping("/refuse")
    public Application applicationRefuse(long applicationId,String result,HttpServletRequest request){//处理同意申请单的方法
        //版本14.0 新增内容 begin
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;//用户未登录
        }
        //版本14.0 新增内容 end
        //从数据库中查询申请单对象
        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application == null) { //查无此申请单
            return null;
        }
        //新增代码 2020-4-2 09:24:21 begin
        long assetId = application.getAssetId();//获得申请单中的资产ID
        Asset asset = assetRepository.findById(assetId).orElse(null);//通过资产ID获得资产实体
        if (asset == null) {//没有找到该资产
            return null;
        }
//        String status = asset.getStatus();//获得资产的状态
//        if (asset.getStatus() == "预定") { //是不是预定状态 ,如果是，就回到空闲
//            asset.setStatus("空闲"); //设置资产的状态为 空闲
//        }
//新增代码 2020-4-2 09:24:21 end

        //版本14.0 新增内容 begin
        asset.setStatus(application.getStart());
        assetRepository.save(asset);
        application.setStatus("拒绝");//设置申请单状态为 拒绝
        application.setManager(user.getUsername());//当前登录的用户（admin）
        application.setResultContent(result==null?"管理员很懒，没有填写处理意见。":result);//如果没有写处理意见呢？
        //版本14.0 新增内容 end
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }

    /**
     * 拒绝维修申请单
     * @param applicationId 维修申请单号
     * @param result 拒绝申请的处理意见
     * @return
     */
    @GetMapping("/refuseRepair")//拒绝维修申请单的URL网址接口
    public Application applicationRefuseRepair(long applicationId,String result){//参数是维修单号
        //通过申请单号查找申请单
        Application application = applicationRepository.findById(applicationId).orElse(null);
        //假设 资产是已使用状态、借用
        //拒绝 不修改资产的状态
        //不对资产做处理，也不查询资产状态,直接处理申请单
        application.setStatus("拒绝");//设置申请单状态为 同意
        application.setManager("黄主任");//TODO 应该是当前登录的用户（admin）
        application.setResultContent(result);//TODO 如果没有写处理意见呢？
        application.setResultDate(new Date());//处理时间为当前时间
        //把已经处理的申请单保存到数据库中并返回
        return applicationRepository.save(application);
    }

    /**
     * 同意报废申请
     * @param applicationId 申请单号
     * @param result 处理意见
     * @return
     */
    @GetMapping("/agreeScrap")
    public Application applicationAgreeScrap(long applicationId, String result){
        Application application = applicationRepository.findById(applicationId).orElse(null);
        long assetId = application.getAssetId();
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset.getStatus() == "已使用") {
            asset.setStatus("报废");
            assetRepository.save(asset);
        }
        application.setStatus("同意");
        application.setManager("黄主任");
        application.setResultDate(new Date());
        application.setResultContent(result);
        return applicationRepository.save(application);
    }

    /**
     * 通过资产查询申请单
     * @param assetId
     * @return
     */
    @GetMapping("/findByAsset") //默认访问是 /application
    public List<Application> findByAsset(long assetId){
        return applicationRepository.findAllByAssetId(assetId);
    }

    /**
     * 通过申请单类型查询申请单
     * @param type 申请单类型
     * @return
     */
    @GetMapping("/findByType")
    public List<Application> findByType(String  type){
        return applicationRepository.findAllByType(type);
    }

    /**
     * 通过状态和类型查询申请单
     * //http://localhost:8080/application/findByTypeAndStatus?type=领用&status=待处理
     * @param type 类型
     * @param status 状态
     * @return
     */
    @GetMapping("/findByTypeAndStatus")
    public List<Application> findByTypeAndStatus(String type, String status) {
        return applicationRepository.findAllByTypeAndStatus(type,status);
    }

    /**
     * 这是快速提交申请，所以该申请是新增的，不是修改申请，这里需要创建申请单对象。
     * @param type 申请单类型：维修、报废、领用、借用等
     * @param assetId 资产ID,申请单上填写的资产
     * @return 申请单
     */
    @GetMapping("/addQuick")
    public Application addQuick(String type, long assetId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user"); //获得当前登录用户
        Asset asset = assetRepository.findById(assetId).orElse(null); //通过资产ID查询资产对象
        if (asset == null) {//如果资产ID不存在，则直接返回，不处理。
            return null;
        }

        Application application = new Application(); //创建新的申请单对象
        application.setAmount(1);//默认为1个资产
        application.setContent("该申请为快速申请，由扫码提交。"); //设置申请内容-申请理由。
        application.setType(type);//设置申请单类型为参数type的值
        application.setBeginDate(new Date());//设置当前时间为申请单的创建时间
        application.setUsername(user.getUsername());//设置申请单的发起人=申请人
        application.setAssetName(asset.getName());//申请单的资产名称来自资产对象的名称
        application.setAssetId(assetId); // 申请单的资产ID既可以是形式参数assetId，也可以是资产对象的id属性=asset.getId();
        application.setStatus("待处理");//申请单的状态是“同意”、“拒绝”、“待处理”三种，并不是资产的状态，要区分。
        //版本14.0 新增内容 begin
        application.setStart(asset.getStatus());
        application.setStop(type.equals("领用")?"已使用":type);
        //版本14.0 新增内容 end

        application.setManager(null);//这是新提交的申请，肯定是没有处理人的，所以这里要确保处理人为空
        application.setResultDate(null);//同上
        application.setResultContent(null);//同上

        return applicationRepository.save(application); //保存申请单对象并返回申请单
    }

}
