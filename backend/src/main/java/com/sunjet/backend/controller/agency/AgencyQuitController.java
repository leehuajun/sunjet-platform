package com.sunjet.backend.controller.agency;

import com.sunjet.backend.modules.asms.entity.agency.view.AgencyQuitRequestView;
import com.sunjet.backend.modules.asms.service.agency.AgencyQuitService;
import com.sunjet.dto.asms.agency.AgencyQuitRequestInfo;
import com.sunjet.dto.asms.agency.AgencyQuitRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by SUNJET_QRY on 2017/7/28.
 * 合作商退出申请
 */
@Slf4j
@RestController
@RequestMapping("/agencyQuit")
public class AgencyQuitController {

    @Autowired
    private AgencyQuitService agencyQuitService;

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<AgencyQuitRequestView> getPageList(@RequestBody PageParam<AgencyQuitRequestItem> pageParam) {
        return agencyQuitService.getPageList(pageParam);
    }

    @PostMapping("/findOne")
    public AgencyQuitRequestInfo findOne(@RequestBody String objId) {
        return agencyQuitService.findOne(objId);
    }

    @PostMapping("/save")
    public AgencyQuitRequestInfo save(@RequestBody AgencyQuitRequestInfo agencyQuitRequestInfo) {
        return agencyQuitService.save(agencyQuitRequestInfo);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return agencyQuitService.delete(objId);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return agencyQuitService.startProcess(variables);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return agencyQuitService.desertTask(objId);
    }

}
