package com.xchgx.cloud.sso8.assetmanager.controller;

import com.xchgx.cloud.sso8.assetmanager.domain.*;
import com.xchgx.cloud.sso8.assetmanager.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 资产控制器
 */
@RestController
@RequestMapping("/asset") //访问网址前缀 /asset
public class AssetController {
    @Autowired
    private AssetService assetService;

    @GetMapping("/chuku") //添加资产，资产出库
    public Asset chuku(@RequestParam long rukudanId, HttpServletRequest request){ //从哪一个批次（入库单）的资产中出库1台
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        return assetService.chuku(rukudanId, user.getUsername());
    }

    @GetMapping("/list") //访问网址是 http://localhost:8080/list
    public List<Asset> list(){//列出所有资产

        return assetService.list();
    }


    /**
     * //查看当前空闲的电脑
     * @param rukudanId 入库单号
     * @return
     */
    @GetMapping("/free") //查询资产来自于空闲状态
    public List<Asset> assetFree(long rukudanId){//是不是把所有的空闲资产都查出来？
        return assetService.assetFree(rukudanId);
    }

    /**
     * 通过状态查询资产
     * http://localhost:8080/asset/findByStatus?status=已使用
     * @param status 状态
     * @return
     */
    @GetMapping("/findByStatus")
    public List<Asset> findByStatus(String status) {
        return assetService.listByStatus(status);
    }


    /**
     * 快速通道，直接设置资产的状态（由管理员执行）
     * @param status 新的状态，无视前置状态
     * @param assetId 资产实体对象
     * @return
     */
    @GetMapping("/set")
    public Asset set(String status, long assetId){
        return assetService.set(status, assetId);
    }

}
