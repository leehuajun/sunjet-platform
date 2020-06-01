package com.sunjet.backend.modules.asms.repository.basic;


import com.sunjet.backend.modules.asms.entity.basic.DealerBackupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 服务站变更前备份
 * Created by Administrator on 2016/9/5.
 */
public interface DealerBackupRepository extends JpaRepository<DealerBackupEntity, String>, JpaSpecificationExecutor<DealerBackupEntity> {


}
