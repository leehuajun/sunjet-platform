package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.DealerBackupInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 服务站变更去备份信息列表
 * Created by zyf on 2017/7/27.
 */
@Slf4j
@Service("dealerBackupService")
public class DealerBackupService {

    @Autowired
    private RestClient restClient;


    public DealerBackupInfo findOneBackupById(String dealerBackupId) {
        ResponseEntity<DealerBackupInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(dealerBackupId, null);
            responseEntity = restClient.get("/dealer/findOneBackupById", requestEntity, DealerBackupInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DealerBackupInfo saveDealerBackup(DealerBackupInfo dealerBackupInfo) {
        ResponseEntity<DealerBackupInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(dealerBackupInfo, null);
            responseEntity = restClient.get("/dealer/saveDealerBackup", requestEntity, DealerBackupInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
