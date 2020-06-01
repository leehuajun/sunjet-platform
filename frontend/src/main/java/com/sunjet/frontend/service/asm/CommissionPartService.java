package com.sunjet.frontend.service.asm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.asm.CommissionPartInfo;
import com.sunjet.dto.asms.asm.CommissionPartItem;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/8/4.
 * 配件需求列表
 */
@Slf4j
@Service("commissionPartService")
public class CommissionPartService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();


    /**
     * 通过三包单objid  获取配件需求列表
     *
     * @return
     */

    public List<CommissionPartInfo> findAllByWarrantyMaintenanceObjId(String objId) {

        ResponseEntity<String> responseEntity = null;
        try {
            //HttpEntity requestEntity = new HttpEntity<>(objId, null);
            //responseEntity = restClient.get("/commissionPart/findAllByWarrantyMaintenanceObjId", requestEntity, String.class);
            //
            //List<CommissionPartInfo> body = (List<CommissionPartInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            //List<CommissionPartInfo> commissionPartInfos = new ArrayList<>();
            //for (Object o : body) {
            //    CommissionPartInfo commissionPartInfo = JsonHelper.map2Bean(o, CommissionPartInfo.class);
            //    commissionPartInfos.add(commissionPartInfo);
            //}
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            log.info("CommissionPartServicelmpl:findAllByWarrantyMaintenanceObjId:success");
            return restClient.findAll("/commissionPart/findAllByWarrantyMaintenanceObjId", requestEntity, new ParameterizedTypeReference<List<CommissionPartInfo>>() {
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.info("CommissionPartServicelmpl:findAllByWarrantyMaintenanceObjId:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 删除配件
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/commissionPart/delete", requestEntity, Boolean.class);

            log.info("CommissionPartServicelmpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("CommissionPartServicelmpl:delete:error:" + e.getMessage());
            return false;
        }


    }

    /**
     * 通过活动服务单Id查询配件列表
     *
     * @param objId
     * @return
     */

    public List<CommissionPartInfo> findAllByActivityMaintenanceObjId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            return restClient.findAll("/commissionPart/findAllByActivityMaintenanceObjId", requestEntity, new ParameterizedTypeReference<List<CommissionPartInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("CommissionPartServicelmpl:findAllByActivityMaintenanceObjId:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过三包单IdList查所有的需求配件信息
     *
     * @param warrantyMaintenanceIdList
     * @return
     */
    public List<CommissionPartItem> findAllByWarrantyMaintenanceIdList(List<String> warrantyMaintenanceIdList) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(warrantyMaintenanceIdList, null);
            return restClient.findAll("/commissionPart/findAllByWarrantyMaintenanceIdList", requestEntity, new ParameterizedTypeReference<List<CommissionPartItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("CommissionPartServicelmpl:findAllByWarrantyMaintenanceIdList:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过活动单IdList查所有的需求配件信息
     *
     * @param activityMaintenanceIdList
     * @return
     */
    public List<CommissionPartItem> findAllByActivityMaintenanceIdList(List<String> activityMaintenanceIdList) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(activityMaintenanceIdList, null);
            return restClient.findAll("/commissionPart/findAllByActivityMaintenanceIdList", requestEntity, new ParameterizedTypeReference<List<CommissionPartItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("CommissionPartServicelmpl:findAllByActivityMaintenanceIdList:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据三包单id删除维修配件
     *
     * @param objId
     */
    public Boolean deleteByWarrantyMaintenanceObjId(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/commissionPart/deleteByWarrantyMaintenanceObjId", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存配件列表
     *
     * @param commissionParts
     * @return
     */
    public List<CommissionPartInfo> saveList(List<CommissionPartInfo> commissionParts) {
        try {
            //过滤空白编号的维修项目
            Iterator<CommissionPartInfo> iterator = commissionParts.iterator();
            while (iterator.hasNext()) {
                if (StringUtils.isBlank(iterator.next().getPartCode())) {
                    iterator.remove();
                }
            }
            HttpEntity<List<CommissionPartInfo>> requestEntity = new HttpEntity<>(commissionParts, null);
            return restClient.post("/commissionPart/saveList", requestEntity, new ParameterizedTypeReference<List<CommissionPartInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
