package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户类
 */
@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private long id; //主键
    private String username; //用户名
    private String password; //密码
    private String name; //姓名
    private String role;//权限
}
