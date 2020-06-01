package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.DealerItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.ActiveUser;
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
 * 服务站信息列表
 * Created by zyf on 2017/7/27.
 */
@Slf4j
@Service("dealerService")
public class DealerService {

    @Autowired
    private RestClient restClient;

    /**
     * 获取列表集合
     *
     * @return
     */
    public List<DealerInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<DealerInfo> all = restClient.findAll("/dealer/findAll", requestEntity, new ParameterizedTypeReference<List<DealerInfo>>() {
            });
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<DealerItem> getPageList(PageParam<DealerItem> pageParam) {
        try {
            return restClient.getPageList("/dealer/getPageList", pageParam, new ParameterizedTypeReference<PageResult<DealerItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:getPageList:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 通过objId查找
     *
     * @param objId
     * @return
     */
    public DealerInfo findOneById(String objId) {
        ResponseEntity<DealerInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/dealer/findOneById", requestEntity, DealerInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:findOneById:error" + e.getMessage());
            return null;
        }
    }

    public List<DealerInfo> findAllByKeyword(String keywordDealer) {

        ResponseEntity<List> responseEntity = null;
        try {
            DealerInfo info = new DealerInfo();
            info.setName(keywordDealer);
            info.setCode(keywordDealer);
            HttpEntity requestEntity = new HttpEntity<>(info, null);
            responseEntity = restClient.get("/dealer/findAllByKeyword", requestEntity, List.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:findAllByKeyword:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 查出通过关键字搜索服务站父节点
     *
     * @return
     */
    public List<DealerInfo> findAllDealerParent(String keyword) {
        ResponseEntity<List> responseEntity = null;
        try {
            DealerInfo info = new DealerInfo();
            info.setName(keyword);
            info.setCode(keyword);
            HttpEntity requestEntity = new HttpEntity<>(info, null);
            return restClient.findAll("/dealer/findAllParentDealers", requestEntity, new ParameterizedTypeReference<List<DealerInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据当前用户查询服务站
     *
     * @param keyword
     * @param activeUser
     * @return
     */
    public List<DealerInfo> searchDealers(String keyword, ActiveUser activeUser) {
        ResponseEntity<List> responseEntity = null;
        try {
            DealerInfo info = new DealerInfo();
            info.setName(keyword);
            info.setCode(keyword);
            Map<String, Object> map = new HashMap<>();
            map.put("info", info);

            UserInfo userInfo = UserInfo.UserInfoBuilder.anUserInfo()
                    .withLogId(activeUser.getLogId())
                    .withAgency(activeUser.getAgency() == null ? null : activeUser.getAgency())
                    .withDealer(activeUser.getDealer() == null ? null : activeUser.getDealer())
                    .withName(activeUser.getUsername())
                    .withObjId(activeUser.getUserId())
                    .withRoles(activeUser.getRoles())
                    .withUserType(activeUser.getUserType())
                    .build();

            map.put("userInfo", userInfo);
            HttpEntity requestEntity = new HttpEntity<>(map, null);
            responseEntity = restClient.get("/dealer/searchDealers", requestEntity, List.class);
            List body = responseEntity.getBody();
            List<DealerInfo> dealerInfos = new ArrayList<>();
            for (Object o : body) {
                dealerInfos.add(JsonHelper.map2Bean(o, DealerInfo.class));
            }

            return dealerInfos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:findAll:error" + e.getMessage());
            return null;
        }

    }

    /**
     * 根据服务编号搜服务站信息
     *
     * @param dealerCode
     * @return
     */
    public DealerInfo findOneByCode(String dealerCode) {
        ResponseEntity<DealerInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(dealerCode, null);
            responseEntity = restClient.get("/dealer/findOneByCode", requestEntity, DealerInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:findOneByCode:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过服务经理id获取服务站
     *
     * @param objId
     * @return
     */
    public List<DealerInfo> findAllByServiceManagerId(String objId) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            return restClient.findAll("/dealer/findAllByServiceManagerId", requestEntity, new ParameterizedTypeReference<List<DealerInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DealerInfo> findAllNotServiceManager() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            return restClient.findAll("/dealer/findAllNotServiceManager", requestEntity, new ParameterizedTypeReference<List<DealerInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objId删除
     *
     * @param objId
     */
    public boolean deleteByObjId(String objId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/dealer/deleteByObjId", requestEntity, Boolean.class);
            log.info("DealerService:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:delete:error" + e.getMessage());
            return false;
        }
    }


    /**
     * 新增
     *
     * @param dealerInfo
     * @return
     */
    public DealerInfo save(DealerInfo dealerInfo) {
        ResponseEntity<DealerInfo> responseEntity = null;
        try {
            HttpEntity<DealerInfo> requestEntity = new HttpEntity<>(dealerInfo, null);
            responseEntity = restClient.post("/dealer/save", requestEntity, DealerInfo.class);
            log.info("DealerService:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:save:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过对象删除
     *
     * @param dealerInfo
     * @return
     */
    public boolean delete(DealerInfo dealerInfo) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<DealerInfo> requestEntity = new HttpEntity<>(dealerInfo, null);
            responseEntity = restClient.delete("/dealer/delete", requestEntity, Boolean.class);
            log.info("DealerService:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:delete:error:" + e.getMessage());
            return false;
        }
    }

    public boolean checkCodeExists(String code) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(code, null);
            responseEntity = restClient.delete("/dealer/checkCodeExists", requestEntity, Boolean.class);
            log.info("DealerService:checkCodeExists:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DealerService:checkCodeExists:error:" + e.getMessage());
            return false;
        }
    }
}
