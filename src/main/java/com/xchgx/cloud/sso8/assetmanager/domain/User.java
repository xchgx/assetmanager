package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.*;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户类
 */
@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private long id; //主键
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
