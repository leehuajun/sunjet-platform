package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.DealerBackupEntity;
import com.sunjet.backend.modules.asms.repository.basic.DealerBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lhj on 16/9/17.
 * 服务站基础信息
 */
@Service("dealerBackupService")
public class DealerBackupServiceImpl implements DealerBackupService {
    @Autowired
    private DealerBackupRepository dealerBackupRepository;

    @Override
    public DealerBackupEntity findOneBackupById(String backupById) {
        return dealerBackupRepository.findOne(backupById);
    }

    @Override
    public DealerBackupEntity saveDealerBackup(DealerBackupEntity dealerBackupEntity) {
        return dealerBackupRepository.save(dealerBackupEntity);
    }
}
