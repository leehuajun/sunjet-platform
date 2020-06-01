package com.sunjet.backend.controller.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerAlterRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerAlterRequestView;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.modules.asms.service.dealer.DealerAlterService;
import com.sunjet.dto.asms.dealer.DealerAlterRequestInfo;
import com.sunjet.dto.asms.dealer.DealerAlterRequestItem;
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
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@RestController
@RequestMapping("/dealerAlterRequest")
public class DealerAlterController {

    @Autowired
    private DealerAlterService dealerAlterService;
    @Autowired
    private DocumentNoService documentNoService;

    @GetMapping("/findAll")
    public List<DealerAlterRequestInfo> findAll() {
        return dealerAlterService.findAll();
    }

    @PostMapping("/save")
    public DealerAlterRequestInfo save(@RequestBody DealerAlterRequestInfo dealerAlterRequestInfo) {
        if (dealerAlterRequestInfo != null && StringUtils.isBlank(dealerAlterRequestInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(DealerAlterRequestEntity.class.getSimpleName());
            dealerAlterRequestInfo.setDocNo(docNo);

        }
        return dealerAlterService.save(dealerAlterRequestInfo);
    }

    @PostMapping("/findOneById")
    public DealerAlterRequestInfo findOneById(@RequestBody String ObjId) {
        return dealerAlterService.findOneById(ObjId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody DealerAlterRequestInfo info) {
        return dealerAlterService.delete(info);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String objId) {
        return dealerAlterService.deleteByObjId(objId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<DealerAlterRequestView> getPageList(@RequestBody PageParam<DealerAlterRequestItem> pageParam) {
        return dealerAlterService.getPageList(pageParam);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> map) {
        return dealerAlterService.startProcess(map);
    }


    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return dealerAlterService.desertTask(objId);
    }


}
