package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 申请持久化类
 * 只要继承了JpaRepository，我们就自动拥有了添加，修改，删除的功能
 */
public interface ApplicationRepository extends JpaRepository<Application,Long> {//继承数据库的标准操作功能
}
