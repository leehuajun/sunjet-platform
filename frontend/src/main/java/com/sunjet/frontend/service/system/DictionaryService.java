package com.sunjet.frontend.service.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.frontend.auth.RestClient;
import com.sunjet.utils.common.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/24.
 * 字典数据
 */
@Slf4j
@Service("dictionaryService")
public class DictionaryService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取列表集合
     *
     * @return
     */

    public List<DictionaryInfo> findAll() {
        try {
            ResponseEntity<String> responseEntity = restClient.get("/dictionary/findAll", String.class);
            List<DictionaryInfo> boby = null;
            boby = (List<DictionaryInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<DictionaryInfo> dictionaryInfoList = new ArrayList<>();
            for (Object o : boby) {
                JSONObject object = JSONObject.fromObject(o);
                DictionaryInfo info = JsonHelper.json2Bean(object.toString(), DictionaryInfo.class);
                dictionaryInfoList.add(info);
            }
            log.info("DictionaryServiceImpl:findAll:success");
            return dictionaryInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:findAll:error" + e.getMessage());
            return null;
        }
    }


    /**
     * 新增
     *
     * @param dictionaryInfo
     * @return
     */

    public DictionaryInfo save(DictionaryInfo dictionaryInfo) {
        ResponseEntity<DictionaryInfo> responseEntity = null;
        try {
            HttpEntity<DictionaryInfo> requestEntity = new HttpEntity<>(dictionaryInfo, null);
            responseEntity = restClient.post("/dictionary/save", requestEntity, DictionaryInfo.class);
            log.info("DictionaryServiceImpl:save:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:save:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 查找
     *
     * @param objId
     * @return
     */

    public DictionaryInfo findOne(String objId) {
        ResponseEntity<DictionaryInfo> responseEntity = null;
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.get("/dictionary/findOne", requestEntity, DictionaryInfo.class);
            log.info("DictionaryServiceImpl:findOne:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:findOne:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过10010查星级
     *
     * @param code
     * @return
     */

    public List<DictionaryInfo> findDictionariesByParentCode(String code) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(code, null);
//            ResponseEntity<String> responseEntity = restClient.get("/dictionary/findDictionariesByParentCode", requestEntity, String.class);

//            return restClient.getPageList("/part/getPageList", pageParam,new ParameterizedTypeReference<PageResult<PartInfo>>(){});
            List<DictionaryInfo> list = restClient.findAll("/dictionary/findDictionariesByParentCode"
                    , requestEntity
                    , new ParameterizedTypeReference<List<DictionaryInfo>>() {
                    });
//            List<DictionaryInfo> boby = null;
//            boby = (List<DictionaryInfo>) mapper.readValue(responseEntity.getBody(), List.class);
//            List<DictionaryInfo> dictionaryInfoList = new ArrayList<>();
//            for (Object o : boby) {
//                JSONObject object = JSONObject.fromObject(o);
//                DictionaryInfo info = JsonHelper.json2Bean(object.toString(), DictionaryInfo.class);
//                dictionaryInfoList.add(info);
//            }
            log.info("DictionaryServiceImpl:findDictionariesByParentCode:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:findDictionariesByParentCode:error" + e.getMessage());
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
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            responseEntity = restClient.delete("/dictionary/delete", requestEntity, Boolean.class);
            log.info("DictionaryServiceImpl:delete:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:delete:error:" + e.getMessage());
            return false;
        }
    }

    /**
     * 查找所以父级数据
     *
     * @return
     */

    public List<DictionaryInfo> findAllParent() {
        try {
            ResponseEntity<String> entity = restClient.get("/dictionary/findAllParent", String.class);
            List<DictionaryInfo> boby = null;
            boby = (List<DictionaryInfo>) mapper.readValue(entity.getBody(), List.class);
            List<DictionaryInfo> dictionaryInfoList = new ArrayList<>();
            for (Object o : boby) {
                JSONObject object = JSONObject.fromObject(o);
                DictionaryInfo info = JsonHelper.json2Bean(object.toString(), DictionaryInfo.class);
                dictionaryInfoList.add(info);
            }
            log.info("DictionaryServiceImpl:findAllParent:success");
            return dictionaryInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:findAllParent:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 根据编码code查找一条基础数据
     *
     * @param code
     * @return
     */
    public DictionaryInfo findDictionaryByCode(String code) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(code, null);
            ResponseEntity<DictionaryInfo> responseEntity = restClient.get("/dictionary/findDictionaryByCode", requestEntity, DictionaryInfo.class);
            log.info("DictionaryServiceImpl:findDictionaryByCode:success");
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("DictionaryServiceImpl:findDictionaryByCode:error" + e.getMessage());
            return null;
        }
    }
}
