package com.sunjet.frontend.vm.flow;

import com.sunjet.dto.asms.flow.HistoricTaskInstanceInfo;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.BaseVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * 已处理任务
 * Created by SUNJET_WS on 2017/10/8.
 */
public class HandleTaskListVM extends BaseVM {

    @Getter
    @Setter
    private Date startDate;
    @Getter
    @Setter
    private Date endDate;

    @Getter
    @Setter
    private String processName;
    @Getter
    @Setter
    private String orgName;

    @Getter
    @Setter
    private List<HistoricTaskInstanceInfo> handleTask = new ArrayList<>();

    @Getter
    @Setter
    private List<HistoricTaskInstanceInfo> searchTaskList = new ArrayList<>();

    @Getter
    @Setter
    private List<ProcessInstanceInfo> processInstanceInfoList = new ArrayList<>();  //已处理流程实例


    @Init(superclass = true)
    public void init() {
        startDate = DateHelper.getFirstOfYear();
        endDate = new Date();
        initProcessDefinition();
        initRequestOrg();
        //getTaskList();
        handleTask = processService.findHandleTaskByLogId(getActiveUser().getLogId());
        getprocessInstance(handleTask);

    }

    /**
     * 获取已处理任务流程实例
     *
     * @param handleTask
     */
    public void getprocessInstance(List<HistoricTaskInstanceInfo> handleTask) {
        Set<String> processInstanceIds = new HashSet<>();
        if (handleTask != null) {
            for (HistoricTaskInstanceInfo historicTaskInstanceInfo : handleTask) {
                processInstanceIds.add(historicTaskInstanceInfo.getProcessInstanceId());
            }
            if (processInstanceIds.size() > 0) {
                processInstanceInfoList = processService.findProcessInstanceByIds(processInstanceIds);
            }
        }

    }


    /**
     * 获取单据编号
     *
     * @param processInstanceId
     * @return
     */
    public String getHandleDocNo(String processInstanceId) {
        String result = "";
        for (ProcessInstanceInfo pi : this.processInstanceInfoList) {
            if (pi.getId().equals(processInstanceId)) {
                String[] items = pi.getBusinessKey().split("\\.");
                if (items.length < 3) {
                    result = "";
                } else {
                    result = items[2];
                }
                break;
            }
        }
        return result;
    }


    /**
     * 提交人id
     *
     * @param processInstanceId
     * @return
     */
    public String getHandleSubmitter(String processInstanceId) {
        String result = "";
        for (ProcessInstanceInfo pi : this.processInstanceInfoList) {
            if (pi.getId().equals(processInstanceId)) {
                String[] items = pi.getBusinessKey().split("\\.");
                if (items.length < 5) {
                    result = "";
                } else {
                    result = items[4];
                }
                break;
            }
        }
        return result;
    }


    @Command
    @NotifyChange("handleTask")
    public void searchTasks() {
        this.getHandleTask().clear();
        List<HistoricTaskInstanceInfo> handleTaskByLogId = processService.findHandleTaskByLogId(getActiveUser().getLogId());
        for (HistoricTaskInstanceInfo task : handleTaskByLogId) {
            boolean isOk = true;

            if (this.processName != null && !this.processName.trim().equals("")) {
                if (this.getMapProcessDefinition().get(task.getProcessDefinitionId()).getName().toString().contains(this.processName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                if (task.getCreateTime().after(startDate) && task.getCreateTime().before(endDate)) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }
            if (isOk && this.orgName != null && !this.orgName.trim().equals("")) {
                String orgname = this.getMapRequestOrg().get(this.getSubmitter(task.getProcessInstanceId()));

                if (orgname != null && orgname.contains(orgName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                this.searchTaskList.add(task);
            }
        }
        this.handleTask = this.searchTaskList;
    }


    /**
     * 撤销任务
     */
    @Command
    @NotifyChange("handleTask")
    public void callBackTask(@BindingParam("handleTask") HistoricTaskInstanceInfo historicTaskInstanceInfo) {
        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    Boolean state = processService.callBackTask(historicTaskInstanceInfo.getId(), getActiveUser().getLogId());

                    if (!state) {
                        ZkUtils.showInformation("此任务不能撤回", "提示");
                    } else {

                        showDialog();
                        //searchTasks();
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
                        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_HANDLE_TASK_LIST, null);
                        BindUtils.postNotifyChange(null, null, this, "handleTask");
                    }
                } else {
                    return;
                }

            }
        });


    }

    /**
     * 刷新已处理任务
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_HANDLE_TASK_LIST)
    @NotifyChange("handleTask")
    public void refreshHandleTaskList() {
        this.handleTask = processService.findHandleTaskByLogId(getActiveUser().getLogId());
        getprocessInstance(handleTask);
    }


    @Command
    public void showFlowImage(@BindingParam("handleTask") TaskInfo task) {
        String imageViewerUrl = "/views/flow/image_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("task", task);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }


}
