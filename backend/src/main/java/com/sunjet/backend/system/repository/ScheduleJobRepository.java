package com.sunjet.backend.system.repository;


import com.sunjet.backend.system.entity.ScheduleJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * ModuleRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, String>, JpaSpecificationExecutor<ScheduleJobEntity> {

    /**
     * 查询所有启用的任务
     *
     * @return
     */
    @Query("select sj from ScheduleJobEntity sj where sj.enabled =true")
    List<ScheduleJobEntity> findAllByEnabled();

}
