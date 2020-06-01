package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.GoOutEntity;
import com.sunjet.dto.asms.asm.GoOutInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface GoOutService {

    GoOutInfo save(GoOutInfo coOutInfo);

    boolean delete(GoOutInfo coOutInfo);

    boolean delete(String objId);

    GoOutInfo findOne(String objId);

    /**
     * 通过三包单查找外出活动列表
     *
     * @param objId
     * @return
     */
    List<GoOutInfo> findAllByWarrantyMaintenanceObjId(String objId);

    /**
     * 通过首保服务单查找外出活动列表
     *
     * @param objId
     * @return
     */
    List<GoOutInfo> findAllByFirstMaintenanceId(String objId);

    boolean deleteByActivityMaintenanceId(String objId);

    List<GoOutInfo> findAllByActivityMaintenanceObjId(String objId);

    List<GoOutEntity> saveList(List<GoOutEntity> goOutEntityList);
}
