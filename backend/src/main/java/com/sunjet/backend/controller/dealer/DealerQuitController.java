package com.sunjet.backend.controller.dealer;

import com.sunjet.backend.modules.asms.entity.dealer.DealerQuitRequestEntity;
import com.sunjet.backend.modules.asms.entity.dealer.view.DealerQuitRequestView;
import com.sunjet.backend.modules.asms.service.basic.DocumentNoService;
import com.sunjet.backend.modules.asms.service.dealer.DealerQuitService;
import com.sunjet.dto.asms.dealer.DealerQuitRequestInfo;
import com.sunjet.dto.asms.dealer.DealerQuitRequestItem;
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
 * 服务站退出申请
 * Created by zyf on 2017/7/28.
 */
@Slf4j
@RestController
@RequestMapping("/dealerQuitRequest")
public class DealerQuitController {
    @Autowired
    private DealerQuitService dealerQuitService;
    @Autowired
    private DocumentNoService documentNoService;

    @GetMapping("/findAll")
    public List<DealerQuitRequestInfo> findAll() {
        return dealerQuitService.findAll();
    }

    @PostMapping("/save")
    public DealerQuitRequestInfo save(@RequestBody DealerQuitRequestInfo dealerQuitRequestInfo) {
        if (dealerQuitRequestInfo != null && StringUtils.isBlank(dealerQuitRequestInfo.getDocNo())) {
            //获取单据编号
            String docNo = documentNoService.getDocumentNo(DealerQuitRequestEntity.class.getSimpleName());
            dealerQuitRequestInfo.setDocNo(docNo);

        }
        return dealerQuitService.save(dealerQuitRequestInfo);
    }

    @PostMapping("/findOneById")
    public DealerQuitRequestInfo findOneById(@RequestBody String ObjId) {
        return dealerQuitService.findOneById(ObjId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody DealerQuitRequestInfo info) {
        return dealerQuitService.delete(info);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String objId) {
        return dealerQuitService.deleteByObjId(objId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<DealerQuitRequestView> getPageList(@RequestBody PageParam<DealerQuitRequestItem> pageParam) {
        return dealerQuitService.getPageList(pageParam);
    }


    /**
     * 启动流程
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startProcess")
    public Map<String, String> startProcess(@RequestBody Map<String, Object> variables) {
        return dealerQuitService.startProcess(variables);
    }

    /**
     * 作废单据
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/desertTask")
    public Boolean desertTask(@RequestBody String objId) {
        return dealerQuitService.desertTask(objId);
    }


}
