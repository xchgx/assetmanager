package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户类
 */
@Entity
@Data
@ToString
public class User {
    @Id
    @GeneratedValue
    private long id; //主键
    private String username; //用户名
    private String password; //密码
    private String name; //姓名
    private String role;//权限
    private String department;//工作部门
}
