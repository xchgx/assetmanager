package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class VisitLog {
    @Id
    @GeneratedValue
    Long id;

    @NonNull
    private String url;//访问地址
    @NonNull
    private String ip;//客户端IP
    @NonNull
    private String browser;//浏览器信息
    @NonNull
    private String username;//用户名
    @NonNull
    private String name;//姓名
    @NonNull
    private String query;//查询字符串
    @NonNull
    private String method;//访问方式
    @NonNull
    private String os;//客户端端口
    private String result;//客户端端口
    private Date date = new Date();//浏览器信息
}
