package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.OperationInfo;
import com.sunjet.frontend.service.system.OperationService;
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

/**
 * @author zyf
 * @create 2017-7-13 上午10:50
 */
public class OperationFormVM extends FormVM {

    @WireVariable
    private OperationService operationService;

    @Getter
    @Setter
    private OperationInfo operationInfo = new OperationInfo();

    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            operationInfo = operationService.findOneById(objId);
        } else {
            operationInfo = new OperationInfo();
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
    @NotifyChange("operationInfo")
    public void submit() {
        try {
            operationInfo = operationService.save(operationInfo);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_OPERATION_LIST, null);
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
