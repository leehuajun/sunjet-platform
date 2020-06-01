package com.sunjet.backend.controller.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerAdmitRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAdmitRequestView;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.modules.asms.service.dealer.DealerAdmitService;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAdmitRequestItem;
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
 * 服务站准入申请
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@RestController
@RequestMapping("/dealerAdmitRequest")
public class DealerAdmitController {

    @Autowired
    private DealerAdmitService dealerAdmitService;
    @Autowired
    private DocumentNoService documentNoService;

    @GetMapping("/findAll")
    public List<DealerAdmitRequestInfo> findAll() {
        return dealerAdmitService.findAll();
    }

    @PostMapping("/save")
    public DealerAdmitRequestInfo save(@RequestBody DealerAdmitRequestInfo dealerAdmitRequestInfo) {
        if (dealerAdmitRequestInfo != null && StringUtils.isBlank(dealerAdmitRequestInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(DealerAdmitRequestEntity.class.getSimpleName());
            dealerAdmitRequestInfo.setDocNo(docNo);

        }
        return dealerAdmitService.save(dealerAdmitRequestInfo);
    }

    @PostMapping("/findOneById")
    public DealerAdmitRequestEntity findOneById(@RequestBody String ObjId) {
        return dealerAdmitService.findOneById(ObjId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody DealerAdmitRequestInfo info) {
        return dealerAdmitService.delete(info);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String objId) {
        return dealerAdmitService.deleteByObjId(objId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<DealerAdmitRequestView> getPageList(@RequestBody PageParam<DealerAdmitRequestItem> pageParam) {
        return dealerAdmitService.getPageList(pageParam);
    }

    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> map) {
        return dealerAdmitService.startProcess(map);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return dealerAdmitService.desertTask(objId);
    }


}
