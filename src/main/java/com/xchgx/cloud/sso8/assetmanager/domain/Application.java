package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity //数据库实体对象 自动创建数据表
@Data //自动创建属性的getter方法和setter方法
public class Application {
    @Id   //表示该字段（属性）为主键
    @GeneratedValue //自动递增 整型数字自动保持不重复
    private long id;//主键

    //申请人用户名，要使用唯一特性的字段
    private String username;
    //资产来源，属于那一批采购的资产，引用入库单Id
    private long rukudanId;
    private String assetName;//资产名称
    //提交申请的时间,应该由系统自动生成
    private Date beginDate;
    //处理时间，该申请已经得到管理员的批示
    private Date resultDate;
    private int amount;//申请资产的个数
    private String content;//申请单中书写的内容，描述资产用途
    private String manager;//管理员的用户名，具有唯一特性的值
    private String resultContent;//处理结果，处理意见、处理批示，已经没有电脑可用了
    private String status;//申请单的状态 同意、拒绝、待处理
}