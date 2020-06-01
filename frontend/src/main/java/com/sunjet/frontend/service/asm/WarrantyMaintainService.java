package com.sunjet.frontend.service.asm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.asm.WarrantyMaintainInfo;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/8/4.
 * 维修项目
 */
@Slf4j
@Service("warrantyMaintainService")
public class WarrantyMaintainService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();


    /**
     * 通过三包单objid  获取维修项目
     *
     * @return
     */

    public List<WarrantyMaintainInfo> findAllByWarrantyMaintenanceObjId(String objId) {

        ResponseEntity<String> responseEntity = null;
        List<WarrantyMaintainInfo> warrantyMaintainInfoList = new ArrayList<>();
        try {
            if (StringUtils.isNotBlank(objId)) {
                HttpEntity requestEntity = new HttpEntity<>(objId, null);
                responseEntity = restClient.get("/warrantyMaintain/findAllByWarrantyMaintenanceObjId", requestEntity, String.class);
                List<WarrantyMaintainInfo> body = (List<WarrantyMaintainInfo>) mapper.readValue(responseEntity.getBody(), List.class);
                for (Object o : body) {
                    WarrantyMaintainInfo warrantyMaintainInfo = JsonHelper.map2Bean(o, WarrantyMaintainInfo.class);
                    warrantyMaintainInfoList.add(warrantyMaintainInfo);
                }

                log.info("WarrantyMaintainServicelmpl:findAllByWarrantyMaintenanceObjId:success");
                return warrantyMaintainInfoList;

            } else {
                return warrantyMaintainInfoList;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantyMaintainServicelmpl:findAllByWarrantyMaintenanceObjId:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 删除维修项目
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/warrantyMaintain/delete", requestEntity, Boolean.class);

            log.info("WarrantyMaintainServicelmpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("WarrantyMaintainServicelmpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 保存维修项目列表
     *
     * @param warrantyMaintains
     */
    public List<WarrantyMaintainInfo> saveList(List<WarrantyMaintainInfo> warrantyMaintains) {
        try {
            //过滤空白编号的维修项目
            Iterator<WarrantyMaintainInfo> iterator = warrantyMaintains.iterator();
            while (iterator.hasNext()) {
                if (StringUtils.isBlank(iterator.next().getCode())) {
                    iterator.remove();
                }
            }
            HttpEntity<List<WarrantyMaintainInfo>> requestEntity = new HttpEntity<>(warrantyMaintains, null);
            return restClient.post("/warrantyMaintain/saveList", requestEntity, new ParameterizedTypeReference<List<WarrantyMaintainInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存单个维修项目
     *
     * @param warrantyMaintainInfo
     * @return
     */
    public WarrantyMaintainInfo save(WarrantyMaintainInfo warrantyMaintainInfo) {
        ResponseEntity<WarrantyMaintainInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(warrantyMaintainInfo, null);
            responseEntity = restClient.post("/warrantyMaintain/save", requestEntity, WarrantyMaintainInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
