package com.xchgx.cloud.sso8.assetmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 这个不是持久化类，可以不用加实体注解@Entity
 * 默认为a标签
 */
@Data //提供了getter和setter方法
@AllArgsConstructor //all所有的，Args参数，Constructor构造器
@NoArgsConstructor //无参数构造器
public class Operation {
    private String url;//访问接口地址
    private String content;//标签内容

    @Override
    public String toString() {
        return "<a href='" + url +"'>" + content + "</a>" ;
    }
}
