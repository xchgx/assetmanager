package com.xchgx.cloud.sso8.assetmanager.service;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Application> applications = applicationRepository.findAllByOrderByIdDesc();
        //申请单的操作选项， begin
        return addOperation(applications);
    }

    /**
     * 添加申请单后面的操作项
     * @param applications 原始的申请单集合
     * @return 添加完操作项之后的集合
     */
    private List<Application> addOperation(List<Application> applications){

        String operationAgree = "<a href=\"javascript:toUrl('/application/agree?applicationId={applicationId}');\" class=\"btn btn-success btn-xs\">同意</a>";//同意按钮
        String operationRefuse = "<a href=\"javascript:toUrl('/application/refuse?applicationId={applicationId}');\" class=\"btn btn-danger btn-xs\">拒绝</a>";//拒绝按钮
        String operationRepairOk = "<a href=\"javascript:toUrl('/application/repairOk?applicationId={applicationId}');\" class=\"btn btn-success btn-xs\">维修成功</a>";//维修成功
        String operationRepairFail = "<a href=\"javascript:toUrl('/application/repairFail?applicationId={applicationId}');\" class=\"btn btn-warning btn-xs\">维修失败</a>";//维修失败
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
}
