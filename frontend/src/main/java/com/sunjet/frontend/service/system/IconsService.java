package com.sunjet.frontend.service.system;

import com.sunjet.dto.system.admin.IconInfo;
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
 * 系统图标
 * Created by zyf on 2017/7/21.
 */
@Slf4j
@Service("iconsService")
public class IconsService {

    @Autowired
    private RestClient restClient;


    public List<IconInfo> findAll() {
        try {
            List<IconInfo> infos = restClient.findAll("/icons/findAll", null, new ParameterizedTypeReference<List<IconInfo>>() {
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("IconsServiceImpl:findAll:error" + e.getMessage());
            return null;
        }

    }

    /**
     * 分页
     *
     * @return
     */
    public PageResult<IconInfo> getPageList(PageParam<IconInfo> pageParam) {
        try {
            //
            //HttpEntity<PageParam> requestEntity = new HttpEntity<>(pageParam, null);
            //
            //ResponseEntity<Object> responseEntity = restClient.get(restClient.getPath("/icons/getPageList"),requestEntity,Object.class);
            //
            //log.info("IconsServiceImpl:getPageList:success");
            //
            //LinkedHashMap map = (LinkedHashMap)responseEntity.getBody();
            //
            //PageResult<IconsInfo> pageResult = new PageResult<>(map);
            //
            //return pageResult;
            return restClient.getPageList("/icons/getPageList", pageParam, new ParameterizedTypeReference<PageResult<IconInfo>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.info("IconsServiceImpl:getPageList:error:" + e.getMessage());
            return null;
        }

    }


    public boolean deleteById(String iconId) {

        ResponseEntity<Boolean> responseEntity = null;
        try {
            HttpEntity requestEntity = new HttpEntity<>(iconId, null);
            responseEntity = restClient.delete("/icon/delete", requestEntity, Boolean.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("IconsServiceImpl:delete:error" + e.getMessage());
            return false;
        }
    }
}
