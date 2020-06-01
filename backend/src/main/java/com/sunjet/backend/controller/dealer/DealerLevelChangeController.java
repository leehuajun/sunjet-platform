package com.sunjet.backend.controller.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerLevelChangeRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerLevelChangeRequestView;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.modules.asms.service.dealer.DealerLevelChangeService;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestInfo;
import com.sunjet.dto.asms.dealer.DealerLevelChangeRequestItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 服务站等级变更申请
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@RestController
@RequestMapping("/dealerLevelChangeRequest")
public class DealerLevelChangeController {
    @Autowired
    private DealerLevelChangeService dealerLevelChangeService;
    @Autowired
    private DocumentNoService documentNoService;

    @GetMapping("/findAll")
    public List<DealerLevelChangeRequestInfo> findAll() {
        return dealerLevelChangeService.findAll();
    }

    @PostMapping("/save")
    public DealerLevelChangeRequestInfo save(@RequestBody DealerLevelChangeRequestInfo dealerLevelChangeRequestInfo) {
        if (dealerLevelChangeRequestInfo != null && StringUtils.isBlank(dealerLevelChangeRequestInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(DealerLevelChangeRequestEntity.class.getSimpleName());
            dealerLevelChangeRequestInfo.setDocNo(docNo);

        }
        return dealerLevelChangeService.save(dealerLevelChangeRequestInfo);
    }

    @PostMapping("/findOneById")
    public DealerLevelChangeRequestInfo findOneById(@RequestBody String ObjId) {
        return dealerLevelChangeService.findOneById(ObjId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody DealerLevelChangeRequestInfo info) {
        return dealerLevelChangeService.delete(info);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String objId) {
        return dealerLevelChangeService.deleteByObjId(objId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<DealerLevelChangeRequestView> getPageList(@RequestBody PageParam<DealerLevelChangeRequestItem> pageParam) {
        return dealerLevelChangeService.getPageList(pageParam);
    }

    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> map) {
        return dealerLevelChangeService.startProcess(map);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return dealerLevelChangeService.desertTask(objId);
    }


}
