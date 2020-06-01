package com.sunjet.frontend.service.system;

import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 公告
 */
@Slf4j
@Service("noticeService")
public class NoticeService {

    @Autowired
    private RestClient restClient;

    /**
     * 获取所有公告
     *
     * @return
     */

    public List<NoticeInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<NoticeInfo> list = restClient.findAll("/notice/findAll", requestEntity, new ParameterizedTypeReference<List<NoticeInfo>>() {
            });
            log.info("NoticeServicelmpl:findAll:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("NoticeServicelmpl:findAll:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<NoticeInfo> getPageList(PageParam<NoticeInfo> pageParam) {
        try {
            return restClient.getPageList("/notice/getPageList", pageParam, new ParameterizedTypeReference<PageResult<NoticeInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("NoticeServicelmpl:getPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 通过一个objId 查找一个实体
     *
     * @param objId
     * @return
     */

    public NoticeInfo findOneById(String objId) {
        ResponseEntity<NoticeInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/notice/findOneById", requestEntity, NoticeInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("NoticeServicelmpl:findOneById:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存一个vo实体
     *
     * @param noticeInfo
     * @return
     */

    public NoticeInfo save(NoticeInfo noticeInfo) {
        ResponseEntity<NoticeInfo> responseEntity = null;
        try {
            HttpEntity<NoticeInfo> requestEntity = new HttpEntity<>(noticeInfo, null);
            responseEntity = restClient.post("/notice/save", requestEntity, NoticeInfo.class);
            log.info("NoticeServicelmpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("NoticeServicelmpl:save:error" + e.getMessage());
            return null;
        }
    }

    public List<NoticeInfo> findLastNotices(int days) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(days, null);
            List<NoticeInfo> list = restClient.findAll("/notice/findLastNotices", requestEntity, new ParameterizedTypeReference<List<NoticeInfo>>() {
            });
            log.info("NoticeServicelmpl:findAll:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("NoticeServicelmpl:findAll:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除公告
     *
     * @param objId
     */
    public Boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/notice/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
