package com.sunjet.backend.modules.asms.service.basic;


import com.sunjet.backend.modules.asms.entity.basic.view.AgencyView;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.AgencyItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by lhj on 16/9/17.
 * 合作商基础信息
 */
public interface AgencyService {

    AgencyInfo save(AgencyInfo agencyInfo);

    boolean delete(AgencyInfo agencyInfo);

    boolean delete(String objId);

    AgencyInfo findOne(String objId);

    PageResult<AgencyView> getPageList(PageParam<AgencyItem> pageParam);

    /**
     * @param keywordAgency
     * @return
     */
    @PostMapping
    public List<AgencyInfo> findAllByKeyword(String keywordAgency);


    AgencyInfo findOneByCode(String code);


    Boolean checkCodeExists(String code);


    List<AgencyInfo> findAll();

    List<AgencyInfo> findEnabled();

    boolean addAgencyWithProvinces(AgencyInfo agencyInfo);

    boolean deleteAgencyWithProvinces(String angencyId);


    List<AgencyInfo> findAllAgencyByProvinceId(String provinceId);


}
