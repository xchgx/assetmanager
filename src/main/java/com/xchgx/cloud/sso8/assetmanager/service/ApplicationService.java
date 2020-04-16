package com.xchgx.cloud.sso8.assetmanager.service;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.User;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 版本15.0 新增类
 *
 */
@Service
@Slf4j
public class ApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private ToolService toolService;

    /**
     * 查询资产ID的使用者变更申请单
     * @param assetId 资产ID
     * @return 申请单集合
     */
    public  List<Application> userChangeApplications(long assetId){
        //开始状态=空闲  and  结束状态=已使用   and  申请单状态=同意
        return applicationRepository.findAllByStartAndStopAndStatus("空闲", "已使用", "同意");
    }
    /**
     * //通过资产ID查询申请单
     * @param assetId 资产ID
     * @return
     */
    public List<Application> assetApplication(long assetId){
        return applicationRepository.findAllByAssetId(assetId);
    }
    /**
     * //通过资产ID查询最后最新的申请单
     * @param assetId 资产ID
     * @return
     */
    public Application assetLastApplication(long assetId){
        return applicationRepository.findByAssetIdAndLastTrue(assetId);
    }
    /**
     * 获得所有的维修申请单
     * @return 维修申请单集合
     */
    public List<Application> repairApplications(){
        List<Application> applications = applicationRepository.findAllByTypeAndStatusNotAndStatusNot("维修","待处理","拒绝");
        return addOperation(applications);
    }
    /**
     * 版本15.0 新增方法
     * 获得所有带管理员操作项的申请单
     * @return 申请单集合
     */
    public List<Application> allApplication(){
        //从数据库中获得所有的申请单
        List<Application> applications = applicationRepository.findAll();
        //申请单的操作选项， begin
        return addOperation(applications);
    }

    /**
     * 添加申请单后面的操作项
     * @param applications 原始的申请单集合
     * @return 添加完操作项之后的集合
     */
    private List<Application> addOperation(List<Application> applications){

        String operationAgree = "<a href=\"/application/agree?applicationId={applicationId}\" class=\"btn btn-success btn-xs\">同意</a>";//同意按钮
        String operationRefuse = "<a href=\"/application/refuse?applicationId={applicationId}\" class=\"btn btn-danger btn-xs\">拒绝</a>";//拒绝按钮
        String operationRepairOk = "<a href=\"/application/repairOk?applicationId={applicationId}\" class=\"btn btn-success btn-xs\">维修成功</a>";//维修成功
        String operationRepairFail = "<a href=\"/application/repairFail?applicationId={applicationId}\" class=\"btn btn-warning btn-xs\">维修失败</a>";//维修失败
        String operationOver = "处理结束";
        String operationRepairOver = "维修结束";
        //申请单的操作选项， end
        for(Application application : applications){
            Asset asset = assetRepository.findById(application.getAssetId()).orElse(null);
            if (application.getStatus().equals("待处理")) {
                String op1 = operationAgree.replace("{applicationId}",""+application.getId());
                String op2 = operationRefuse.replace("{applicationId}",""+application.getId());
                application.setOperation(op1+op2);
            }else if(application.getStop().equals("维修")
                    &&application.getStatus().equals("同意")) {//不是待处理状态
                String op1 = operationRepairOk.replace("{applicationId}", "" + application.getId());
                String op2 = operationRepairFail.replace("{applicationId}", "" + application.getId());
                application.setOperation(op1 + op2);//设置申请单的操作选项第2种
            }else if(application.getStart().equals("维修")
                    &&application.getStatus().equals("拒绝")){
                String op1 = operationRepairOk.replace("{applicationId}", "" + application.getId());
                String op2 = operationRepairFail.replace("{applicationId}", "" + application.getId());
                application.setOperation(op1 + op2);//设置申请单的操作选项第2种
            }else{
                application.setOperation(operationOver);//处理结束
            }
        }
        return applications;
    }

    /**
     *      * 版本15.0 新增方法
     * 扫码快速添加申请单
     * @param type     申请单类型：使用、维修、借用、报废
     * @param assetId 资产ID
     * @param username 当前登录用户名
     * @return 返回保存好的申请单
     */
