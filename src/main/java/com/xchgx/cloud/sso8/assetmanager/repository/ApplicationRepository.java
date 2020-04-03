package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    public List<Application> findAllByAssetId(long assetId);

    public List<Application> findAllByType(String type);

    public List<Application> findAllByTypeAndStatus(String type, String status);
}
