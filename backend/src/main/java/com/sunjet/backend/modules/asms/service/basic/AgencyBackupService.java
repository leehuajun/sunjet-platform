package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.AgencyBackupEntity;

/**
 * Created by lhj on 16/9/17.
 * 合作商变更前备份
 */
public interface AgencyBackupService {

    AgencyBackupEntity findOneBackupInfoById(String backupInfoId);

    AgencyBackupEntity saveBackupInfo(AgencyBackupEntity agencyBackupEntity);
}
