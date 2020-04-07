package com.xchgx.cloud.sso8.assetmanager.repository;

import com.xchgx.cloud.sso8.assetmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户持久化类
 */
public interface UserRepository extends JpaRepository<User,Long> {
    /**
     * 通过用户名查询用户对象
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);
}
