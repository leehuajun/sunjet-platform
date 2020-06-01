package com.sunjet.backend.system.service;


import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 配置参数
 */
public interface ConfigService {

    List<ConfigInfo> findAll();

    //void update(ConfigInfo configInfo);

    void restore(ConfigInfo configInfo);

    String getValueByKey(String key);


    ConfigInfo save(ConfigInfo configInfo);

    boolean delete(ConfigInfo configInfo);

    boolean delete(String objId);

    ConfigInfo findOne(String objId);

    PageResult<ConfigInfo> getPageList(PageParam<ConfigInfo> pageParam);

    List<ConfigInfo> getAllConfig();

}
