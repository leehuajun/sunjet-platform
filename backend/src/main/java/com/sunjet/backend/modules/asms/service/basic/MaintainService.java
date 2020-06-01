package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.MaintainEntity;
import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public interface MaintainService {
    MaintainInfo save(MaintainInfo maintainInfo);

    boolean delete(MaintainInfo maintainInfo);

    MaintainInfo findOne(String objId);

    List<MaintainInfo> findAll();

    PageResult<MaintainInfo> getPageList(PageParam<MaintainInfo> pageParam);

    MaintainInfo findOneById(String objId);

    boolean deleteByObjId(String objId);

    List<MaintainEntity> findAllByFilter(MaintainEntity maintainEntity);

    List<MaintainInfoExt> importData(List<MaintainInfoExt> infos);

}
