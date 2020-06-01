package com.sunjet.frontend.service.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.frontend.auth.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/24.
 */
@Slf4j
@Service("menuService")
public class MenuService {


    @Autowired
    private RestClient restClient;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 查询所有菜单
     *
     * @return
     */

    public List<MenuInfo> findAll() {

        try {
            HttpEntity requestEntity = new HttpEntity<>(null);

            List<MenuInfo> menuInfoList = restClient.findAll("/menu/menusAll", requestEntity, new ParameterizedTypeReference<List<MenuInfo>>() {
            });
            log.info("MenuServicelmpl:findAll:success");
            return menuInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("ResourceServiceImpl:save:error:" + e.getMessage());
            return null;
        }

    }

    /**
     * 根据ID查询info
     *
     * @param objId
     * @return
     */

    public MenuInfo findOneById(String objId) {
        HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
        ResponseEntity<MenuInfo> menuInfoResponseEntity = restClient.get("/menu/findOneMenuById", requestEntity, MenuInfo.class);
        MenuInfo menuInfo = menuInfoResponseEntity.getBody();
        return menuInfo;
    }

    /**
     * 保存实体
     *
     * @param menuInfo
     * @return
     */

    public MenuInfo save(MenuInfo menuInfo) {

        try {
            HttpEntity<MenuInfo> requestEntity = null;
            requestEntity = new HttpEntity<>(menuInfo, null);
            ResponseEntity<MenuInfo> entity = restClient.post("/menu/saveMenu", requestEntity, MenuInfo.class);
            MenuInfo body = entity.getBody();
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean delete(String objId) {
        HttpEntity<String> requestEntity = new HttpEntity<>(objId, null);
        ResponseEntity<Boolean> delete = restClient.delete("/menu/delete", requestEntity, Boolean.class);


        return false;
    }

    /**
     * 通过url 查找菜单
     *
     * @param url
     * @return
     */

    public MenuInfo findMenuByUrl(String url) {
        HttpEntity<String> requestEntity = new HttpEntity<>(url, null);
        ResponseEntity<MenuInfo> menuInfoResponseEntity = restClient.get("/menu/findMenuByUrl", requestEntity, MenuInfo.class);
        MenuInfo menuInfo = menuInfoResponseEntity.getBody();
        return menuInfo;
    }

}
