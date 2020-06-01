package com.sunjet.frontend.vm.flow;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * Created by lhj on 16/10/17.
 */
public class TaskListVM extends ListVM {
    protected final static Logger logger = LoggerFactory.getLogger(TaskListVM.class);

    //    @WireVariable
//    private ProcessService processService;


    @Getter
    @Setter
    private Date startDate;
    @Getter
    @Setter
    private Date endDate;
    //单据名称
    @Getter
    @Setter
    private String processName;
    //所属单位
    @Getter
    @Setter
    private String orgName;
    @Getter
    @Setter
    private String docNo;
    //单据类型
    @Getter
    @Setter
    private List<String> docType = new ArrayList<>();
    @Getter
    @Setter
    private String dealerName;  // 服务站名称
    @Setter
    @Getter
    private String dealerCode;  // 服务站编码
    @Getter
    @Setter
    private String ProvinceName; // 省份
    @Getter
    @Setter
    private ProvinceInfo selectedProvince;  //选择省份

    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities;

    @Getter
    @Setter
    private List<TaskInfo> tasks = new ArrayList<>();    //流程任务列表

    @Init(superclass = true)
    public void init() {
        startDate = DateHelper.getFirstOfYear();
        endDate = new Date();
        //初始化流程定义列表
        initProcessDefinition();
        //初始化所属公司
        initRequestOrg();
        docType.add("");
        docType.add("三包服务");   // 三包服务单
        docType.add("活动服务");   // 活动服务单
        docType.add("首保服务");      // 首保服务单
        docType.add("调拨通知");          // 供货通知单
        docType.add("供货流程");                // 供货单
        docType.add("费用速报");         // 费用速报
        docType.add("质量速报");         // 质量速报
        docType.add("故障件返回");               // 故障件返回单
        docType.add("合作商费用结算");      // 合作商费用结算单
        docType.add("服务站费用结算单");      // 服务站费用结算单
        docType.add("运费结算单");      // 运费结算单
        docType.add("活动发布");  // 活动发布单
        docType.add("故障件返回通知");         // 故障件返回通知单
        docType.add("活动通知");        // 活动通知单
        docType.add("合作商准入申请");    // 合作商准入申请
        docType.add("合作商变更申请");    // 合作商变更申请
        docType.add("合作商退出申请");     // 合作商退出申请
        docType.add("服务站准入申请");    // 服务站准入申请
        docType.add("服务站变更申请");    // 服务站变更申请
        docType.add("服务站等级变更申请"); // 服务站等级变更申请
        docType.add("服务站退出申请");     // 服务站退出申请
        this.setTasks(this.getTaskList());
        this.setProvinceEntities(regionService.findAllProvince());
    }


    @Command
    public void showFlowImage(@BindingParam("task") TaskInfo task) {
        String imageViewerUrl = "/views/flow/image_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("task", task);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }


    @GlobalCommand(GlobalCommandValues.LIST_TASK)
    @NotifyChange("tasks")
    public void listTask() {

        this.setTasks(this.getTaskList());
        this.searchTasks();
    }


    @Command
    @NotifyChange("tasks")
    public void searchTasks() {
        List<TaskInfo> taskList = this.getTaskList();
        this.getTasks().clear();
        for (TaskInfo task : taskList) {
            boolean isOk = true;

            if (this.processName != null && !this.processName.trim().equals("")) {
                if ("服务站费用结算单".equals(this.processName)) {
                    this.processName = "服务站费用结算流程";
                } else if ("运费结算单".equals(this.processName)) {
                    this.processName = "运输费用结算流程";
                }

                if (this.getMapProcessDefinition().get(task.getProcessDefinitionId()).getName().toString().contains(this.processName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }

                //System.out.println(this.getMapProcessDefinition().get(task.getProcessDefinitionId()).getName().toString());
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
            //单据编号搜素
            if (isOk && StringUtils.isNotBlank(this.docNo)) {
                //获取task里的单据编号
                String taskDocNo = getDocNo(task.getProcessInstanceId());

                if (taskDocNo != null && taskDocNo.contains(this.docNo.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }
            //服务站搜索
            if (isOk && StringUtils.isNotBlank(this.dealerName)) {
                //获取单位名称
                String name = this.getMapRequestOrg().get(this.getSubmitter(task.getProcessInstanceId()));
                if (name != null && name.contains(this.dealerName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }
            //省份搜索
            if (isOk && StringUtils.isNotBlank(this.ProvinceName)) {
                //获取单位名称
                String province = this.getMapProvinceName().get(this.getSubmitter(task.getProcessInstanceId()));
                if (province != null && province.contains(this.ProvinceName.trim())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            }

            if (isOk) {
                this.getTasks().add(task);
            }
        }
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }


    /**
     * 重置搜索条件
     */
    @Command
    @NotifyChange({"processName", "orgName", "docNo", "dealerCode", "dealerName", "ProvinceName", "provinceEntities"})
    public void reset() {
        this.processName = "";
        this.orgName = "";
        this.docNo = "";
        this.dealerCode = "";
        this.dealerName = "";
        this.ProvinceName = "";
        this.selectedProvince = null;
    }

    /**
     * 选择服务站
     */
    @Command
    @NotifyChange({"dealerCode", "dealerName"})
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        this.setDealerCode(model.getCode());
        this.setDealerName(model.getName());

    }

    /**
     * 选择省份
     */
    @Command
    @NotifyChange("provinceEntities")
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
        this.setProvinceName(selectedProvince.getName());
    }
}
