package com.sunjet.backend.controller.agency;

import com.sunjet.backend.modules.asms.entity.agency.view.AgencyAdmitRequestView;
import com.sunjet.backend.modules.asms.service.agency.AgencyAdmitService;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyAdmitRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/7/28.
 * 合作商准入申请
 */
@Slf4j
@RestController
@RequestMapping("/agencyAdmit")
public class AgencyAdmitController {

    @Autowired
    private AgencyAdmitService agencyAdmitService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @ApiOperation(value = "合作商准入申请分页查询")
    @PostMapping("/getPageList")
    public PageResult<AgencyAdmitRequestView> getPageList(@RequestBody PageParam<AgencyAdmitRequestItem> pageParam) {
        return agencyAdmitService.getPageList(pageParam);
    }

    @ApiOperation(value = "合作商准入申请查询一个实体")
    @PostMapping("/findOne")
    public AgencyAdmitRequestInfo findOne(@RequestBody String objId) {
        return agencyAdmitService.findOne(objId);
    }

    @ApiOperation(value = "合作商准入申请保存一个实体")
    @PostMapping("/save")
    public AgencyAdmitRequestInfo save(@RequestBody AgencyAdmitRequestInfo agencyAdmitRequestInfo) {
        return agencyAdmitService.save(agencyAdmitRequestInfo);
    }

    @ApiOperation(value = "合作商准入申请删除一个实体")
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return agencyAdmitService.delete(objId);
    }

    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> map) {
        return agencyAdmitService.startProcess(map);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return agencyAdmitService.desertTask(objId);
    }

}
