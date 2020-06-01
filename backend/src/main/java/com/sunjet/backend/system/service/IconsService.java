package com.sunjet.backend.system.service;


import com.sunjet.backend.system.entity.IconEntity;
import com.sunjet.dto.system.admin.IconInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 系统图标
 */
public interface IconsService {


    boolean delete(String objId);

    List<IconEntity> findAll();

    PageResult<IconEntity> getPageList(PageParam<IconEntity> pageParam);
}
