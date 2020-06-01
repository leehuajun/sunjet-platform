package com.sunjet.frontend.vm.base;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.flow.*;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.supply.SupplyInfo;
import com.sunjet.dto.system.admin.ConfigInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.asm.PendingSettlementDetailsService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by wfb on 17-7-25.
 */
public class FormVM extends BaseVM {

    @WireVariable
    private PendingSettlementDetailsService pendingSettlementDetailsService;
    @Getter
    @Setter
    protected String objId;//实体主键
    @Getter
    @Setter
    private String businessId;//业务id

    @Getter
    @Setter
    protected Boolean readonly = false;//只读

    @Getter
    @Setter
    private List<String> outcomes = new ArrayList<>();  //按钮
    @Getter
    @Setter
    private List<CommentInfo> comments = new ArrayList<>();  //意见

    @Getter
    @Setter
    protected String processDefinitionKey = "";

    @Getter
    @Setter
    private String comment = "";


    @Getter
    @Setter
    private Boolean handle = false; //是否显示窗体

    @Getter
    @Setter
    private Boolean checkCanPrint = false; // 是否显示打印

    @Getter
    @Setter
    protected Boolean canEdit = true;   //检查是否可以编辑

    @Getter
    @Setter
    protected Boolean canHandleTask = false;    // 是否显示代办任务  默认不显示:false

    @Getter
    @Setter
    protected Boolean canShowFlowImage = false;   //是显示流程图 默认不显示:false

    @Getter
    @Setter
    protected Boolean canDesertTask = false;   // 是否显示作废  默认不显示

    @Getter
    @Setter
    protected Boolean canShowOpenSupplyNoticeForm = false; //是否显示查看调拨通知单按钮

    @Getter
    @Setter
    protected Boolean canShowOpenRecycleNoticeForm = false; // 是否显示查看故障件返回通知单按钮
    @Getter
    @Setter
    protected String windowHeight = CommonHelper.screenHeight.toString() + "px";

    @Init
    public void formBaseInit() {

        this.objId = (String) Executions.getCurrent().getArg().get("objId");
        this.businessId = (String) Executions.getCurrent().getArg().get("businessId");
        this.taskId = (String) Executions.getCurrent().getArg().get("taskId");
        if (StringUtils.isNotBlank(objId)) {
            this.flowDocInfo = this.findInfoById(objId);
        }

        this.initUserList();
        getTaskList();

        //初始化按钮
        this.outcomes = StringUtils.isNotBlank(taskId) ? processService.findOutComeListByTaskId(taskId) : outcomes;
        //初始化审批记录
        this.comments = StringUtils.isNotBlank(taskId) ? processService.findCommentByTaskId(taskId) : comments;


        if (this.flowDocInfo != null && !this.flowDocInfo.getStatus().equals(DocStatus.DRAFT.getIndex()) && this.flowDocInfo.getProcessInstanceId() != null) {
            this.canShowFlowImage = true;
        }


        if (this.flowDocInfo != null && StringUtils.isNotBlank(this.flowDocInfo.getProcessInstanceId())) {
            if (this.flowDocInfo.getStatus().equals(DocStatus.REJECT.getIndex()) || this.flowDocInfo.getStatus().equals(DocStatus.WITHDRAW.getIndex())) {
                if (getActiveUser().getLogId().equals(this.flowDocInfo.getSubmitter())) {
                    readonly = false;
                } else {
                    readonly = true;
                }
            } else {
                readonly = true;
            }
        } else {
            readonly = false;
        }
    }


    /**
     * 模态化窗口位置
     *
     * @return
     */
    @Override
    public String getWindow_position() {
        return "center,center";
    }


