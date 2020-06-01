package com.sunjet.frontend.vm.base;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.dto.asms.flow.ProcessDefinitionInfo;
import com.sunjet.dto.asms.flow.ProcessInstanceInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.dto.system.admin.MenuInfo;
import com.sunjet.dto.system.admin.MessageInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.auth.ActiveUser;
import com.sunjet.frontend.config.CustomConfig;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.flow.ProcessService;
import com.sunjet.frontend.service.system.*;
import com.sunjet.frontend.utils.activiti.CustomComment;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.common.LoggerUtil;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.NoEmptyConstraint;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.utils.common.DateHelper;
import com.sunjet.utils.common.JsonHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * Created by wfb on 17-7-28.
 */
public class BaseVM {


    @WireVariable
    protected DealerService dealerService;   // 服务站

    @WireVariable
    protected AgencyService agencyService;  // 合作商

    @WireVariable
    protected DictionaryService dictionaryService;      // 数据字典

    @WireVariable
    protected RegionService regionService;    // 地区省份

    @WireVariable
    protected ConfigService configService;   //配置

    @WireVariable
    protected NoticeService noticeService;   //公告

    @WireVariable
    protected MessageService messageService;    // 消息通知

    @WireVariable
    protected UserService userService;   //用户

    @WireVariable
    protected ProcessService processService;
    @WireVariable
    protected MenuService menuService;


    @WireVariable
    protected CustomConfig customConfig;


    @Getter
    @Setter
    protected String keyword = "";    // 搜索关键字
    @Getter
    protected String window_position = "center,center";


    @Getter
    @Setter
    protected List<DealerInfo> dealers = new ArrayList<>();

    @Getter
    @Setter
    private NoEmptyConstraint noEmptyConst = new NoEmptyConstraint();       // 表单非空校验器

    @Getter
    @Setter
    private List<NoticeInfo> notices = new ArrayList<>();    //公告列表

    //@Getter
    //@Setter
    //protected List<TaskInfo> tasks = new ArrayList<>();    //流程任务列表

    @Getter
    @Setter
    protected List<MessageInfo> messages = new ArrayList<>();   // 消息通知列表

    @Getter
    @Setter
    private Map<String, String> mapRequestOrg = new HashMap<>();   //所属单位

    @Getter
    @Setter
    private Map<String, String> mapProvinceName = new HashMap<>();  //所属省份

    @Getter
    @Setter
    protected List<ProcessInstanceInfo> processInstances = new ArrayList<>();  //流程实例集合

    @Getter
    @Setter
    private Map<String, ProcessDefinitionInfo> mapProcessDefinition = new HashMap<>();
    ////修改密码按钮
    //@Getter
    //@Setter
    //private Boolean enableUpdatePassWord = false;

    @Getter
    @Setter
    private Map<String, String> users = new HashMap<>();   //用户列表
    @Getter
    @Setter
    protected String emptyMessage = "暂时没有找到任何记录!";
    @Getter
    @Setter
    protected Window win;           // 当前窗体
    @Getter
    @Setter
    protected String userType = "";


    @Getter
    @Setter
    protected FlowDocInfo flowDocInfo = new FlowDocInfo();

    @Getter
    @Setter
    protected String taskId = "";

    @Getter
    protected String decimalFormat = "####0.00";


    /**
     * 获取当前活动的用户
     *
     * @return
     */
    public ActiveUser getActiveUser() {
        return (ActiveUser) SecurityUtils.getSubject().getPrincipal();
    }


    /**
     * 弹出框大小
     *
     * @param event
     */
    @Command
    public void checkClientInfo(@BindingParam("evt") Event event) {
        if (win != null) {
            win.setHeight(CommonHelper.windowHeight + "px");
//            win.setWidth((CommonHelper.windowWidth - 100) + "px");
            if ((CommonHelper.windowWidth - 100) > 900) {
                win.setWidth((CommonHelper.windowWidth - 100) + "px");
            }
        } else {
            ZkUtils.showError("Win is Null", "error");
        }
    }


    /**
     * 查询
     *
     * @param keyword
     */
    @Command
    @NotifyChange("dealers")
    public void searchDealers(@BindingParam("keyword") String keyword) {
        this.dealers = dealerService.searchDealers(keyword, getActiveUser());
    }


    @Command
    @NotifyChange({"dealers", "keyword"})
    public void clearSelectedDealer() {
        dealers.clear();
        this.keyword = "";
    }


