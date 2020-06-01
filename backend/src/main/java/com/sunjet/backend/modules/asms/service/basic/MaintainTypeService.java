package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface MaintainTypeService {
    MaintainTypeInfo save(MaintainTypeInfo maintainTypeInfo);

    boolean delete(MaintainTypeInfo maintainTypeInfo);

    MaintainTypeInfo findOne(String objId);

    List<MaintainTypeInfo> findAll();

    PageResult<MaintainTypeInfo> getPageList(PageParam<MaintainTypeInfo> pageParam);

//    MaintainTypeInfo findOneById(String objId);

    boolean deleteByObjId(String objId);

    List<MaintainTypeInfo> findModels();

    List<MaintainTypeInfo> findSystems(String parentId);

    List<MaintainTypeInfo> findSubSystems(String parentId);

    List<MaintainTypeInfo> findAllByParentId(String parentId);

//    List<MaintainTypeInfo> findAllByFilter(String keyword);
}
