package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.VisitLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog,Long> {
    public Page<VisitLog> findAll(Pageable pageRequest);
    public Page<VisitLog> findAllByUsernameNotOrderByDate(String username, Pageable pageRequest);
}
