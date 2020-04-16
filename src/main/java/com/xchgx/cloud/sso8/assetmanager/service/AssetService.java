package com.xchgx.cloud.sso8.assetmanager.service;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRepository assetRepository;

    @Autowired //入库单持久化对象
    private AssetRuKuDanRepository assetRuKuDanRepository;

    @Autowired //自动注入 申请单持久化对象
    private ApplicationRepository applicationRepository;

    @Autowired
    private ToolService toolService;

    /**
     * 资产出库
     * @param rukudanId 入库单
     * @param username 当前登录用户
     * @return
     */
    public Asset chuku(long rukudanId, String username){

        AssetRuKuDan ruKuDan = assetRuKuDanRepository.findById(rukudanId).orElse(null);//通过ID查询入库单对象
        if(ruKuDan == null){//如果没有找到对应的入库单就返回null
            System.out.println("无法出库");
            return null;
        }
        System.out.println(ruKuDan.getId());

        Asset asset = new Asset();//创建资产对象
        toolService.rukudanToAsset(ruKuDan, asset);
        asset.setStatus("空闲");//或者未使用
//        assetRepository.save(asset);
        int remainded = ruKuDan.getRemained();//获得入库单中的剩余数量
        remainded--;//减少一台
        ruKuDan.setRemained(remainded);//重新赋值剩余数量
        assetRuKuDanRepository.save(ruKuDan);//重新保存到数据库中
        //以上为新增部分v2.0 2020年3月31日10:43:12
        Asset asset1 = assetRepository.saveAndFlush(asset);
        Application application = toolService.createFirstApplication(asset1,username);
        applicationRepository.save(application);
        String operation1 = "<a href=\"/application/addQuick?type=已使用&applicationId="+application.getId()+"&assetId="+asset1.getId()+"\">提交领用申请</a>";
        String operation2 = "<a href=\"/application/addQuick?type=维修&applicationId="+application.getId()+"&assetId="+asset1.getId()+"\">提交维修申请</a>";
        application.setMenu(operation1+operation2);//暂时只支持空闲到领用和借用的操作
        applicationRepository.save(application);
        return assetRepository.save(asset1);//保存并返回资产对象
    }

    /**
     * //查询所有资产
     * @return
     */
    public List<Asset> list(){
        return assetRepository.findAll();//查询所有资产
    }


    /**
     * 查询指定批次（入库单号）下的空闲资产
     * @param rukudanId
     * @return
     */
    public List<Asset> assetFree( long rukudanId){

        //assetRepository; //TODO 先去解决查询数据库的代码然后过来
        List<Asset> freeAssets = assetRepository.findAllByRukudanIdAndStatus(rukudanId, "空闲");
        List<Asset> freeAssets2 = assetRepository.findAllByRukudanIdAndStatus(rukudanId, "预定");
        freeAssets.addAll(freeAssets2);//将两个集合合并成1个
        return freeAssets;//返回给前端，让使用者查看并选择哪一个资产作为领用申请单上的资产ID
    }


    /**
     * 通过资产状态查询资产
     * @param status 资产状态 空闲、已使用、借用、维修、报废
     * @return
     */
    public List<Asset> listByStatus(String status) {
        return assetRepository.findAllByStatus(status);
    }

    /**
     *
     * 快速通道，直接设置资产的状态（由管理员执行）
     * @param status 新的状态，无视前置状态
     * @param assetId 资产实体对象
     * @return
     */
    public Asset set(String status, long assetId){
        Asset asset = assetRepository.findById(assetId).orElse(null); //通过资产ID查询资产对象
        //TODO 这里应该判断一下是否存在该资产
        if (asset == null) {
            return null;//如果不存在该资产，则不进行状态更新操作。
        }
        asset.setStatus(status);//设置资产状态为status参数1值
        return assetRepository.save(asset);//赶紧保存到数据库中
    }
}
