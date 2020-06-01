package com.sunjet.frontend.service.recycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/8/11.
 * 故障件返回通知单子行列表
 */
@Slf4j
@Service("recycleNoticeItemService")
public class RecycleNoticeItemService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 通过通知单objid  获取返回通知单需求列表
     *
     * @param objId
     * @return
     */

    public List<RecycleNoticeItemInfo> findByNoticeId(String objId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/recycleNoticeItem/findByNoticeId", requestEntity, String.class);

            List<RecycleNoticeItemInfo> body = (List<RecycleNoticeItemInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<RecycleNoticeItemInfo> recycleNoticeItemInfos = new ArrayList<>();
            for (Object o : body) {
                RecycleNoticeItemInfo recycleNoticeItemInfo = JsonHelper.map2Bean(o, RecycleNoticeItemInfo.class);
                recycleNoticeItemInfos.add(recycleNoticeItemInfo);
            }
            log.info("RecycleNoticeItemServiceImpl:findByNoticeId:success");
            return recycleNoticeItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeItemServiceImpl:findByNoticeId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存一个实体
     *
     * @param recycleNoticeItemInfo
     * @return
     */

    public RecycleNoticeItemInfo save(RecycleNoticeItemInfo recycleNoticeItemInfo) {
        try {
            HttpEntity<RecycleNoticeItemInfo> httpEntity = new HttpEntity<>(recycleNoticeItemInfo, null);
            ResponseEntity<RecycleNoticeItemInfo> responseEntity = restClient.post("/recycleNoticeItem/save", httpEntity, RecycleNoticeItemInfo.class);
            log.info("RecycleNoticeItemServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeItemServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 待返回清单 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<RecycleNoticePendingItem> getPageList(PageParam<RecycleNoticePendingItem> pageParam) {
        try {
            log.info("RecycleNoticeItemServiceImpl:getPageList:success");
            return restClient.getPageList("/recycleNoticeItem/getPageList", pageParam, new ParameterizedTypeReference<PageResult<RecycleNoticePendingItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeItemServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据配件编号或配件名称搜索配件需求列表
     *
     * @param key
     * @param dealerCode
     * @return
     */

    public List<RecycleNoticePendingInfo> findByReturnOrParts(String key, String dealerCode) {
        try {
            //RecycleNoticeItemInfo recycleNoticeItemInfo = new RecycleNoticeItemInfo();
            RecycleNoticePendingItem recycleNoticePendingItem = new RecycleNoticePendingItem();
            recycleNoticePendingItem.setPartCode(key);
            recycleNoticePendingItem.setPartName(key);
            recycleNoticePendingItem.setDealerCode(dealerCode);
            Map<String, RecycleNoticePendingItem> map = new HashMap<>();
            map.put("recycleNoticePendingItem", recycleNoticePendingItem);

            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<RecycleNoticePendingInfo> list = restClient.findAll("/recycleNoticeItem/findByReturnOrParts", requestEntity, new ParameterizedTypeReference<List<RecycleNoticePendingInfo>>() {
            });
            log.info("RecycleNoticeItemServiceImpl:findByReturnOrParts:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeItemServiceImpl:findByReturnOrParts:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */

    public Boolean delete(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<Boolean> responseEntity = restClient.delete("/recycleNoticeItem/delete", httpEntity, Boolean.class);
            log.info("RecycleNoticeItemServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeItemServiceImpl:delete:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objid查询实体
     *
     * @param objId
     * @return
     */

    public RecycleNoticeItemInfo findOneByObjid(String objId) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(objId, null);
            ResponseEntity<RecycleNoticeItemInfo> responseEntity = restClient.post("/recycleNoticeItem/findOneByObjid", httpEntity, RecycleNoticeItemInfo.class);
            log.info("RecycleNoticeItemServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleNoticeItemServiceImpl:delete:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过vin 查询故障返回通知明细objid集合
     *
     * @param vin
     * @return
     */

    public List<String> findAllRecycleItemsObjIdByVin(String vin) {
        Map<String, String> map = new HashMap<>();
        map.put("vin", vin);
        try {
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            List<String> list = restClient.findAll("/recycleNoticeItem/findAllRecycleItemsObjIdByVin", requestEntity, new ParameterizedTypeReference<List<String>>() {
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    ///**
    // * 根据配件编号或名字关键字搜索故障件配件信息
    // * @param key
    // * @return
    // */
    //
    //public List<RecycleNoticeItemInfo> findCanReturnParts(String key) {
    //
    //    try {
    //        HttpEntity requestEntity = new HttpEntity<>(key, null);
    //        List<RecycleNoticeItemInfo> list = restClient.findAll("/recycleNoticeItem/findCanReturnParts", requestEntity, new ParameterizedTypeReference<List<RecycleNoticeItemInfo>>() {});
    //        log.info("RecycleNoticeItemServiceImpl:findCanReturnParts:success");
    //        return list;
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        log.info("RecycleNoticeItemServiceImpl:findCanReturnParts:error" + e.getMessage());
    //        return null;
    //    }
    //}

}
