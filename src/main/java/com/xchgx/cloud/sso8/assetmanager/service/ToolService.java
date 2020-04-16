package com.xchgx.cloud.sso8.assetmanager.service;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ToolService {
    /**
     * 将入库单中的属性一对一的赋值给资产对象。
     * 除了使用者和资产状态为null，其它的属性均来自于入库单
     * @param ruKuDan 入库单对象(!null)
     * @param asset 资产对象(!null)
     * @return
     */
    public Asset rukudanToAsset(AssetRuKuDan ruKuDan, Asset asset) {
        asset.setType(ruKuDan.getType());
        asset.setReadme(ruKuDan.getReadme());
        asset.setScrq(ruKuDan.getScrq());
        asset.setBzq(ruKuDan.getBzq());
        asset.setPrice(ruKuDan.getPrice());
        asset.setRukudanId(ruKuDan.getId());
        asset.setName(ruKuDan.getName());//设置资产的名称为入库单中的资产名称
        asset.setUsername(null);
        asset.setStatus(null);
        return asset;
    }

    /**
     * 创建一张新的申请单
     * @return
     */
    public Application createFirstApplication(Asset asset,String manager){
        //创建第一张申请单   版本4.0 2020年4月15日23:35:46 begin
        Application application = new Application();
        application.setAssetId(asset.getId());
        application.setAssetName(asset.getName());
        application.setParentId(0);//没有父级申请单
        application.setChildId(0);//还没有产生子申请单
        application.setLast(true);//这也是最后一张最新的申请单
        application.setStop("空闲"); //出库后直接到达空闲，
        application.setStart("库存");//这是唯一一个没有开始状态的申请单，它来自于入库单
        application.setBeginDate(new Date()); //设置当前时间为开始时间（创建申请单的时间）
        application.setUsername(null);
        application.setContent("资产出库");
//        application.setManager(username); // 处理人是同一个人
        application.setType("出库");//临时添加，实际上没有这种申请
        application.setStatus("同意");//自动就同意了
//        application.setResultDate(new Date());
//        application.setResultContent("系统自动同意，这是该资产的第一张申请单");
        application.setOperation("处理结束");
        //创建第一张申请单   版本4.0 2020年4月15日23:35:46 end
        return handleApplication(application, manager, "系统自动同意，这是该资产的第一张申请单");
    }

    /**
     * 处理申请单
     * @param application 申请单对象
     * @return
     */
    public Application handleApplication(Application application,String manager, String resultContent) {
        application.setManager(manager); // 处理人是同一个人
        application.setResultDate(new Date());
        application.setResultContent(resultContent);
        return application;
    }
}
