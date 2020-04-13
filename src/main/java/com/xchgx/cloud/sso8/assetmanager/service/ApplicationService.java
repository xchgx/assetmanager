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
     * 版本15.0 新增方法
     * 获得所有带管理员操作项的申请单
     * @return
     */
    public List<Application> allApplication(){
        List<Application> applications = applicationRepository.findAll();
        String operationAgree = "<a href=\"/application/agree?applicationId={applicationId}\" class=\"btn btn-success btn-xs\">同意</a>";//同意按钮
        String operationRefuse = "<a href=\"/application/refuse?applicationId={applicationId}\" class=\"btn btn-danger btn-xs\">拒绝</a>";//拒绝按钮
        String operationRepairOk = "<a href=\"/application/repairOk?applicationId={applicationId}\" class=\"btn btn-success btn-xs\">维修成功</a>";//维修成功
        String operationRepairFail = "<a href=\"/application/repairFail?applicationId={applicationId}\" class=\"btn btn-warning btn-xs\">维修失败</a>";//维修失败
        String operationOver = "处理结束";
        String operationRepairOver = "维修结束";
        for (Application application : applications) {
            Asset asset = assetRepository.findById(application.getAssetId()).orElse(null);
            if (application.getStatus().equals("待处理")) {
                String op1 = operationAgree.replace("{applicationId}", "" + application.getId());
                String op2 = operationRefuse.replace("{applicationId}", "" + application.getId());
                application.setOperation(op1 + op2);
//            } else if (asset.getStatus().equals("维修")) {
//                String op1 = operationRepairOk.replace("{applicationId}", "" + application.getId());
//                String op2 = operationRepairFail.replace("{applicationId}", "" + application.getId());
//                application.setOperation(op1 + op2);
            } else if(application.getStop().equals("维修") && application.getStatus().equals("同意")) {//不是待处理状态
                String op1 = operationRepairOk.replace("{applicationId}", "" + application.getId());
                String op2 = operationRepairFail.replace("{applicationId}", "" + application.getId());
                application.setOperation(op1 + op2);
            }else if(application.getStart().equals("维修") && application.getStatus().equals("拒绝")){
                String op1 = operationRepairOk.replace("{applicationId}", "" + application.getId());
                String op2 = operationRepairFail.replace("{applicationId}", "" + application.getId());
                application.setOperation(op1 + op2);
            }else{
                application.setOperation(operationOver);//处理结束
            }

//            if(!application.getStatus().equals("处理结束")) {
//                long assetId = application.getAssetId();
//                Asset asset = assetRepository.findById(assetId).orElse(null);
//                if (application.getStatus().equals("待处理")) {
//                    System.out.println(operationAgree.replace("{applicationId}", "" + application.getId()));
//                    String operation =
//                            operationAgree.replace("{applicationId}", "" + application.getId()) +
//                                    operationRefuse.replace("{applicationId}", "" + application.getId());
//                    application.setOperation(operation);
//                } else {
//                    if (application.getType().equals("维修")) {
//                        if (asset.getStatus().equals("维修") && application.getStatus().contains("维修")) {
//                            application.setOperation(operationRepairOver);
//                        }else if(asset.getStatus().equals("维修") && ( application.getStatus().contains("同意") || application.getStatus().contains("拒绝") )){
//
//                            String operation =
//                                    operationRepairOk.replace("{applicationId}", "" + application.getId()) +
//                                            operationRepairFail.replace("{applicationId}", "" + application.getId());
//                            application.setOperation(operation);
//                        }  else {
//                            application.setOperation(operationOver);
//                        }
//                    } else {
//                        application.setOperation(operationOver);
//                    }
//                }
//            }
        }

        return applications;
    }
}
