package com.sunjet.frontend.vm.flow;

import com.sunjet.dto.asms.flow.HistoricProcessInstanceInfo;
import com.sunjet.frontend.vm.base.BaseVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 已完成任务
 * Created by SUNJET_WS on 2017/10/8.
 */
public class FinishTaskListVM extends BaseVM {

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
    private List<HistoricProcessInstanceInfo> historicProcessInstanceList = new ArrayList<>();

    @Getter
    @Setter
    private List<HistoricProcessInstanceInfo> historicList = new ArrayList<>();

    @Init(superclass = true)
    public void init() {
        startDate = DateHelper.getFirstOfYear();
        endDate = new Date();
        initProcessDefinition();
        initRequestOrg();
        initUserList();
        historicProcessInstanceList = processService.findHistoricTaskByLogId(getActiveUser().getLogId());
        this.setHistoricList(historicProcessInstanceList);


    }

    /**
     * 获取历史单据编号
     *
     * @param businessKey
     * @return
     */
    public String getHistoricProcessDocNo(String businessKey) {
        String docNo = "";
        String[] items = businessKey.split("\\.");
        if (items.length < 3) {
            docNo = "";
        } else {
            docNo = items[2];
        }
        return docNo;
    }


    @Command
    @NotifyChange("*")
    public void searchTasks() {
        this.historicList.clear();
        historicProcessInstanceList = processService.findHistoricTaskByLogId(getActiveUser().getLogId());
        for (HistoricProcessInstanceInfo historicProcessInstanceInfo : historicProcessInstanceList) {
            boolean isOk = true;

            if (this.processName != null && !this.processName.trim().equals("")) {
                if (this.getMapProcessDefinition().get(historicProcessInstanceInfo.getProcessDefinitionId()).getName().toString().contains(this.processName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                if (historicProcessInstanceInfo.getStartTime().after(startDate) && historicProcessInstanceInfo.getStartTime().before(endDate)) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }
            if (isOk && this.orgName != null && !this.orgName.trim().equals("")) {
                String orgname = this.getMapRequestOrg().get(this.getSubmitter(historicProcessInstanceInfo.getSuperProcessInstanceId()));

                if (orgname != null && orgname.contains(orgName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                this.historicList.add(historicProcessInstanceInfo);
            }
        }
    }


    /**
     * 获取提交人名字
     *
     * @param businessKey
     * @return
     */
    public String getName(String businessKey) {
        String result = "";
        String[] items = businessKey.split("\\.");
        if (items.length < 4) {
            result = "";
        } else {
            result = items[3];
        }

        return result;
    }


}
