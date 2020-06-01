package com.sunjet.frontend.vm.flow;

import com.sunjet.dto.asms.flow.DeploymentInfo;
import com.sunjet.frontend.service.flow.ProcessService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.util.List;

/**
 * Created by SUNJET_WS on 2017/9/6.
 */
public class DeploymentListVM extends FormVM {

    @WireVariable
    ProcessService processService;

    @Getter
    @Setter
    private List<DeploymentInfo> deployments;


    @Init(superclass = true)
    public void init() {
        deployments = processService.findDeploymentList();
    }


    /**
     * 删除部署对象
     *
     * @param deployment
     */
    @Command
    @NotifyChange("*")
    public void delete(@BindingParam("model") DeploymentInfo deployment) {
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    processService.deleteProcessDefinitionByDeploymentId(deployment.getId());
                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_DEPLOYMENT_LIST, null);
                } else {
                    return;
                }
            }
        });

    }


    @GlobalCommand(GlobalCommandValues.REFRESH_DEPLOYMENT_LIST)
    public void refreshDeploymentList() {
        deployments = processService.findDeploymentList();
        BindUtils.postNotifyChange(null, null, this, "deployments");
    }


}
