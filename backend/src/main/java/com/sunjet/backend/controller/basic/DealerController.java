package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.entity.basic.DealerBackupEntity;
import com.sunjet.backend.modules.asms.entity.basic.view.DealerView;
import com.sunjet.backend.modules.asms.service.basic.DealerBackupService;
import com.sunjet.backend.modules.asms.service.basic.DealerService;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.DealerItem;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.utils.common.JsonHelper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 服务站信息列表
 * Created by zyf on 2017/7/27.
 */
@Slf4j
@RestController
@RequestMapping("/dealer")
public class DealerController {

    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerBackupService dealerBackupService;

    /**
     * 获取所有的服务站信息
     *
     * @return
     */
    @ApiOperation(value = "获取所有服务站")
    @PostMapping("/findAll")
    public List<DealerInfo> findAll() {
        return dealerService.findAll();
    }

    @PostMapping("/save")
    public DealerInfo save(@RequestBody DealerInfo dealerInfo) {

        return dealerService.save(dealerInfo);
    }

    @PostMapping("/findOneById")
    public DealerInfo findOneById(@RequestBody String ObjId) {
        return dealerService.findOneById(ObjId);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody DealerInfo info) {
        return dealerService.delete(info);
    }

    @DeleteMapping("/deleteByObjId")
    public boolean deleteByObjId(@RequestBody String objId) {
        return dealerService.deleteByObjId(objId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<DealerView> getPageList(@RequestBody PageParam<DealerItem> pageParam) {
        return dealerService.getPageList(pageParam);
    }

    /**
     * 关键字搜索服务站
     *
     * @param info
     * @return
     */
    @PostMapping("/findAllByKeyword")
    public List<DealerInfo> findAllByKeyword(@RequestBody DealerInfo info) {
        return dealerService.findAllByKeyword(info.getName());
    }

    /**
     * 关键字搜索服务站父类
     *
     * @param dealerInfo
     * @return
     */
    @PostMapping("/findAllParentDealers")
    public List<DealerInfo> findAllDealerParent(@RequestBody DealerInfo dealerInfo) {
        return dealerService.findAllParentDealers(dealerInfo.getName());
    }


    /**
     * 根据用户类型和关键字搜素服务站
     *
     * @return
     */
    @PostMapping("/searchDealers")
    public List<DealerInfo> searchDealers(@RequestBody Map map) throws IOException {

        DealerInfo dealerInfo = JsonHelper.map2Bean(map.get("info"), DealerInfo.class);
        UserInfo userInfo = JsonHelper.map2Bean(map.get("userInfo"), UserInfo.class);
        List<DealerInfo> dealerInfos = dealerService.searchDealers(dealerInfo.getCode(), userInfo);
        return dealerInfos;
    }


    /**
     * 检查code是否存在
     *
     * @param dealerCode
     * @return
     */
    @PostMapping("/checkCodeExists")
    public Boolean checkCodeExists(@RequestBody String dealerCode) {
        return dealerService.checkCodeExists(dealerCode);
    }

    /**
     * 根据服务站编号查服务站
     *
     * @param dealerCode
     * @return
     */
    @PostMapping("/findOneByCode")
    public DealerInfo findOneByCode(@RequestBody String dealerCode) {
        return dealerService.findOneByCode(dealerCode);
    }

    /**
     * 通过服务经理查询服务站
     *
     * @param serviceManagerId
     * @return
     */
    @PostMapping("findAllByServiceManagerId")
    public List<DealerInfo> findAllByServiceManagerId(@RequestBody String serviceManagerId) {
        return dealerService.findAllByServiceManagerId(serviceManagerId);
    }

    /**
     * 查询没有绑定服务经理的服务站
     */
    @PostMapping("findAllNotServiceManager")
    public List<DealerInfo> findAllNotServiceManager() {
        return dealerService.findAllNotServiceManager();
    }

    @PostMapping("/findOneBackupById")
    public DealerBackupEntity findOneBackupById(@RequestBody String backupById) {
        return dealerBackupService.findOneBackupById(backupById);
    }

    @PostMapping("/saveDealerBackup")
    public DealerBackupEntity saveDealerBackup(@RequestBody DealerBackupEntity dealerBackupEntity) {
        return dealerBackupService.saveDealerBackup(dealerBackupEntity);
    }
}
