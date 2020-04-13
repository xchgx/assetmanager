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
        applications.stream().forEach(application -> {
            Asset asset = assetRepository.findById(application.getAssetId()).orElse(null);
            if (application.getStatus().equals("待处理")) {
                String op1 = operationAgree.replace("{applicationId}",""+application.getId());
                String op2 = operationRefuse.replace("{applicationId}",""+application.getId());
                application.setOperation(op1+op2);
            }else if(asset.getStatus().equals("维修")){//不是待处理状态
                String op1=operationRepairOk.replace("{applicationId}",""+application.getId());
                String op2=operationRepairFail.replace("{applicationId}",""+application.getId());
                application.setOperation(op1+op2);
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
        });
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
        application.setAmount(1);//默认为1个资产
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
        application.setStop("使用");//统一将领用和已使用改为 使用
        //版本14.0 新增内容 end

        application.setManager(null);//这是新提交的申请，肯定是没有处理人的，所以这里要确保处理人为空
        application.setResultDate(null);//同上
        application.setResultContent(null);//同上

        return applicationRepository.save(application); //保存申请单对象并返回申请单
    }
}
