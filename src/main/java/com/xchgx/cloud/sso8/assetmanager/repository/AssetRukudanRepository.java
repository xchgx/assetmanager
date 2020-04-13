package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.AssetRukudan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 创建 资产入库单持久化接口
 * 只要继承了JpaRepository，我们就自动拥有了添加，修改，删除的功能
 */
public interface AssetRukudanRepository extends JpaRepository<AssetRukudan,Long> {//继承数据库的标准操作功能
}