    public void setActiveUserMsg(FlowDocInfo flowDocInfo) {
        this.flowDocInfo = flowDocInfo;
        this.processDefinitionKey = StringUtils.replace(this.flowDocInfo.getClass().getSimpleName(), "Info", "Entity");


        if (StringUtils.isBlank(flowDocInfo.getObjId())) {
            flowDocInfo.setSubmitter(getActiveUser().getLogId());
            flowDocInfo.setSubmitterName(getActiveUser().getUsername());
            flowDocInfo.setSubmitterPhone(getActiveUser().getPhone());

            flowDocInfo.setCreaterId(getActiveUser().getUserId());
            flowDocInfo.setCreaterName(getActiveUser().getUsername());
            flowDocInfo.setCreatedTime(new Date());

        } else {
            flowDocInfo.setModifierId(getActiveUser().getUserId());
            flowDocInfo.setModifierName(getActiveUser().getUsername());
            flowDocInfo.setModifiedTime(new Date());
        }
//        } else {
//            Task task = getTaskByBusinessId(entity.getObjId());
//            if (task != null)
//                this.taskId = getTaskByBusinessId(entity.getObjId()).getId();
//        }
        // 草稿状态 和 驳回状态，允许修改
//        if (flowDocInfo.getStatus() == DocStatus.DRAFT.getIndex() || flowDocInfo.getStatus() == DocStatus.REJECT.getIndex()) {
//            this.setReadonly(false);
////            this.canSubmit = true;
//        } else { // 其余状态不允许修改
//            this.setReadonly(true);
////            this.canSubmit = false;
//        }
    }

    /**
     * 启动流程
     *
     * @param flowDocInfo
     * @return
     */
    protected Boolean startProcessInstance(FlowDocInfo flowDocInfo) {
        if (!StringUtils.isNotEmpty(flowDocInfo.getObjId())) {
            ZkUtils.showExclamation("请先保存数据再提交！", "系统提示");
            return false;
        }
        //FlowDocInfo one = (FlowDocInfo) this.baseService.getRepository().findOne(flowDocInfo.getObjId());
        if (flowDocInfo.getProcessInstanceId() != null) {
            ZkUtils.showError("此单据已经提交", "提示");
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_FORM, null);
            return false;
        }

