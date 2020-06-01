package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.AgencyBackupEntity;
import com.sunjet.backend.modules.asms.repository.basic.AgencyBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by lhj on 16/9/17.
 */
@Transactional
@Service("agencyBackupService")
public class AgencyBackupServiceImpl implements AgencyBackupService {

    @Autowired
    private AgencyBackupRepository agencyBackupRepository;

    /**
     * 查询变更前合作商信息
     *
     * @param backupInfoId
     * @return
     */
    @Override
    public AgencyBackupEntity findOneBackupInfoById(String backupInfoId) {
        return agencyBackupRepository.findOne(backupInfoId);
    }

    @Override
    public AgencyBackupEntity saveBackupInfo(AgencyBackupEntity agencyBackupEntity) {
        return agencyBackupRepository.save(agencyBackupEntity);
    }
}
