package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import com.sunjet.backend.modules.asms.service.basic.PartService;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.basic.PartInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
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
 * 配件目录
 */
@Slf4j
@RestController
@RequestMapping("/part")
public class PartController {

    @Autowired
    PartService partService;


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<PartInfo> getPageList(@RequestBody PageParam<PartInfo> pageParam) {
        return partService.getPageList(pageParam);
    }


    /**
     * 通过objid查找一个实体
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOne")
    public PartInfo findOne(@RequestBody String objId) {
        return partService.findOne(objId);
    }

    /**
     * 保存一个实体
     *
     * @param partInfo
     * @return
     */
    @PostMapping("/save")
    public PartInfo save(@RequestBody PartInfo partInfo) {
        return partService.save(partInfo);
    }

    /**
     * 配件搜索(不包含已经禁用的)
     *
     * @param keyword
     * @return
     */
    @PostMapping("/findAllByKeyword")
    public List<PartInfo> findAllByKeyword(@RequestBody String keyword) {
        return partService.findAllByKeyword(keyword);
    }


    /**
     * 配件搜索
     *
     * @param keyword
     * @return
     */
    @PostMapping("/findAllByCode")
    public List<PartEntity> findAllByCode(@RequestBody String keyword) {
        return partService.findAllByCode(keyword);
    }


    @PostMapping("/findByPartId")
    public List<PartInfo> findByPartId(@RequestBody List<String> objIds) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(objIds);
        return partService.findByPartId(arrayList);
    }

    @PostMapping("/importParts")
    public List<PartInfoExt> importParts(@RequestBody List<PartInfoExt> infoExts) {
        return partService.importParts(infoExts);
    }

    @PostMapping("/modifyParts")
    public List<PartInfoExt> modifyParts(@RequestBody List<PartInfoExt> infoExts) {
        return partService.modifyParts(infoExts);
    }


    /**
     * 通过code查配件
     *
     * @param code
     * @return
     */
    @PostMapping("/findOneByCode")
    public PartEntity findOneByCode(@RequestBody String code) {
        return partService.findOneByCode(code);
    }

}
