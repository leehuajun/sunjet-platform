package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.entity.basic.MaintainEntity;
import com.sunjet.backend.modules.asms.service.basic.MaintainService;
import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 维修项目及工时定额列表
 * Created by zyf on 2017/7/27.
 */
@Slf4j
@RestController
@RequestMapping("/maintain")
public class MaintainController {

    @Autowired
    private MaintainService maintainService;

    @GetMapping("/findAll")
    public List<MaintainInfo> findAll() {
        return maintainService.findAll();
    }

    @PostMapping("/save")
    public MaintainInfo save(@RequestBody MaintainInfo maintainInfo) {

        return maintainService.save(maintainInfo);
    }

    @PostMapping("/findOneById")
    public MaintainInfo findOneById(@RequestBody String ObjId) {
        return maintainService.findOneById(ObjId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody MaintainInfo info) {
        return maintainService.delete(info);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String objId) {
        return maintainService.deleteByObjId(objId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<MaintainInfo> getPageList(@RequestBody PageParam<MaintainInfo> pageParam) {
        return maintainService.getPageList(pageParam);
    }

    /**
     * 关键字搜索
     *
     * @param maintainEntity
     * @return
     */
    @PostMapping("/findAllByFilter")
    public List<MaintainEntity> findAllByFilter(@RequestBody MaintainEntity maintainEntity) {
        return maintainService.findAllByFilter(maintainEntity);

    }

    @PostMapping("/importData")
    public List<MaintainInfoExt> importData(@RequestBody List<MaintainInfoExt> infos) {
        return maintainService.importData(infos);
    }


}