//    public Application addQuick(String type, long assetId,long applicationId, String username) {
//        Asset asset = assetRepository.findById(assetId).orElse(null); //通过资产ID查询资产对象
//        if (asset == null) {//如果资产ID不存在，则直接返回，不处理。
//            return null;
//        }
//        Application srcApplication = applicationRepository.findById(applicationId).orElse(null);
//        Application nextApplication = toolService.createNextApplication(srcApplication,type);
//        applicationRepository.save(srcApplication);
//        return applicationRepository.save(nextApplication); //保存申请单对象并返回申请单
//    }

    /**
     * 快速提交领用申请单
     * @param assetId 资产ID
     * @param applicationId 申请单ID
     * @param username 当前登录用户
     * @return
     */

    /**
     * 创建下一张申请单，根据上一张申请单
     * 快速提交领用申请单
     * @return
     */
    public Application createUsedApplication(long applicationId, User user){
        Application parent = applicationRepository.findById(applicationId).orElse(null);
        //创建第一张申请单   版本4.0 2020年4月15日23:35:46 begin
        parent.setLast(false);//不再是最后一张

        Application application = new Application();
        application.setAssetId(parent.getAssetId());
        application.setAssetName(parent.getAssetName());
        application.setParentId(parent.getParentId());//没有父级申请单
        application.setChildId(0);//还没有产生子申请单
        application.setLast(true);//这也是最后一张最新的申请单
        application.setStart(parent.getStop());//上一张申请单的结束状态
        application.setStop("已使用"); //出库后直接到达空闲，
        application.setBeginDate(new Date()); //设置当前时间为开始时间（创建申请单的时间）
        application.setUsername(user.getUsername());//申请人
        application.setContent(user.getName()+"发出了领用申请，领用的资产是"+parent.getAssetName()+"-"+parent.getAssetId());
        application.setType("领用");//
        application.setStatus("待处理");
//        application.setOperation("同意、拒绝");//见下面
        application.setMenu("");//nothing

        application.setManager(null);//这是新提交的申请，肯定是没有处理人的，所以这里要确保处理人为空
        application.setResultDate(null);//同上
        application.setResultContent(null);//同上
        applicationRepository.saveAndFlush(application);

        String operationAgree = "<a href=\"/application/agree?applicationId={applicationId}\" class=\"btn btn-success btn-xs\">同意</a>";//同意按钮
        String operationRefuse = "<a href=\"/application/refuse?applicationId={applicationId}\" class=\"btn btn-danger btn-xs\">拒绝</a>";//拒绝按钮
        application.setOperation(operationAgree.replace("{applicationId}", application.getId() + "")
                + operationRefuse.replace("{applicationId}", "" + application.getId()));
        Application child = applicationRepository.save(application);
        parent.setChildId(child.getId());
        applicationRepository.save(parent);
        return child;
    }

    /**
     * 同意领用申请
     * @param applicationId 领用申请单ID
     * @param user 当前登录用户
     * @return
     */
    public Application agreeUsed(long applicationId, User user) {
        //从数据库中查询申请单对象
        Application parent = applicationRepository.findById(applicationId).orElse(null);
        if (parent == null) {
            log.info("application="+parent);
            return null;
        }
        //版本14.0 新增内容 begin
        if (user == null) {
            log.info("user="+user);
            return null;//用户未登录
        }
        //版本14.0 新增内容 end

        Asset asset = assetRepository.findById(parent.getAssetId()).orElse(null);
        if(asset == null){//没有找到资产
            log.info("asset="+asset);
            return  null;
        }

        //版本14.0 新增内容 begin
        asset.setStatus(parent.getStop()); //设置为申请单的停止状态
        //版本14.0 新增内容 end
        asset.setUsername(parent.getUsername());//设置使用者
        assetRepository.save(asset);//BUG 修改资产状态要持久化到数据库
//过期

//        String status = asset.getStatus();//获得资产当前状态
//        if(status != "预定"){//你没有预定就直接同意，不合适
//            return null;//不作处理，直接返回空
//        }


        //---------------------------------------------
        parent.setLast(false);//不再是最后一张
        parent.setOperation("处理结束");
        parent.setMenu("失效");
//        parent.setManager(user.getUsername());//这是新提交的申请，肯定是没有处理人的，所以这里要确保处理人为空
//        parent.setResultDate(new Date());//同上
//        parent.setResultContent(user.getName()+"同意领用申请。");//同上

        Application application = new Application();
        application.setAssetId(parent.getAssetId());
        application.setAssetName(parent.getAssetName());
        application.setParentId(parent.getParentId());//
        application.setChildId(0);//还没有产生子申请单
        application.setLast(true);//这也是最后一张最新的申请单
        application.setStart(parent.getStart());//上一张申请单的结束状态是已使用
        application.setStop(parent.getStop()); //这里的停止状态是
        application.setBeginDate(new Date()); //设置当前时间为开始时间（创建申请单的时间）
        application.setUsername(user.getUsername());//申请人
        application.setContent(parent.getContent()+";"+user.getName()+",同意了该申请");
        application.setType("领用");//
        application.setStatus("同意");
        application.setOperation("处理结束");
        application.setMenu("");//nothing

        application.setManager(null);//这是新提交的申请，肯定是没有处理人的，所以这里要确保处理人为空
        application.setResultDate(null);//同上
        application.setResultContent(null);//同上
        applicationRepository.saveAndFlush(application);

        String operationAgree = "<a href=\"/application/agree?applicationId={applicationId}\" class=\"btn btn-success btn-xs\">同意</a>";//同意按钮
        String operationRefuse = "<a href=\"/application/refuse?applicationId={applicationId}\" class=\"btn btn-danger btn-xs\">拒绝</a>";//拒绝按钮
        application.setMenu(operationAgree.replace("{applicationId}", application.getId() + "")
                + operationRefuse.replace("{applicationId}", "" + application.getId()));

        parent.setChildId(application.getId());
        applicationRepository.save(parent);
        return applicationRepository.save(application);
        //---------------------------------------------

    }
}
