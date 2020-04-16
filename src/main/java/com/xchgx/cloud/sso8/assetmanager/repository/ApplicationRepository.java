package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    //查询同一个资产ID的申请单
    public List<Application> findAllByAssetId(long assetId);

    //查找指定资产ID的最新最后的一张申请单
    public Application findByAssetIdAndLastTrue(long assetId);

    //查询同一个资产ID并且状态为待处理的申请单
    public List<Application> findAllByAssetIdAndStatus(long assetId,String status);

    //通过申请单类型查询
    public List<Application> findAllByType(String type);

    //查询所有的维修单，同时你的状态不是待处理，也不是拒绝
    public List<Application> findAllByTypeAndStatusNotAndStatusNot(String type,String status1,String status2);

    //开始状态=空闲  and  结束状态=已使用   and  申请单状态=同意
    public List<Application> findAllByStartAndStopAndStatus(String start,String stop,String status);

    //通过状态和类型查询
    public List<Application> findAllByTypeAndStatus(String type,String status);
}
