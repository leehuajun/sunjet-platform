package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.basic.PartInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public interface PartService {

    PartInfo save(PartInfo partInfo);

    boolean delete(String objId);

    PartInfo findOne(String objId);

    PageResult<PartInfo> getPageList(PageParam<PartInfo> pageParam);

    List<PartInfo> findAll();

    List<PartInfo> findAllByKeyword(String keyword);

    List<PartInfo> findByPartId(ArrayList<String> objIds);

    List<PartInfoExt> importParts(List<PartInfoExt> infoExts);

    List<PartInfoExt> modifyParts(List<PartInfoExt> infoExts);

    PartEntity findOneByCode(String code);

    List<PartEntity> findAllByCode(String code);
}
