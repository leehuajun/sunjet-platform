package com.sunjet.frontend.service.recycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.recycle.RecycleItemInfo;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/8/11.
 * 故障件返回通知单子行列表
 */
@Slf4j
@Service("recycleItemService")
public class RecycleItemService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 通过故障件返回单id查询返回单子行列表
     *
     * @param objId
     * @return
     */

    public List<RecycleItemInfo> findByRecycle(String objId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/recycleItem/findByRecycle", requestEntity, String.class);

            List<RecycleItemInfo> body = (List<RecycleItemInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<RecycleItemInfo> recycleItemInfos = new ArrayList<>();
            for (Object o : body) {
                RecycleItemInfo recycleItemInfo = JsonHelper.map2Bean(o, RecycleItemInfo.class);
                recycleItemInfos.add(recycleItemInfo);
            }
            log.info("RecycleItemServiceImpl:findByRecycle:success");
            return recycleItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleItemServiceImpl:findByRecycle:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过子行通知单id  获取通知单子行需求列表
     *
     * @param objId
     * @return
     */

    public List<RecycleItemInfo> findAllByNoticeItemId(String objId) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/recycleItem/findAllByNoticeItemId", requestEntity, String.class);

            List<RecycleItemInfo> body = (List<RecycleItemInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<RecycleItemInfo> recycleItemInfos = new ArrayList<>();
            for (Object o : body) {
                RecycleItemInfo recycleItemInfo = JsonHelper.map2Bean(o, RecycleItemInfo.class);
                recycleItemInfos.add(recycleItemInfo);
            }
            log.info("RecycleItemServiceImpl:findAllByNoticeItemId:success");
            return recycleItemInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleItemServiceImpl:findAllByNoticeItemId:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存一个实体
     *
     * @param recycleItemInfo
     * @return
     */

    public RecycleItemInfo save(RecycleItemInfo recycleItemInfo) {
        try {
            HttpEntity<RecycleItemInfo> httpEntity = new HttpEntity<>(recycleItemInfo, null);
            ResponseEntity<RecycleItemInfo> responseEntity = restClient.post("/recycleItem/save", httpEntity, RecycleItemInfo.class);
            log.info("RecycleItemServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleItemServiceImpl:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过 recycle 查找故障件返回单配件需求列表
     *
     * @param recycle
     * @return
     */

    public List<RecycleItemInfo> findByRecyclePartList(String recycle) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(recycle, null);
            List<RecycleItemInfo> list = restClient.findAll("/recycleItem/findByRecyclePartList", requestEntity, new ParameterizedTypeReference<List<RecycleItemInfo>>() {
            });
            log.info("RecycleItemServiceImpl:findByRecyclePartList:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleItemServiceImpl:findByRecyclePartList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 删除故障件明细子行
     *
     * @param objId
     * @return
     */

    public Boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/recycleItem/delete", requestEntity, Boolean.class);

            log.info("RecycleItemServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RecycleItemServiceImpl:delete:error:" + e.getMessage());
            return null;
        }


    }

    public RecycleItemInfo findOne(String objId) {
        ResponseEntity<RecycleItemInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/recycleItem/findOneById", requestEntity, RecycleItemInfo.class);

            RecycleItemInfo Info = responseEntity.getBody();
            return Info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
