package com.sunjet.frontend.vm.base;

import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.flow.TaskInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.dto.system.base.Order;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import com.sunjet.frontend.service.basic.VehicleService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wfb on 17-7-24.
 * VM 基类：
 * 1.封装分页参数
 * 2.封装新增/修改请求路径
 * 3.封装用户权限
 */
public class ListVM<T> extends BaseVM {

    @WireVariable
    private VehicleService vehicleService;

    @Getter
    @Setter
    private Map<String, TaskInfo> mapTasks = new HashMap<>();
    @Getter
    @Setter
    private Map<String, String> mapUsers = new HashMap<>();
    @Getter
    @Setter
    private List<VehicleInfo> vehicles = new ArrayList<>();

    @Getter
    @Setter
    private Integer pageSize;
    /**
     * 分页参数
     */
    @Getter
    @Setter
    protected PageParam<T> pageParam = new PageParam<T>();
    /**
     * 分页返回
     */
    @Getter
    @Setter
    protected PageResult<T> pageResult;
    /**
     * 新增/修改页面请求地址
     */
    @Getter
    @Setter
    protected String formUrl;
    @Getter
    @Setter
    private String title = "";


    @Getter
    @Setter
    private Boolean enableAdd = false;  //新增按钮权限
    @Getter
    @Setter
    private Boolean enableUpdate = false;      // 编辑按钮状态
    @Getter
    @Setter
    private Boolean enableDelete = false;      // 删除按钮状态
    @Getter
    @Setter
    private Boolean enableRefresh = true;     // 刷新按钮状态
    @Getter
    @Setter
    private Boolean enableSearch = true;      // 查询按钮状态
    @Getter
    @Setter
    private Boolean enableReset = true;       // 重置按钮状态
    @Getter
    @Setter
    private Boolean enableAddRecycleDoc = false;  // 创建故障件返回单按钮状态
    @Getter
    @Setter
    private Boolean enableSaveAllocation = false;  // 保存分配信息按钮状态
    @Getter
    @Setter
    private Boolean enableSaveSupply = false; // 创建调拨供货单按钮状态
    @Getter
    @Setter
    private Boolean enableWarrantSettlement = false; // 创建服务结算单单按钮状态
    @Getter
    @Setter
    private Boolean enableFreightSettlement = false; // 创建运费结算单按钮状态
    @Getter
    @Setter
    private Boolean enablePartSettlement = false; // 创建配件结算单按钮状态
    @Getter
    @Setter
    private Boolean enableExport = false;   //导出excel按钮
    @Getter
    @Setter
    private Boolean enableImportMaintains = false;  // 导入维修项目信息
    @Getter
    @Setter
    private Boolean enableImportAddVehicles = false;    // 导入车辆信息批量增加
    @Getter
    @Setter
    private Boolean enableImportModifyVehicles = false;   // 导入车辆信息批量修改
    @Getter
    @Setter
    private Boolean enableImportAddParts = false;
    @Getter
    @Setter
    private Boolean enableImportModifyParts = false;

    /**
     * 用于显示编辑对话框
     */
//    @Getter
//    @Setter
//    protected Window formDialog;

    @Getter
    @Setter
    protected DocStatus selectedStatus = DocStatus.ALL;    //单据全选状态

    @Getter
    @Setter
    private List<DocStatus> documentStatuses = DocStatus.getListWithAll();   //服务单状态

    @Init(superclass = true)
    public void flowListInit() {
        pageSize = CommonHelper.baseGridHeight / 27;
        pageParam.setPageSize(pageSize);
        List<TaskInfo> waitingTasks = processService.findWaitingTasks();
        for (TaskInfo task : waitingTasks) {
            mapTasks.put(task.getProcessInstanceId(), task);
        }
        List<UserInfo> userList = userService.findAll();
        mapUsers.clear();
        for (UserInfo entity : userList) {
            mapUsers.put(entity.getLogId(), entity.getName());
        }

    }

    /**
     * 根据状态索引，获取状态的名称
     *
     * @param index
     * @return
     */
    public String getStatusName(Integer index) {
        if (index != null) {
            return DocStatus.getName(index);
        }
        return null;
    }

    /**
     * 刷新--当前页
     *
     * @param item 实体对象
     */
    public void refreshPage(T item) {
        //当前页
        pageParam.setPage(pageResult.getPage());
//        pageParam.setPageSize(pageResult.getPageSize());
        pageParam.setPageSize(CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT);
        //实体参数
        pageParam.setInfoWhere(item);
    }

    /**
     * 刷新--当前页
     *
     * @param item     实体对象
     * @param order    type: asc desc
     * @param orderKey 排序字段
     */
    public void refreshPage(T item, Order order, String orderKey) {
        //当前页
        pageParam.setPage(pageResult.getPage());
//        pageParam.setPageSize(pageResult.getPageSize());
        pageParam.setPageSize(CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT);
        //排序
        setSort(order, orderKey);
        //实体参数
        pageParam.setInfoWhere(item);
    }

