package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户类
 */
@Entity
@Data
@ToString
@AllArgsConstructor //所有属性的构造方法（包括id）
@NoArgsConstructor //无参数构造方法
@RequiredArgsConstructor //必须的属性构造方法
public class User {
    @Id
    @GeneratedValue
    private long id; //主键 自动递增，不用认为的输入
    @NonNull
    private String username; //用户名
    @NonNull
    private String password; //密码
    @NonNull
    private String name; //姓名
    @NonNull
    private String role;//权限
    @NonNull
    private String department;//工作部门
}
