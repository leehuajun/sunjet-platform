package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.AgencyItem;
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
 * Created by SUNJET_WS on 2017/7/27.
 * 合作商
 */
@Service("agencyService")
@Slf4j
public class AgencyService {

    @Autowired
    private RestClient restClient;


    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    public PageResult<AgencyItem> getPageList(PageParam<AgencyItem> pageParam) {
        try {
            log.info("AgencyService:getPageList:success");
            return restClient.getPageList("/agency/getPageList", pageParam, new ParameterizedTypeReference<PageResult<AgencyItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:getPageList:error:" + e.getMessage());
            return null;
        }
    }


    /**
     * 查询一个实体
     *
     * @param objId
     * @return
     */
    public AgencyInfo findOne(String objId) {

        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<AgencyInfo> ResponseEntity = restClient.get("/agency/findOne", requestEntity, AgencyInfo.class);
            AgencyInfo agencyInfo = ResponseEntity.getBody();
            log.info("AgencyService:findOne:success");
            return agencyInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:findOne:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 保存实体
     *
     * @param agencyInfo
     * @return
     */
    public AgencyInfo save(AgencyInfo agencyInfo) {
        try {
            HttpEntity<AgencyInfo> requestEntity = new HttpEntity<>(agencyInfo, null);
            ResponseEntity<AgencyInfo> ResponseEntity = restClient.get("/agency/save", requestEntity, AgencyInfo.class);
            log.info("AgencyService:save:success");
            return ResponseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:save:error" + e.getMessage());
            return null;
        }
    }

    public List<AgencyInfo> findAllByKeyword(String keywordAgency) {
        try {
            AgencyInfo info = new AgencyInfo();
            info.setName(keywordAgency);
            info.setCode(keywordAgency);
            HttpEntity<AgencyInfo> requestEntity = new HttpEntity<>(info, null);
            List<AgencyInfo> list = restClient.findAll("/agency/findAllByKeyword", requestEntity, new ParameterizedTypeReference<List<AgencyInfo>>() {
            });
            log.info("AgencyService:findAllByKeyword:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:findAllByKeyword:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过供货商编号查找
     *
     * @param code
     * @return
     */
    public AgencyInfo findOneByCode(String code) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(code, null);
            ResponseEntity<AgencyInfo> ResponseEntity = restClient.get("/agency/findOneByCode", requestEntity, AgencyInfo.class);
            AgencyInfo agencyInfo = ResponseEntity.getBody();
            log.info("AgencyService:findOneByCode:success");
            return agencyInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:findOneByCode:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 检查code是否存在
     *
     * @param code
     * @return
     */
    public Boolean checkCodeExists(String code) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(code, null);
            ResponseEntity<Boolean> responseEntity = restClient.get("/agency/checkCodeExists", requestEntity, Boolean.class);
            log.info("AgencyService:checkCodeExists:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:checkCodeExists:error" + e.getMessage());
            return null;
        }
    }

    public List<AgencyInfo> findAll() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<AgencyInfo> all = restClient.findAll("/agency/findAll", requestEntity, new ParameterizedTypeReference<List<AgencyInfo>>() {
            });
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:findAll:error" + e.getMessage());
            return null;
        }
    }

    public List<AgencyInfo> findEnabled() {
        try {
            HttpEntity requestEntity = new HttpEntity<>(null);
            List<AgencyInfo> all = restClient.findAll("/agency/findEnabled", requestEntity, new ParameterizedTypeReference<List<AgencyInfo>>() {
            });
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AgencyService:findEnabled:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 通过id获取一个合作商覆盖的省份
     *
     * @param objId
     * @return
     */
    public AgencyInfo findOneProvincesById(String objId) {
        ResponseEntity<AgencyInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/agency/findOneProvincesById", requestEntity, AgencyInfo.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:findOneProvincesById:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过省份id查找合作商
     *
     * @param provincesId
     * @return
     */
    public List<AgencyInfo> findOneAgencnyById(String provincesId) {
        ResponseEntity<AgencyInfo> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(provincesId, null);
            List<AgencyInfo> all = restClient.findAll("/agency/findOneAgencnyById", requestEntity, new ParameterizedTypeReference<List<AgencyInfo>>() {
            });
            return all;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("RoleServiceImpl:findOneAgencnyById:error" + e.getMessage());
            return null;
        }
    }

}
