package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 创建 资产持久化接口
 * 只要继承了JpaRepository，我们就自动拥有了添加，修改，删除的功能
 */
public interface AssetRepository extends JpaRepository<Asset,Long> {//继承数据库的标准操作功能
    public List<Asset> findAllByRukudanIdAndStatus(long rukudanId,String status);
    public List<Asset> findAllByStatus(String status);

    /**
     * 通过用户名查询资产，查询资产的使用者
     * @param username 使用者用户名
     * @return 资产集合
     */
    public List<Asset> findAllByUsername(String username);
}
