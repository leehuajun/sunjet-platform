package com.sunjet.frontend.service.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.system.admin.ConfigInfo;
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
 * Created by SUNJET_QRY on 2017/7/21.
 * 系统配置信息
 */
@Service("configService")
@Slf4j
public class ConfigService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ConfigService configService;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 新增
     *
     * @param configInfo
     * @return
     */

    public ConfigInfo save(ConfigInfo configInfo) {
        ResponseEntity<ConfigInfo> responseEntity = null;
        try {
            HttpEntity<ConfigInfo> requestEntity = new HttpEntity<>(configInfo, null);
            responseEntity = restClient.post("/config/add", requestEntity, ConfigInfo.class);
            log.info("ConfigServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:save:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 获取所有参数设置列表
     *
     * @return
     */

    public List<ConfigInfo> findAll() {
        try {
            ResponseEntity<String> responseEntity = restClient.get("/config/findAll", String.class);

            List<ConfigInfo> body = (List<ConfigInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<ConfigInfo> configInfoList = new ArrayList<>();
            for (Object o : body) {
                configInfoList.add(JsonHelper.map2Bean(o, ConfigInfo.class));
            }
            log.info("ConfigServiceImpl:findAll:success");
            return configInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 删除
     *
     * @param objId
     * @return
     */

    public boolean delete(String objId) {
        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity<String> request = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/config/delete", request, Boolean.class);
            log.info("ConfigServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }

    /**
     * 通过 objId 获取一个ConfigInfo
     *
     * @param objId
     * @return
     */

    public ConfigInfo findOne(String objId) {

        ResponseEntity<ConfigInfo> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/config/findOne", requestEntity, ConfigInfo.class);
            log.info("ConfigServiceImpl:findOne:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:findOne:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */

    public PageResult<ConfigInfo> getPageList(PageParam<ConfigInfo> pageParam) {
        try {
            return restClient.getPageList("/config/getPageList", pageParam, new ParameterizedTypeReference<PageResult<ConfigInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:getPageList:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取所有配置
     *
     * @return
     */

    public Map<String, ConfigInfo> getAllConfig() {
        Map<String, ConfigInfo> map = null;

        try {
            List<ConfigInfo> all = restClient.findAll("/config/getAllConfig", null, new ParameterizedTypeReference<List<ConfigInfo>>() {
            });
            if (all != null && all.size() > 0) {
                map = new HashMap<>();
                for (ConfigInfo configInfo : all) {
                    map.put(configInfo.getConfigKey(), configInfo);
                }
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:getAllConfig:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 恢复默认值
     *
     * @param ConfigInfo
     */

    public void restore(ConfigInfo ConfigInfo) {

    }

    /**
     * 根据key获取value
     *
     * @param key
     * @return
     */

    public String getValueByKey(String key) {
        ResponseEntity<String> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(key, null);
            responseEntity = restClient.get("/config/getValueByKey", requestEntity, String.class);
            log.info("ConfigServiceImpl:getValueByKey:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ConfigServiceImpl:getValueByKey:error" + e.getMessage());
            return null;
        }
    }


}
