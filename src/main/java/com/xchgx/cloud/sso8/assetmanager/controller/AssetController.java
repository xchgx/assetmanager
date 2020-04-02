package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资产控制器
 */
@RestController
@RequestMapping("/asset")
public class AssetController {
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRepository assetRepository;
    @Autowired //入库单持久化对象
    private AssetRuKuDanRepository assetRuKuDanRepository;
    @Autowired //自动注入 申请单持久化对象
    private ApplicationRepository applicationRepository;


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


    /**
     * //查看当前空闲的电脑
     * @param rukudanId 入库单号
     * @return
     */
    @GetMapping("asset/free") //查询资产来自于空闲状态
    public List<Asset> assetFree(long rukudanId){//是不是把所有的空闲资产都查出来？
        //assetRepository; //TODO 先去解决查询数据库的代码然后过来
        List<Asset> freeAssets = assetRepository.findAllByRukudanIdAndStatus(rukudanId, "空闲");
        List<Asset> freeAssets2 = assetRepository.findAllByRukudanIdAndStatus(rukudanId, "预定");
        freeAssets.addAll(freeAssets2);//将两个集合合并成1个
        return freeAssets;//返回给前端，让使用者查看并选择哪一个资产作为领用申请单上的资产ID
    }
}
