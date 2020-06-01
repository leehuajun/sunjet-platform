package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by SUNJET_WS on 2017/9/26.
 */
@Service("baseService")
@Slf4j
public class BaseService {

    @Autowired
    private RestClient restClient;


    public FlowDocInfo save(FlowDocInfo flowDocInfo) {
        ResponseEntity<FlowDocInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(flowDocInfo, null);
            responseEntity = restClient.post("/base/save", requestEntity, FlowDocInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
