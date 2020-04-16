package com.xchgx.cloud.sso8.assetmanager.service;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 版本15.0 新增类
 *
 */
@Service
public class ApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    private AssetRepository assetRepository;


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
    public Application addQuick(String type, long assetId, String username) {
        Asset asset = assetRepository.findById(assetId).orElse(null); //通过资产ID查询资产对象
        if (asset == null) {//如果资产ID不存在，则直接返回，不处理。
            return null;
        }

        Application application = new Application(); //创建新的申请单对象
//        application.setAmount(1);//默认为1个资产
        application.setContent("该申请为快速申请，由扫码提交。"); //设置申请内容-申请理由。
        application.setType(type);//设置申请单类型为参数type的值
        application.setBeginDate(new Date());//设置当前时间为申请单的创建时间
        application.setUsername(username);//设置申请单的发起人=申请人
        application.setAssetName(asset.getName());//申请单的资产名称来自资产对象的名称
        application.setAssetId(assetId); // 申请单的资产ID既可以是形式参数assetId，也可以是资产对象的id属性=asset.getId();
        application.setStatus("待处理");//申请单的状态是“同意”、“拒绝”、“待处理”三种，并不是资产的状态，要区分。
        //版本14.0 新增内容 begin
        application.setStart(asset.getStatus());
//        application.setStop(type.equals("领用")?"已使用":type);
        application.setStop(type);//统一将领用和已使用改为 使用
        //版本14.0 新增内容 end
        application.setOperation("同意、拒绝");
        application.setMenu("无");
//        application.setChildId();

        application.setManager(null);//这是新提交的申请，肯定是没有处理人的，所以这里要确保处理人为空
        application.setResultDate(null);//同上
        application.setResultContent(null);//同上

        return applicationRepository.save(application); //保存申请单对象并返回申请单
    }
}
