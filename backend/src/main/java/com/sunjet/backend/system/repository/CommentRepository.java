package com.sunjet.backend.system.repository;

import com.sunjet.backend.modules.asms.entity.flow.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/27.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {
    @Query("select ce from CommentEntity ce where ce.flowInstanceId=?1 order by ce.doDate asc")
    List<CommentEntity> findAllByProcessInstanceId(String processInstanceId);
}