        if (!flowDocInfo.getSubmitter().equals(getActiveUser().getLogId())) {
            ZkUtils.showExclamation("只能经办人提交流程！", "系统提示");
            return false;
        }
        if (checkValid() == false) {
            return false;
        }
        return true;
    }


    /**
     * 审批意见提交
     *
     * @param outcome
     */
    @Command
    @NotifyChange("*")
    public void commit(@BindingParam("outcome") String outcome) {
        if (StringUtils.isBlank(this.getComment().trim())) {
            ZkUtils.showInformation("请填写审批意见", "提示");
            return;
        }

        ZkUtils.showQuestion("是否确定执行该操作?", "询问", new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                int clickedButton = (Integer) event.getData();
                if (clickedButton == Messagebox.OK) {
                    // 用户点击的是确定按钮
                    //BindUtils.postGlobalCommand(null, null, GlobalCommandValues.COMMIT_TASK, map);
                    completeTask(outcome, comment);
                    handle = !handle;
//                    BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_HANDLE_TASK_LIST, null);
                    updateUIState();
                }
                return;
            }
        });

    }

    /**
     * 检查数据
     *
     * @return
     */
    protected Boolean checkValid() {
        return false;
    }


    protected FlowDocInfo findInfoById(String objId) {
        return null;
    }

    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return null;
    }


    /**
     * 审批流程
     *
     * @param outcome
     * @param comment
     * @throws IOException
     */
    protected void completeTask(String outcome, String comment) throws IOException {

        Map<String, Object> variables = new HashMap<>();
        if (outcome.equals("退回") || outcome.equals("驳回")) {
            variables.put("status", DocStatus.REJECT.getIndex());
        } else {
            variables.put("status", DocStatus.AUDITING.getIndex());
        }

        this.flowDocInfo = this.saveInfo(flowDocInfo);


        ProcessInstanceInfo processInstance = processService.completeTask(this.taskId, outcome, comment, getActiveUser().getLogId(), variables);
        this.flowDocInfo = this.findInfoById(flowDocInfo.getObjId());
        PendingSettlementDetailInfo pendingSettlementDetailInfo = pendingSettlementDetailsService.getOneBySrcDocID(flowDocInfo.getObjId());
        if (processInstance == null) {

            Boolean autoClose = CommonHelper.mapAutoClose.get(StringUtils.replace(flowDocInfo.getClass().getSimpleName(), "Info", "Entity"));
            if (autoClose) {   // 如果流程自动关闭

                if (pendingSettlementDetailInfo == null) {
                    flowDocInfo.setStatus(DocStatus.CLOSED.getIndex());  // 审批结束,并关闭流程
                }

                String businessKey01 = StringUtils.replace(flowDocInfo.getClass().getSimpleName(), "Info", "Entity")
                        + "." + flowDocInfo.getObjId()
                        + "." + flowDocInfo.getDocNo()
                        + "." + flowDocInfo.getSubmitterName();
                String businessKey02 = StringUtils.replace(flowDocInfo.getClass().getSimpleName(), "Info", "Entity")
                        + "." + flowDocInfo.getObjId()
                        + "." + flowDocInfo.getDocNo()
                        + "." + flowDocInfo.getSubmitterName()
                        + "." + flowDocInfo.getSubmitter();

                String businessKey = "";
                ProcessInstanceInfo pi = processService.findProcessInstanceByBusinessKey(businessKey01);
                ProcessDefinitionInfo processDefinition = null;
                if (pi == null) {
                    HistoricProcessInstanceInfo historicProcessInstance = processService.findHistoricProcessInstanceByBusinessKey(businessKey01);
                    if (historicProcessInstance == null) {
                        pi = processService.findProcessInstanceByBusinessKey(businessKey02);
                        if (pi == null) {
                            historicProcessInstance = processService.findHistoricProcessInstanceByBusinessKey(businessKey02);
                            if (historicProcessInstance == null) {
                                ZkUtils.showError("找不到流程定义内容！", "警告！");
                            } else {
                                businessKey = businessKey02;
                            }
                        } else {
                            businessKey = businessKey02;
                        }
                    } else {
                        businessKey = businessKey01;
                    }
                } else {
                    businessKey = businessKey01;
                }

                List<CommentInfo> comments = processService.findCommentByBusinessKey(businessKey);
                if (comments == null)
                    comments = processService.findCommentByBusinessKey(businessKey02);

                for (CommentInfo cmt : comments) {
                    CommentItem commentEntity = new CommentItem();
                    commentEntity.setFlowInstanceId(cmt.getProcessInstanceId());
                    commentEntity.setDoDate(cmt.getTime());
                    commentEntity.setUsername(userService.findOneWithUserId(cmt.getUserId()));
                    commentEntity.setUserId(flowDocInfo.getSubmitter());
                    commentEntity.setResult(cmt.getType());
                    commentEntity.setMessage(cmt.getFullMessage());
                    processService.saveComment(commentEntity);
                }

            } else {
                if (pendingSettlementDetailInfo == null) {
                    flowDocInfo.setStatus(DocStatus.AUDITED.getIndex());  // 审批结束
                }
            }
        } else {
            if (outcome.equals("退回") || outcome.equals("驳回")) {
                flowDocInfo.setStatus(DocStatus.REJECT.getIndex());  // 退回
            } else {
                flowDocInfo.setStatus(DocStatus.AUDITING.getIndex());  // 审批结束
            }
        }

        this.flowDocInfo = this.saveInfo(flowDocInfo);

        this.taskId = null;
        this.canHandleTask = false;

    }


    /**
     * 跟据服务站 获取服务站的当前工时单价
     *
     * @param dealer 服务站
     * @return
     */
    protected Double getHourPriceByDealer(DealerInfo dealer) {
        String result = "1";
        if (dealer.getCode() != null && dealer.getCode().length() > 4) {
            result = dealer.getCode().substring(3, 4);   // 返回城市等级编码
        }
        Double price = 0.0;
        /** 判断服务站的星级 (暂缺，待补充)*/
        if (StringUtils.isNotBlank(dealer.getStar())) {
            /** 下面判断城市 */
            List<DictionaryInfo> stars = dictionaryService.findDictionariesByParentCode("10010");
            String prices = "";
            for (DictionaryInfo de : stars) {
                if (de.getName().equals(dealer.getStar())) {
                    prices = de.getValue();
                    break;
                }
            }

            if (result.equals("1")) { // 一类城市
                price = Double.parseDouble(prices.split(",")[1]);
            } else {
                price = Double.parseDouble(prices.split(",")[0]);
            }
        }

        /** 下面判断是否严寒月份 */
        if (!regionService.findProvinceById(dealer.getProvinceId()).getCold()) {
            return price;
        }
        Map<String, ConfigInfo> map = configService.getAllConfig();
        List<String> months = Arrays.asList(map.get("frigid_months").getConfigValue().split(","));
        List<Integer> frigid_months = new ArrayList<>();
        for (String str : months) {
            frigid_months.add(Integer.parseInt(str));
        }

        if (frigid_months.contains(LocalDate.now().getMonth().getValue())) {
            price = price + Double.parseDouble(map.get("frigid_subsidy").getConfigValue());
        }

        return price;

    }

    /**
     * 刷新form表单
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_FORM)
    @NotifyChange("*")
    public void refresh_form() {

    }

    /**
     * 显示流程图
     *
     * @param entity
     */
    @Command
    public void showFlowImage(@BindingParam("entity") FlowDocInfo entity) {
        System.out.println("显示流程图");
//        Task task = processService.findTaskById(taskId);
        String imageViewerUrl = "/views/flow/audit_log_viewer.zul";
        Map<String, Object> vars = new HashMap<>();
        vars.put("entity", entity);
        Window win = (Window) ZkUtils.createComponents(imageViewerUrl, null, vars);
        win.doModal();
    }


    @Command()
    @NotifyChange("handle")
    public void showHandleForm() {
        this.comment = "";
        this.handle = !this.handle;
        BindUtils.postNotifyChange(null, null, this, "comment");
        BindUtils.postNotifyChange(null, null, this, "handle");
    }

    protected void updateUIState() {
    }


    /**
     * 显示作废按钮
     *
     * @return
     */
    public boolean checkCanDesert() {
        if ((this.flowDocInfo.getStatus().equals(DocStatus.REJECT.getIndex()) || this.flowDocInfo.getStatus().equals(DocStatus.WITHDRAW.getIndex())) && getActiveUser().getLogId().equals(this.flowDocInfo.getSubmitter())) {
            canDesertTask = true;
        } else {
            canDesertTask = false;
        }
        return canDesertTask;
    }

    /**
     * 显示任务办理
     *
     * @return
     */
    public Boolean checkCanHandleTask() {
        if (StringUtils.isBlank(taskId)) {
            this.canHandleTask = false;
        } else {
            this.canHandleTask = true;
        }
        return this.canHandleTask;
    }


    /**
     * 显示供货快递单打印按钮
     *
     * @return
     */
    public boolean checkCanExpressPrintReport() {
        if (!SupplyInfo.class.getSimpleName().equals(flowDocInfo.getClass().getSimpleName()))
            return false;
        return true;
    }

    /**
     * 是否可以提交
     *
     * @return
     */
    public Boolean checkCanCommit() {
        if ("admin".equals(getActiveUser().getLogId())) {
            return false;
        }
        if (StringUtils.isBlank(flowDocInfo.getProcessInstanceId())) {
            if (flowDocInfo.getStatus() == DocStatus.DRAFT.getIndex() && getActiveUser().getLogId().equals(flowDocInfo.getSubmitter())) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }


    /**
     * 检查编辑按钮
     *
     * @return
     */
    public Boolean checkCanEdit() {
        //如果是管理员不能保存
        if ("admin".equals(getActiveUser().getLogId())) {
            return false;
        }
        //如果是提交人判断单据状态
        if (StringUtils.isNotBlank(flowDocInfo.getSubmitter())) {
            if (flowDocInfo.getStatus() == DocStatus.DRAFT.getIndex()) {
                if (flowDocInfo.getSubmitter().equals(getActiveUser().getLogId())) {
//                this.readonly = false;
                    return true;
                }
            } else if (flowDocInfo.getStatus() == DocStatus.REJECT.getIndex() || flowDocInfo.getStatus() == DocStatus.WITHDRAW.getIndex()) {
                if (flowDocInfo.getSubmitter().equals(getActiveUser().getLogId()) && StringUtils.isNotBlank(this.taskId)) {
//                this.readonly = false;
                    return true;
                }
            }
        } else {
            return true;
        }

        return false;
    }

    /**
     * 检查生成返回单按钮
     *
     * @return
     */
    public boolean checkCanEditRecycle() {
        return false;
    }


    /**
     * 检查生成调拨单按钮
     *
     * @return
     */
    public Boolean checkCanEditSupply() {
        return false;
    }


    /**
     * 根据历史审批记录类型判断 显示数据
     *
     * @param type
     * @return
     */
    public Boolean checkComments(String type) {
        if ("comment".equals(type)) {
            return false;
        }
        return true;
    }


}
