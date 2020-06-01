package com.sunjet.backend.controller.agency;

import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAlterRequestView;
import com.sunjet.backend.modules.asms.service.agency.AgencyAlterService;
import com.sunjet.dto.asms.agency.AgencyAlterRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAlterRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/7/28.
 * 合作商变更申请
 */
@Slf4j
@RestController
@RequestMapping("/agencyAlter")
public class AgencyAlterController {

    @Autowired
    private AgencyAlterService agencyAlterService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<AgencyAlterRequestView> getPageList(@RequestBody PageParam<AgencyAlterRequestItem> pageParam) {
        return agencyAlterService.getPageList(pageParam);
    }

    @PostMapping("/findOne")
    public AgencyAlterRequestInfo findOne(@RequestBody String objId) {
        return agencyAlterService.findOne(objId);
    }

    @PostMapping("/save")
    public AgencyAlterRequestInfo save(@RequestBody AgencyAlterRequestInfo agencyAlterRequestInfo) {
        return agencyAlterService.save(agencyAlterRequestInfo);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return agencyAlterService.delete(objId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return agencyAlterService.startProcess(variables);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return agencyAlterService.desertTask(objId);
    }


}
