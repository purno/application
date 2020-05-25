package com.application.dao.jparepository;

import com.application.dao.annotations.ReadOnly;
import com.application.dao.entities.UserInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {


    @Cacheable(value = "clientCache", unless = "#result == null")
    @ReadOnly
    UserInfo findByClientEmailAndIsActiveTrue(String clientEmail);


}
