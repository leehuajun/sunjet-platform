package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.entity.view.OperationView;
import com.sunjet.backend.system.service.OperationService;
import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.dto.system.admin.OperationItem;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作列表
 * Created by zyf on 2017/7/24.
 */
@Slf4j
@RestController
@RequestMapping("/operation")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping("/findAll")
    public List<OperationInfo> findAll() {
        return operationService.findAll();
    }

    @PostMapping("/save")
    public OperationInfo save(@RequestBody OperationInfo operationInfo) {

        return operationService.save(operationInfo);
    }

    @DeleteMapping("/delete")
    public boolean delete(OperationInfo info) {
        return operationService.delete(info);
    }

    @PostMapping("/findOneById")
    public OperationInfo findOneById(@RequestBody String ObjId) {
        return operationService.findOneById(ObjId);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String ObjId) {
        return operationService.delete(ObjId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<OperationView> getPageList(@RequestBody PageParam<OperationItem> pageParam) {
        return operationService.getPageList(pageParam);
    }
}
