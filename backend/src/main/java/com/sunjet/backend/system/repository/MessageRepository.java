package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * ConfigRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface MessageRepository extends JpaRepository<MessageEntity, String>, JpaSpecificationExecutor<MessageEntity> {

    @Query("select me from MessageEntity me where me.orgId=?1 and me.isRead=false order by me.createdTime desc")
    List<MessageEntity> findAllUnRead(String orgId);

    @Query("select me from MessageEntity me where me.refId=?1 order by me.createdTime desc")
    MessageEntity findByRefId(String refId);

    @Query("select me from MessageEntity me where me.logId=?1 and me.isRead=false order by me.createdTime desc")
    List<MessageEntity> findAllUnReadByLogId(String logId);
}
