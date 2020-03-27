package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 资产类，域对象
 */
@Entity //创建数据表相关代码 在类上添加注解，说明该类是数据表
@Getter //自动添加get方法
@Setter //自动添加set方法
public class AssetRukudan { //资产类
    @Id //创建数据表相关代码  主键
    @GeneratedValue //创建数据表相关代码  自动递增
    private long id; //数据表的主键
    private int amount;//资产数量
    private String name;//资产名称
    private String type;//资产类型
    private String readme;//资产说明书
    private float price;//资产单价
//    private String status;//资产状态 // 入库单没有资产状态
    private Date scrq;//生产日期
    private String bzq;//保质期
    private Date rukuDate;//入库日期，相当于采购批次
}
