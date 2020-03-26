package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 资产类，域对象
 */
@Entity //创建数据表相关代码 在类上添加注解，说明该类是数据表
@Getter //自动添加get方法
@Setter //自动添加set方法

public class Asset{ //资产类
    @Id //创建数据表相关代码  主键
    @GeneratedValue //创建数据表相关代码  自动递增
    private long id; //数据表的主键
//    private long assetRuKuDanId;//来自那一批采购的入库单。这里是hi入库单主键
    private long rukudanId;//来自那一批采购的入库单。这里是 入库单
    private String username;//使用者
}