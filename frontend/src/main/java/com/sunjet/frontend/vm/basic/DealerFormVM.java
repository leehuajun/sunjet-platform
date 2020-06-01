package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.system.RoleService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务站 表单
 * <p>
 * Created by Administrator on 2017/7/13.
 */
public class DealerFormVM extends FormVM {

    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private RoleService roleService;
    @WireVariable
    private RegionService regionService;

    @Getter
    @Setter
    private DealerInfo dealerInfo = new DealerInfo();
    @Getter
    @Setter
    private List<DealerInfo> serviceManagers = new ArrayList<>();
    @Getter
    @Setter
    private List<String> enableds = new ArrayList<>();
    @Getter
    @Setter
    private String selectEnabled;

    @Getter
    @Setter
    private Boolean canSave = false;

    @Init(superclass = true)
    public void init() {
        this.setCanSave(hasPermission("DealerEntity:modify"));
        this.enableds.add("是");
        this.enableds.add("否");
        if (StringUtils.isNotBlank(objId)) {
            dealerInfo = dealerService.findOneById(objId);
        } else {
            dealerInfo = new DealerInfo();
        }

    }

    @Command
    public void saveServiceManager() {
        if ("是".equals(this.getSelectEnabled())) {
            dealerInfo.setEnabled(true);
        } else if ("否".equals(this.getSelectEnabled())) {
            dealerInfo.setEnabled(false);
        } else {
            //默认设置
            dealerInfo.setEnabled(dealerInfo.getEnabled());
        }

        dealerInfo.setServiceManagerName(dealerInfo.getServiceManagerName());
        dealerService.save(dealerInfo);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_LIST, null);
        ZkUtils.showInformation("保存成功", "提示");

    }

    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_DEALER + filename;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    /**
     * 表单提交,保存用户信息
     */
    @Command
    @NotifyChange("dealerInfo")
    public void submit() {
        try {
            dealerInfo = dealerService.save(dealerInfo);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEALER_LIST, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
