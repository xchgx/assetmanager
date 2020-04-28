package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity //实体注解
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class VisitLog {
    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String ip;//IP地址
    @NonNull
    private String os;//操作系统
    @NonNull
    private String url;//访问的接口地址
    @NonNull
    private Date date;//访问的时间
    @NonNull
    private String username;//用户名
    @NonNull
    private String name;//姓名
    private String browser;//浏览器信息
    private String method;//浏览器方法
    private String result;//访问的结果

}