    /**
     * 获取通知公告 内容列表
     *
     * @return
     */
    protected List<NoticeInfo> getNoticeList() {
        return noticeService.findLastNotices(7);
    }


    /**
     * 初始化用户列表
     */
    protected void initUserList() {
        List<UserInfo> userList = userService.findAll();
        users.clear();
        for (UserInfo entity : userList) {
            users.put(entity.getLogId(), entity.getName());
        }
    }

    /**
     * 初始化流程定义列表
     */
    protected void initProcessDefinition() {
        this.mapProcessDefinition.clear();
        List<ProcessDefinitionInfo> list = processService.findProcessDefinitionList();
        for (ProcessDefinitionInfo pd : list) {
            mapProcessDefinition.put(pd.getId(), pd);
        }
    }

    /**
     * 初始化所属公司
     */
    protected void initRequestOrg() {
        this.mapRequestOrg.clear();
        List<UserInfo> userList = userService.findAll();
        Map<String, DealerInfo> dealerInfoMap = new HashMap<>();
        Map<String, AgencyInfo> agencyInfoMap = new HashMap<>();
        for (DealerInfo dealerInfo : dealerService.findAll()) {
            dealerInfoMap.put(dealerInfo.getObjId(), dealerInfo);
        }

        for (AgencyInfo agencyInfo : agencyService.findAll()) {
            agencyInfoMap.put(agencyInfo.getObjId(), agencyInfo);
        }


        for (UserInfo userEntity : userList) {
            if (userEntity.getUserType() == null || userEntity.getUserType() == "") {
                continue;
            }
            if (userEntity.getUserType().equals("dealer")) {
                userEntity.setDealer(dealerInfoMap.get(userEntity.getDealerId()));
                if (userEntity.getDealer() != null) {
                    this.mapRequestOrg.put(userEntity.getLogId(), userEntity.getDealer().getName());
                    this.mapProvinceName.put(userEntity.getLogId(), userEntity.getDealer().getProvinceName());
                }
            } else if (userEntity.getUserType().equals("agency")) {
                userEntity.setAgency(agencyInfoMap.get(userEntity.getAgencyId()));
                if (userEntity.getAgency() != null) {
                    this.mapRequestOrg.put(userEntity.getLogId(), userEntity.getAgency().getName());
                    this.mapProvinceName.put(userEntity.getLogId(), userEntity.getAgency().getProvinceName());
                }
            } else if (userEntity.getUserType().equals("wuling")) {
                this.mapRequestOrg.put(userEntity.getLogId(), "五菱销售公司");
            }
        }
    }


    /**
     * 获取任务中心 任务列表
     *
     * @return
     */
    protected List<TaskInfo> getTaskList() {
        List<TaskInfo> tasks = processService.findAllTaskList(getActiveUser().getLogId());
        Set<String> processInstanceIds = new HashSet<>();
        for (TaskInfo task : tasks) {
            processInstanceIds.add(task.getProcessInstanceId());
        }
        if (processInstanceIds.size() > 0) {
            processInstances = processService.findProcessInstanceByIds(processInstanceIds);
        }
        return tasks;
    }

    /**
     * 获取所有消息
     *
     * @return
     */
    protected List<MessageInfo> getMessageList() {
        return messageService.findAll();
    }

    protected List<MessageInfo> getUnReadMessageList() {

        List<MessageInfo> messages = new ArrayList<>();
        if (getActiveUser().getDealer() != null) {
            messages = messageService.findAllUnRead(getActiveUser().getDealer().getObjId());
        } else if (getActiveUser().getAgency() != null) {
            messages = messageService.findAllUnRead(getActiveUser().getAgency().getObjId());
        } else {
            messages = messageService.findAllUnReadByLogId(getActiveUser().getLogId());
        }

        return messages;
    }


