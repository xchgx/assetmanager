package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Log {
    @Id
    @GeneratedValue
    private long id;

    private String username;//登录用户

    private String url;//客户端发出请求时的完整URL。
    private String clientIp;//浏览器客户机的IP地址
    private String clientHost;//发出请求的客户机的完整主机名
    private int clientPort;//客户端的端口号
    private String serverIp;//WEB服务器的IP地址
    private String serverName;//WEB服务器的主机名

    private String userAgent;//浏览器基本信息
    private Date visit;//访问时间


}
