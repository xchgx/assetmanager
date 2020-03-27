package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
@Entity
@Data
public class Asset { //资产类
    @Id //创建数据表相关代码  主键
    @GeneratedValue //创建数据表相关代码  自动递增
    private long id; //数据表的主键
//    private int amount;//资产数量 //单个资产不需要数量
    private String name;//资产名称
    private String type;//资产类型
    private String readme;//资产说明书
    private float price;//资产单价
    private String status;//资产状态 // 入库单没有资产状态
    private Date scrq;//生产日期
    private String bzq;//保质期
    private Date rukuDate;//入库日期，相当于采购批次
    private long rukudanId;//保存入库单主键ID
    private String username;//用户名 谁在使用我们的资产 资产使用者
}
