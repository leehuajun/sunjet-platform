package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.ConfigService;
import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/21.
 * 系统配置信息
 */
@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;


    @PostMapping("/add")
    public ConfigInfo getSave(@RequestBody ConfigInfo configInfo) {
        return configService.save(configInfo);
    }

    @PostMapping("/findOne")
    public ConfigInfo getFindOne(@RequestBody String objId) {
        return configService.findOne(objId);
    }

    @GetMapping("/findAll")
    public List<ConfigInfo> getFindAll() {
        return configService.findAll();
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<ConfigInfo> getPageList(@RequestBody PageParam<ConfigInfo> pageParam) {
        return configService.getPageList(pageParam);
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return configService.delete(objId);
    }

    /**
     * 获取所有配置
     *
     * @return
     */
    @PostMapping("/getAllConfig")
    public List<ConfigInfo> getAllConfig() {
        return configService.getAllConfig();
    }

    @PostMapping("getValueByKey")
    public String getValueByKey(@RequestBody String key) {
        return configService.getValueByKey(key);
    }


}
