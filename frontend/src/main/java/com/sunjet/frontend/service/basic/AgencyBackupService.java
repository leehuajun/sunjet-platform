package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.AgencyBackupInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by SUNJET_WS on 2017/7/27.
 * 合作商
 */

@Slf4j
@Service("agencyBackupService")
public class AgencyBackupService {

    @Autowired
    private RestClient restClient;


    public AgencyBackupInfo findOneBackupInfoById(String agencyBackupId) {

        ResponseEntity<AgencyBackupInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(agencyBackupId, null);
            responseEntity = restClient.get("/agency/findOneBackupInfoById", requestEntity, AgencyBackupInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AgencyBackupInfo saveBackupInfo(AgencyBackupInfo agencyBackupInfo) {
        ResponseEntity<AgencyBackupInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(agencyBackupInfo, null);
            responseEntity = restClient.post("/agency/saveBackupInfo", requestEntity, AgencyBackupInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


