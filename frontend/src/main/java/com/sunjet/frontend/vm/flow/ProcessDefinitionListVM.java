package com.sunjet.frontend.vm.flow;


import com.sunjet.dto.asms.flow.DeploymentInfo;
import com.sunjet.dto.asms.flow.ProcessDefinitionInfo;
import com.sunjet.frontend.service.flow.ProcessService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SUNJET_WS on 2017/9/5.
 * 流程定义
 */
public class ProcessDefinitionListVM extends ListVM {

    @WireVariable
    ProcessService processService;    //流程引擎

    @Getter
    @Setter
    private String flowName;

    @Getter
    @Setter
    private String originFileName;

    @Getter
    @Setter
    private String realFilePath;

    @Getter
    @Setter
    private List<ProcessDefinitionInfo> processDefinitions = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        this.setEnableAdd(hasPermission("ProcessDefinitionEntity:create"));
        this.setEnableUpdate(hasPermission("ProcessDefinitionEntity:modify"));
        this.setEnableDelete(hasPermission("ProcessDefinitionEntity:delete"));
        this.processDefinitions = processService.findProcessDefinitionLastVersionList();


    }


    /**
     * 查看部署对象
     */
    @Command
    public void showDeploymentForm() {
        String deploymentListUrl = "/views/flow/deployment_list.zul";
        Window win = (Window) ZkUtils.createComponents(deploymentListUrl, null, null);
        win.doModal();
    }


    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        originFileName = event.getMedia().getName();

        String fileName = ZkUtils.onUploadFile(event.getMedia(), Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW);
        realFilePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + CommonHelper.UPLOAD_DIR_FLOW + "/" + fileName;
//        ZkUtils.showInformation("上传文件成功！文件保存路径:\n" + realFilePath, "系统提示");
    }

    /**
     * 部署流程
     *
     * @throws IOException
     */
    @Command
    @NotifyChange("*")
    public void deploy() throws IOException {
        if (this.flowName == null || this.flowName.trim().equals("")) {
            ZkUtils.showInformation("部署名称不能为空！", "系统提示");
            return;
        }


        DeploymentInfo deploy = processService.deploy(this.flowName, this.realFilePath);
        if (deploy != null) {
            ZkUtils.showInformation("部署成功", "提示");
        } else {
            ZkUtils.showInformation("部署失败", "提示");
        }


        this.flowName = "";
        this.originFileName = "";
        this.realFilePath = "";
        this.processDefinitions = processService.findProcessDefinitionLastVersionList();
    }

    /**
     * 查看流程图
     *
     * @param processDefinition
     */
    @Command
    public void showFlowImage(@BindingParam("model") ProcessDefinitionInfo processDefinition) {
        String imageViewerUrl = "/views/flow/image_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("processDefinition", processDefinition);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }


}
