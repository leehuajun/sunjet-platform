package com.sunjet.frontend.service.recycle;

import com.sunjet.dto.asms.recycle.RecycleInfo;
import com.sunjet.dto.asms.recycle.RecycleItem;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/8/10.
 * 故障件返回通知单
 */
@Slf4j
@Service("recycleService")
public class RecycleService {

    @Autowired
    private RestClient restClient;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */

    public PageResult<RecycleItem> getPageList(PageParam<RecycleItem> pageParam) {
        try {
            log.info("RecycleServiceImpl:getPageList:success");
            return restClient.getPageList("/recycle/getPageList", pageParam, new ParameterizedTypeReference<PageResult<RecycleItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */

    public RecycleInfo findOneById(String objId) {
        ResponseEntity<RecycleInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/recycle/findOneById", requestEntity, RecycleInfo.class);

            List<RecycleItemInfo> recycleItemInfos = restClient.findAll("/recycleItem/findByRecycle", requestEntity, new ParameterizedTypeReference<List<RecycleItemInfo>>() {
            });
            RecycleInfo recycleInfo = responseEntity.getBody();
            recycleInfo.setRecycleItemInfoList(recycleItemInfos);
            return recycleInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 新增/保存
     *
     * @param recycleInfo
     * @return
     */

    public RecycleInfo save(RecycleInfo recycleInfo) {
        try {
            HttpEntity<RecycleInfo> httpEntity = new HttpEntity<>(recycleInfo, null);
            ResponseEntity<RecycleInfo> responseEntity = restClient.post("/recycle/save", httpEntity, RecycleInfo.class);
            log.info("RecycleServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 objId 删除一个实体
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/recycle/delete", httpEntity, Boolean.class);
            log.info("RecycleServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 启动流程
     *
     * @param recycleRequest
     * @param activeUser
     * @return
     */

    public Map<String, String> startProcess(RecycleInfo recycleRequest, ActiveUser activeUser) {
        ResponseEntity<Map> responseEntity = null;
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = UserInfo.UserInfoBuilder.anUserInfo()
                .withLogId(activeUser.getLogId())
                .withAgency(activeUser.getAgency() == null ? null : activeUser.getAgency())
                .withDealer(activeUser.getDealer() == null ? null : activeUser.getDealer())
                .withName(activeUser.getUsername())
                .withObjId(activeUser.getUserId())
                .withRoles(activeUser.getRoles())
                .withUserType(activeUser.getUserType())
                .build();
        map.put("entity", recycleRequest);
        map.put("userInfo", userInfo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.post("/recycle/startProcess", requestEntity, Map.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过来源单号查询故障件objId集合
     *
     * @param srcDocNo
     * @return
     */

    public List<String> findAllRecycleObjIdsBySrcDocNo(String srcDocNo) {
        Map<String, String> map = new HashMap<>();
        map.put("srcDocNo", srcDocNo);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/recycle/findAllRecycleObjIdsBySrcDocNo", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过vin 查询故障件objId集合
     *
     * @param vin
     * @return
     */

    public List<String> findAllRecycleObjIdsByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/recycle/findAllRecycleObjIdsByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 作废单据
     *
     * @param objId
     */
    public boolean desertTask(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/recycle/desertTask", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
