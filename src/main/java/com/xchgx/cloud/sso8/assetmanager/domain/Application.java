package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 申请类，用户提出各种申请（领用申请、维修申请等）
 */
@Entity //实体，自动建表
@Data //自动生成getter 和setter
public class Application {//申请类
    @Id //主键
    @GeneratedValue //主键ID自动递增
    private long id; //主键
    private String type; //申请类型
    private String username; //申请人
    private String content; //申请内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate; //申请日期
    private String status;//状态
    private String opinion;//处理意见
}
