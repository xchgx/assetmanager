package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    //查询同一个资产ID的申请单
    public List<Application> findAllByAssetId(long assetId);
    //查询同一个资产ID并且状态为待处理的申请单
    public List<Application> findAllByAssetIdAndStatus(long assetId,String status);

    //通过申请单类型查询
    public List<Application> findAllByType(String type);

    //通过状态和类型查询
    public List<Application> findAllByTypeAndStatus(String type,String status);
}
