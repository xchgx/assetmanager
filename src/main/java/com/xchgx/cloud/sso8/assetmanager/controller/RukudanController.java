package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.AssetRuKuDan;
import com.xchgx.cloud.sso8.assetmanager.repository.ApplicationRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRepository;
import com.xchgx.cloud.sso8.assetmanager.repository.AssetRuKuDanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 入库单控制器
 */
@RestController
@RequestMapping("/rukudan") //接口网址前缀
public class RukudanController {
    //导入数据库持久化对象
    @Autowired //自动注入，理解为自动导入类对象
    private AssetRepository assetRepository;
    @Autowired //入库单持久化对象
    private AssetRuKuDanRepository assetRuKuDanRepository;
    @Autowired //自动注入 申请单持久化对象
    private ApplicationRepository applicationRepository;

//    /**
//     * 添加入库单
//     * @param name 入库单的资产名称
//     * @param amount 入库单的资产数量
//     * @return
//     */
//    @GetMapping("/add") //添加资产，资产入库，实际访问地址 /rukudan/add
//    public AssetRuKuDan add(@RequestParam String name, @RequestParam int amount){ //接收前端传送过来的参数
//        AssetRuKuDan assetRuKuDan = new AssetRuKuDan(); //创建一个资产对象
//        assetRuKuDan.setName(name); //设置资产名称-来自参数 name
//        assetRuKuDan.setAmount(amount);  //设置资产数量-来自参数 number
//        assetRuKuDan.setRemained(amount);//设置剩余数量为入库数量
//        AssetRuKuDan resultAsset = assetRuKuDanRepository.save(assetRuKuDan);//保存资产对象到数据库中，然后返回存储后的资产对象
//        return resultAsset; //返回存储后的资产对象
//    }
    /**
     * 添加入库单
     * @param name 入库单的资产名称
     * @param amount 入库单的资产数量
     * @return
     */
    @PostMapping("/add") //添加资产，资产入库，实际访问地址 /rukudan/add
    public AssetRuKuDan add(@RequestParam("pic")MultipartFile pic, @RequestParam String name, @RequestParam int amount){ //接收前端传送过来的参数
        String fileName = System.currentTimeMillis() + pic.getOriginalFilename();//防止重名
        String destFileName = "D:\\upload\\" + fileName;
        File destFile = new File(destFileName);
        destFile.getParentFile().mkdirs();
        System.out.println(destFile);
        try {
            pic.transferTo(destFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        AssetRuKuDan assetRuKuDan = new AssetRuKuDan(); //创建一个资产对象
        assetRuKuDan.setName(name); //设置资产名称-来自参数 name
        assetRuKuDan.setAmount(amount);  //设置资产数量-来自参数 number
        assetRuKuDan.setRemained(amount);//设置剩余数量为入库数量
        AssetRuKuDan resultAsset = assetRuKuDanRepository.save(assetRuKuDan);//保存资产对象到数据库中，然后返回存储后的资产对象
        return resultAsset; //返回存储后的资产对象
    }

    /**
     * 查找所有输入单
     * @return
     */
    @GetMapping("/list") //访问网址是 http://localhost:8080/rukudan/list
    public List<AssetRuKuDan> list2(){//列出所有资产
        return assetRuKuDanRepository.findAll();//查询所有资产
    }


    /**
     * 查询入库单的剩余数量
     * @param rukudanId 入库单号
     * @return
     */
    //查看入库单的库存量（剩余数量）
    @GetMapping("/remained")
    public int rukudanRemained(long rukudanId){
        //查询入库单对象
        AssetRuKuDan assetRuKuDan = assetRuKuDanRepository.findById(rukudanId).orElse(null);
        return assetRuKuDan.getRemained();//获得该入库单的剩余数量
    }
}
