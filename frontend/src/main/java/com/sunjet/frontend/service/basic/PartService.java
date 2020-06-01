package com.sunjet.frontend.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.basic.PartInfoExt;
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
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/27.
 */
@Service("partService")
@Slf4j
public class PartService {

    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    public PageResult<PartInfo> getPageList(PageParam<PartInfo> pageParam) {
        try {
            log.info("PartService:getPageList:success");
            return restClient.getPageList("/part/getPageList", pageParam, new ParameterizedTypeReference<PageResult<PartInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:getPageList:error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 查询一个实体
     *
     * @param objId
     * @return
     */
    public PartInfo findOne(String objId) {

        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
            ResponseEntity<PartInfo> ResponseEntity = restClient.get("/part/findOne", requestEntity, PartInfo.class);
            PartInfo PartInfo = ResponseEntity.getBody();
            log.info("PartService:findOne:success");
            return PartInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:findOne:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 保存实体
     *
     * @param partInfo
     * @return
     */
    public PartInfo save(PartInfo partInfo) {
        try {
            HttpEntity<PartInfo> requestEntity = new HttpEntity<>(partInfo, null);
            ResponseEntity<PartInfo> ResponseEntity = restClient.get("/part/save", requestEntity, PartInfo.class);
            PartInfo PartInfo = ResponseEntity.getBody();
            log.info("PartService:save:success");
            return PartInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:save:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 配件搜索
     *
     * @param keyword
     * @return
     */
    public List<PartInfo> findAllByKeyword(String keyword) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(keyword, null);
            ResponseEntity<String> responseEntity = restClient.post("/part/findAllByKeyword", requestEntity, String.class);

            List<PartInfo> body = (List<PartInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<PartInfo> partInfoList = new ArrayList<>();
            for (Object o : body) {
                PartInfo partInfo = JsonHelper.map2Bean(o, PartInfo.class);
                partInfoList.add(partInfo);
            }
            log.info("PartService:findAllByKeyword:success");
            return partInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:findAllByKeyword:error" + e.getMessage());
            return null;
        }
    }

    /**
     * 通过objId 查找一个速报配件实体
     *
     * @param objId
     * @return
     */
    public List<PartInfo> findByPartId(List<String> objId) {
        try {
            HttpEntity<List> requestEntity = new HttpEntity<>(objId, null);

            ResponseEntity<String> responseEntity = restClient.get("/part/findByPartId", requestEntity, String.class);

            List<PartInfo> body = (List<PartInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<PartInfo> partInfoList = new ArrayList<>();
            for (Object o : body) {
                PartInfo partInfo = JsonHelper.map2Bean(o, PartInfo.class);
                partInfoList.add(partInfo);
            }
            log.info("PartService:findByPartId:success");
            return partInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:findByPartId:error" + e.getMessage());
            return null;
        }
    }


    public List<PartInfoExt> importParts(List<PartInfoExt> infos) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(infos, null);
            List<PartInfoExt> list = restClient.findAll("/part/importParts", requestEntity, new ParameterizedTypeReference<List<PartInfoExt>>() {
            });
            log.info("PartService:importParts:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:importParts:error" + e.getMessage());
            return null;
        }
    }

    public List<PartInfoExt> modifyParts(List<PartInfoExt> infos) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(infos, null);
            List<PartInfoExt> list = restClient.findAll("/part/modifyParts", requestEntity, new ParameterizedTypeReference<List<PartInfoExt>>() {
            });
            log.info("PartService:modifyParts:success");
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:modifyParts:error" + e.getMessage());
            return null;
        }
    }

    public PartInfo findOneByCode(String code) {
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(code, null);
            ResponseEntity<PartInfo> ResponseEntity = restClient.get("/part/findOneByCode", requestEntity, PartInfo.class);
            PartInfo PartInfo = ResponseEntity.getBody();
            return PartInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 查找配件信息所以配件包括已经禁用的
     *
     * @param partCode
     * @return
     */
    public List<PartInfo> findAllByCode(String partCode) {
        try {
            HttpEntity requestEntity = new HttpEntity<>(partCode, null);
            ResponseEntity<String> responseEntity = restClient.post("/part/findAllByCode", requestEntity, String.class);

            List<PartInfo> body = (List<PartInfo>) mapper.readValue(responseEntity.getBody(), List.class);
            List<PartInfo> partInfoList = new ArrayList<>();
            for (Object o : body) {
                PartInfo partInfo = JsonHelper.map2Bean(o, PartInfo.class);
                partInfoList.add(partInfo);
            }
            log.info("PartService:findAllByKeyword:success");
            return partInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("PartService:findAllByKeyword:error" + e.getMessage());
            return null;
        }
    }
}
