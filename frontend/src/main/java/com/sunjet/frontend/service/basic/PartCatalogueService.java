package com.sunjet.frontend.service.basic;

import com.sunjet.dto.asms.basic.PartCatalogueInfo;
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
 * @description: 配件目录
 * @Date: Created in 14:20 2018/4/2
 * @Modify by: wushi
 * @ModifyDate by: 14:20 2018/4/2
 */
@Service("partCatalogueService")
@Slf4j
public class PartCatalogueService {

    @Autowired
    private PartCatalogueRestClient partCatalogueRestClient;

    /**
     * 查询集合
     *
     * @param pageParam
     * @return
     */
    public PageResult<PartCatalogueInfo> getPageList(PageParam<PartCatalogueInfo> pageParam) {
        try {
            return partCatalogueRestClient.getPageList("/partManage/getPartCatalogue", pageParam, new ParameterizedTypeReference<PageResult<PartCatalogueInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<PartCatalogueInfo> findAll(PartCatalogueInfo partCatalogueInfo) {

        List<PartCatalogueInfo> infos = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(partCatalogueInfo, null);
            infos = partCatalogueRestClient.findAll("/partManage/getPartCatalogue", requestEntity, new ParameterizedTypeReference<List<PartCatalogueInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