    /**
     * 根据任务对象，打开业务处理页面（如编辑、流程处理）
     *
     * @param task
     */
    @Command
    public void handleTaskByTask(@BindingParam("task") TaskInfo task) {
        String formUrl = processService.findFormUrl(task);
        String businessId = processService.findBusinessIdByTaskId(task.getId());
        if (formUrl == null || formUrl.equals("")) {
            ZkUtils.showError("流程定义文件中没有定义formUrl属性！", "系统提示");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", task.getId());
//        map.put("status", DocStatus.getDocStatus(flowDocEntity.getStatus()));
        map.put("businessId", businessId);
        map.put("objId", businessId);

        MenuInfo menuByUrl = menuService.findMenuByUrl(StringUtils.replace(formUrl, "form", "list"));
        try {
            ZkTabboxUtil.newTab(businessId, menuByUrl.getName(), "", true, ZkTabboxUtil.OverFlowType.AUTO, formUrl, map);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取单据编号
     *
     * @param processInstanceId
     * @return
     */
    public String getDocNo(String processInstanceId) {
        String result = "";
        for (ProcessInstanceInfo pi : this.processInstances) {
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
     * 获取提交人名字
     *
     * @param processInstanceId
     * @return
     */
    public String getSubmitterName(String processInstanceId) {
        String result = "";
        for (ProcessInstanceInfo pi : this.processInstances) {
            if (pi.getId().equals(processInstanceId)) {
                String[] items = pi.getBusinessKey().split("\\.");
                if (items.length < 4) {
                    result = "";
                } else {
                    result = items[3];
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
    public String getSubmitter(String processInstanceId) {
        String result = "";
        for (ProcessInstanceInfo pi : this.processInstances) {
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

    /**
     * 提交信息
     *
     * @param json
     * @return
     */
    public CustomComment getBeanFromJson(String json) {
        CustomComment comment;
        try {//        return JsonHelper.json2Bean(json,CustomComment.class);
            comment = JsonHelper.json2Bean(json, CustomComment.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            comment = new CustomComment("", json);
        }
        return comment;
    }

    /**
     * 判断当前用户是否具有 permission 权限
     * <p>
     * permission 字符串 :   资源:操作        user:save
     *
     * @param permission
     * @return
     */
    public Boolean hasPermission(String permission) {
        LoggerUtil.getLogger().info(getActiveUser().getLogId());
        if (getActiveUser().getLogId().equals("admin")) {
            return true;
        } else {
            return SecurityUtils.getSubject().isPermitted(permission);
        }
    }


    /**
     * 保存提示框
     */
    protected void showDialog() {
        Messagebox.show("操作成功！", "系统提示", Messagebox.OK,
                Messagebox.INFORMATION, new org.zkoss.zk.ui.event.EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        int clickedButton = (Integer) event.getData();
                        if (clickedButton == Messagebox.OK) {
                            // 取消自动关闭对话框
//                            if(win !=null)
//                                win.detach();
                            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
                        }
                    }
                });
    }


    /**
     * 获取用户类型
     *
     * @return
     */
    //protected String  getActiveUserType(){
    //    if (getActiveUser().getDealer() != null) {   // 服务站用户
    //       return "dealer";
    //    } else if (getActiveUser().getAgency() != null) {   // 合作商
    //        return "agency";
    //    } else {   // 五菱用户
    //        List<RoleInfo> roles = getActiveUser().getRoles();
    //        boolean Permissions = false;
    //        if (roles != null) {
    //            for (RoleInfo role : roles) {
    //                if (role.getName().equals("服务经理")) {
    //                    Permissions = true;
    //                }
    //            }
    //        }
    //        if (Permissions) {
    //            //服务经理
    //            return "serviceManager";
    //        } else {
    //            //五菱其他用户
    //            return "wuling";
    //        }
    //    }
    //}
    protected List<DictionaryInfo> findDictionariesByParentCode(String parentCode) {
        List<DictionaryInfo> items = dictionaryService.findDictionariesByParentCode(parentCode);
        Collections.sort(items, (o1, o2) -> {
            if (Integer.parseInt(o1.getValue()) > Integer.parseInt(o2.getValue())) {
                return 1;
            } else {
                return -1;
            }
        });
        return items;
    }

    protected List<DictionaryInfo> findDictionariesByParentCodeWithAll(String parentCode) {
        List<DictionaryInfo> items = dictionaryService.findDictionariesByParentCode(parentCode);
        DictionaryInfo info = new DictionaryInfo();
        info.setName("--全部--");
        info.setCode("0");
        info.setValue("0");
        info.setSeq(0);
        items.add(info);

        Collections.sort(items, (o1, o2) -> {
            if (Integer.parseInt(o1.getValue()) > Integer.parseInt(o2.getValue())) {
                return 1;
            } else {
                return -1;
            }
        });
        return items;
    }


    /**
     * 列表时间格式化
     *
     * @param date
     * @return
     */
    public String dateToString(Date date) {
        return DateHelper.dateToString(date);
    }


    protected String getFilePath() {
        return this.customConfig.getFilePath();
    }

}
