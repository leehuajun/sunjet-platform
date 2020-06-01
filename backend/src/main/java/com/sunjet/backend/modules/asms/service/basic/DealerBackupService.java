package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.DealerBackupEntity;

/**
 * Created by lhj on 16/9/17.
 * 服务站变更前备份信息
 */
public interface DealerBackupService {


    DealerBackupEntity findOneBackupById(String backupById);

    DealerBackupEntity saveDealerBackup(DealerBackupEntity dealerBackupEntity);
}
