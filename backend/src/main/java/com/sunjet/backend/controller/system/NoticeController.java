package com.sunjet.backend.controller.system;

import com.sunjet.backend.modules.asms.entity.basic.NoticeEntity;
import com.sunjet.backend.modules.asms.service.basic.NoticeService;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/12.
 * 公告
 */
@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    /**
     * 获取所有公告
     *
     * @return
     */
    @ApiOperation(value = "查找所有公告")
    @PostMapping("findAll")
    public List<NoticeEntity> findAll() {
        return noticeService.findAll();
    }

    /**
     * 获取最近days天的公告信息
     *
     * @param days
     * @return
     */
    @ApiOperation(value = "获取最近days天的公告信息")
    @PostMapping("/findLastNotices")
    public List<NoticeEntity> findLastNotices(@RequestBody int days) {
        return noticeService.findLastNotices(days);
    }

    /**
     * 获取分页
     *
     * @param pageParam
     * @return
     */
    @ApiOperation(value = "获取公告分页")
    @PostMapping("getPageList")
    public PageResult<NoticeEntity> getPageList(@RequestBody PageParam<NoticeEntity> pageParam) {
        return noticeService.getPageList(pageParam);
    }

    /**
     * 通过objid 获取实体
     *
     * @param objId
     * @return
     */
    @ApiOperation(value = "查找一个公告", notes = "通过objId获取一个公告Info")
    @ApiImplicitParam(name = "objId", value = "实体objId", required = true, dataType = "String")
    @PostMapping("findOneById")
    public NoticeEntity findOneById(@RequestBody String objId) {
        return noticeService.findOne(objId);
    }


    @ApiOperation(value = "保存一个实体对象")
    @PostMapping("/save")
    public NoticeEntity save(@RequestBody NoticeEntity notice) {
        return noticeService.save(notice);
    }


    /**
     * 删除公告公告
     *
     * @return
     */
    @ApiOperation(value = "删除公告")
    @DeleteMapping("/delete")
    public Boolean delete(@RequestBody String objId) {
        return noticeService.delete(objId);
    }

}