    /**
     * 刷新--首页
     *
     * @param item 实体对象
     */
    public void refreshFirstPage(T item) {
        //当前页
        pageParam = new PageParam<>();
        pageParam.setPageSize(CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT);
        //实体参数
        pageParam.setInfoWhere(item);
    }

    /**
     * 刷新--首页
     *
     * @param item     实体对象
     * @param order    type: asc desc
     * @param orderKey 排序字段
     */
    public void refreshFirstPage(T item, Order order, String orderKey) {
        //当前页
        pageParam = new PageParam<>();
        pageParam.setPageSize(CommonHelper.baseGridHeight / CommonHelper.GRID_LINE_HEIGHT);
        //排序
        setSort(order, orderKey);
        //实体参数
        pageParam.setInfoWhere(item);
    }


    protected void setSort(Order order, String orderKey) {
        if (StringUtils.isNotBlank(orderKey) && StringUtils.isNotBlank(orderKey)) {
            pageParam.setOrder(order.toString());
            pageParam.setOrderName(orderKey);
        }
    }

    /**
     * 弹窗
     *
     * @param objId
     * @param url
     */
    @Command
    public void openForm(@BindingParam("objId") String objId, @BindingParam("url") String url, @BindingParam("title") String title) {

//        if (url == null || url == "") {
//            ZkUtils.showError("URL为空！", "系统异常");
//            return;
//        }
//
        Map<String, Object> paramMap = new HashMap<>();
        if (objId != null) {
            paramMap.put("objId", objId);
            paramMap.put("businessId", objId);
        }
        try {
//            ZkTabboxUtil.newTab(objId == null ? URLEncoder.encode(title, "UTF-8") : objId, title + "-" + (objId == null ? "新增" : "编辑"), "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            ZkTabboxUtil.newTab(objId == null ? URLEncoder.encode(title, "UTF-8") : objId, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (TabDuplicateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 关闭窗体
     */
    public void closeDialog() {
//        if (formDialog != null) {
//            formDialog.detach();
//        }
    }


    /**
     * 获取处理人
     *
     * @param processInstanceId
     * @return
     */
    public String getCurrentNode(String processInstanceId) {
        TaskInfo task = mapTasks.get(processInstanceId);
        if (task == null)
            return "";
        if (task.getAssignee() != null && !task.getAssignee().toString().trim().equals("")) {
            return mapUsers.get(task.getAssignee()) == null ? task.getName() : mapUsers.get(task.getAssignee());
        } else {
            return task.getName();
        }
    }

    /**
     * 搜索车辆
     */
    @Command
    @NotifyChange("vehicles")
    public void searchVehicles() {
        if (this.getKeyword().trim().length() >= CommonHelper.FILTER_VEHICLE_LEN) {
            this.vehicles = vehicleService.findAllByKeyword(this.getKeyword().trim());
        } else {
            ZkUtils.showInformation(CommonHelper.FILTER_VEHICLE_ERROR, "提示");
        }
    }


    /**
     * 当前选中的实体对象(list列表)
     */
    //@Getter
    //@Setter
    //protected T selectedEntity;

    ///**
    // * 刷新选中实体并关闭窗口
    // * @param entity
    // */
    //public void refreshEntity(T entity) {
    //    //BeanUtils.copyProperties(entity, this.selectedEntity);
    //    if (formDialog != null) {
    //        formDialog.detach();
    //    }
    //}


    /**
     * 状态颜色
     *
     * @param colorIdx
     * @return
     */
    public String getColor(Integer colorIdx) {
        if (colorIdx == null) {
            return "";
        } else if (colorIdx == DocStatus.WAITING_SETTLE.getIndex()) {
            return "color:#F2DA0C;font-weight:700";
        } else if (colorIdx == DocStatus.SETTLING.getIndex()) {
            return "color:#D47616 ;font-weight:700";
        } else if (colorIdx == DocStatus.SETTLED.getIndex()) {
            return "color:#7BC144;font-weight:700";
        } else if (colorIdx == DocStatus.DRAFT.getIndex()) {
            return "color:#C7DD0E;font-weight:700";
        } else if (colorIdx == DocStatus.AUDITING.getIndex()) {
            return "color:#FF9900;font-weight:700";
        } else if (colorIdx == DocStatus.CLOSED.getIndex()) {
            return "color:#cccccc;font-weight:700";
        } else if (colorIdx == DocStatus.REJECT.getIndex()) {
            return "color:#FF3333;font-weight:700";
        } else if (colorIdx == DocStatus.WITHDRAW.getIndex()) {
            return "color:#FF3333;font-weight:700";
        } else if (colorIdx == DocStatus.SUSPEND.getIndex()) {
            return "color:#B20825;font-weight:700";
        } else if (colorIdx == DocStatus.OBSOLETE.getIndex()) {
            return "color:#999999;font-weight:700";
        } else {
            return "";
        }
    }

}
