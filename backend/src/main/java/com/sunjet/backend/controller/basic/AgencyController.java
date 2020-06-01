package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.entity.basic.AgencyBackupEntity;
import com.sunjet.backend.modules.asms.entity.basic.view.AgencyView;
import com.sunjet.backend.modules.asms.service.basic.AgencyBackupService;
import com.sunjet.backend.modules.asms.service.basic.AgencyService;
import com.sunjet.backend.modules.asms.service.basic.RegionService;
import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.AgencyItem;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_WS on 2017/7/27.
 * 合作商
 */
@Slf4j
@RestController
@RequestMapping("/agency")
public class AgencyController {

    @Autowired
    AgencyService agencyService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private AgencyBackupService agencyBackupService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @ApiOperation(value = "合作商分页查询")
    @PostMapping("/getPageList")
    public PageResult<AgencyView> getPageList(@RequestBody PageParam<AgencyItem> pageParam) {
        return agencyService.getPageList(pageParam);
    }


    /**
     * 通过objid查找一个实体
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "合作商查找一个实体")
    @PostMapping("/findOne")
    public AgencyInfo findOne(@RequestBody String objId) {
        return agencyService.findOne(objId);
    }

    /**
     * 保存一个实体
     *
     * @param agencyInfo
     * @return
     */
    @ApiOperation(value = "合作商保存一个实体")
    @PostMapping("/save")
    public AgencyInfo save(@RequestBody AgencyInfo agencyInfo) {
        return agencyService.save(agencyInfo);
    }

    /**
     * @param info
     * @return
     */
    @ApiOperation(value = "通过名称查找一个合作商列表")
    @PostMapping("/findAllByKeyword")
    public List<AgencyInfo> findAllByKeyword(@RequestBody AgencyInfo info) {
        return agencyService.findAllByKeyword(info.getName());
    }


    /**
     * 通过供货商编号查找
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "通过编号查找一个合作商")
    @PostMapping("/findOneByCode")
    public AgencyInfo findOneByCode(@RequestBody String code) {
        return agencyService.findOneByCode(code);
    }


    @ApiOperation(value = "通过编号检查合作商是否存在")
    @PostMapping("/checkCodeExists")
    public Boolean checkCodeExists(@RequestBody String code) {
        return agencyService.checkCodeExists(code);
    }


    @ApiOperation(value = "获取所有合作商")
    @PostMapping("/findAll")
    public List<AgencyInfo> findAll() {
        return agencyService.findAll();
    }

    @ApiOperation(value = "获取所有启用状态的合作商")
    @PostMapping("/findEnabled")
    public List<AgencyInfo> findEnabled() {
        return agencyService.findEnabled();
    }


    @ApiOperation(value = "通过合作商id查找对应覆盖的省份")
    @PostMapping("/findOneProvincesById")
    public AgencyInfo findOneProvincesById(@RequestBody String objId) {

        AgencyInfo agencyInfo = agencyService.findOne(objId);

        List<ProvinceInfo> regionInfoList = regionService.findAllProvincesByAgencyId(objId);
        if (regionInfoList != null && regionInfoList.size() > 0) {
            agencyInfo.setProvinces(regionInfoList);
        } else {
            agencyInfo.setProvinces(new ArrayList<>());
        }


        return agencyInfo;
    }


    @ApiOperation(value = "通过省份id查找合作商")
    @PostMapping("/findOneAgencnyById")
    public List<AgencyInfo> findOneAgencnyById(@RequestBody String provincesId) {

        return agencyService.findAllAgencyByProvinceId(provincesId);

    }

    @PostMapping("/findOneBackupInfoById")
    public AgencyBackupEntity findOneBackupInfoById(@RequestBody String backupInfoId) {
        return agencyBackupService.findOneBackupInfoById(backupInfoId);
    }

    @PostMapping("/saveBackupInfo")
    public AgencyBackupEntity saveBackupInfo(@RequestBody AgencyBackupEntity agencyBackupEntity) {
        return agencyBackupService.saveBackupInfo(agencyBackupEntity);
    }
}
