package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    //查询同一个资产ID的申请单
    public List<Application> findAllByAssetIdOrderByBeginDateDesc(long assetId);

    //通过申请单类型查询
    public List<Application> findAllByType(String type);

    //查询维修过的资产的申请单
    public List<Application> findAllByStopAndStatusNotAndStatusNot(String stop, String status1,String status2);

    //通过状态和类型查询
    public List<Application> findAllByTypeAndStatus(String type,String status);
}
