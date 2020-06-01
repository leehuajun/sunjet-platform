package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.AgencyBackupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 合作商变更前备份
 * Created by lhj on 16/9/17.
 */
public interface AgencyBackupRepository extends JpaRepository<AgencyBackupEntity, String>, JpaSpecificationExecutor<AgencyBackupEntity> {

}
