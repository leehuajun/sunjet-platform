package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.PartSearchInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.auth.PartCatalogueRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wushi
 * @description: 配件查询
 * @Date: Created in 17:09 2018/4/8
 * @Modify by: wushi
 * @ModifyDate by: 17:09 2018/4/8
 */
@Service("partSearchService")
@Slf4j
public class PartSearchService {


    @Autowired
    private PartCatalogueRestClient partCatalogueRestClient;

    /**
     * 查询集合
     *
     * @param pageParam
     * @return
     */
    public PageResult<PartSearchInfo> getPageList(PageParam<PartSearchInfo> pageParam) {
        try {
            return partCatalogueRestClient.getPageList("/partManage/getPartCatalogue", pageParam, new ParameterizedTypeReference<PageResult<PartSearchInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<PartSearchInfo> findAll(PartSearchInfo partSearchInfo) {

        List<PartSearchInfo> infos = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(partSearchInfo, null);
            infos = partCatalogueRestClient.findAll("/partManage/getPartSearch", requestEntity, new ParameterizedTypeReference<List<PartSearchInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
