package com.sunjet.backend.system.repository;

import com.sunjet.backend.system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: lhj
 * @create: 2017-07-02 19:56
 * @description: 说明
 */
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @Query("select u from UserEntity u where u.logId=?1")
    UserEntity findOneByLogId(String logId);

    @Query(value = "select u from UserEntity u,UserWithRoleEntity s where u.objId = s.userId and s.roleId=?1")
    List<UserEntity> findAllByRoleId(String roleId);

    @Query("select u from UserEntity u where u.enabled=true and u.dealerId<>null and u.dealerId=?1")
    List<UserEntity> findAllByDealerId(String parentId);

    @Query("select u from UserEntity u where u.enabled=true and u.agencyId<>null and u.agencyId=?1")
    List<UserEntity> findAllByAgencyId(String objId);

    @Query("select u from UserEntity u where u.logId=?1 and u.enabled =true ")
    UserEntity findOneByEnabledLogId(String logId);

    /**
     * 查账找所有启用并且有邮箱账号的用户
     *
     * @return
     */
    @Query("select u from UserEntity u where  u.enabled =true and u.email IS NOT NULL ")
    List<UserEntity> findAllByEnabledAndEmailIsNotNull();

}
