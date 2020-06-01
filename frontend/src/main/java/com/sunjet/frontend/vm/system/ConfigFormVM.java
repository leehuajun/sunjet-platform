package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.frontend.service.system.ConfigService;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyf
 * @create 2017-7-13 上午10:59
 */
public class ConfigFormVM extends FormVM {

    @WireVariable
    private ConfigService configService;

    @Setter
    @Getter
    private ConfigInfo configInfo; // 配置信息实体info
    @Setter
    @Getter
    private List<ConfigInfo> configInfoList = new ArrayList<>(); // 配置信息列表

    @Init(superclass = true)
    public void init() {

        configInfoList = configService.findAll();
        if (StringUtils.isBlank(objId)) {
            configInfo = new ConfigInfo();
        } else {
            configInfo = configService.findOne(objId);
        }

    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    public void submit() {
        configInfo = configService.save(configInfo);
        //cacheManager.initConfig();
        //if (StringUtils.isNotBlank(objId)) {
        //    Map<String, Object> map = new HashMap<>();
        //    map.put("entity", configInfo);
        //    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_CONFIG_ENTITY, map);
        //} else {
        //    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_CONFIG_LIST, null);
        //}
        showDialog();
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_CONFIG_LIST, null);
    }

}
