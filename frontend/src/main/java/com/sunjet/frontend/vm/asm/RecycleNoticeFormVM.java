package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.system.base.FlowDocInfo;
import com.sunjet.frontend.service.basic.PartService;
import com.sunjet.frontend.service.recycle.RecycleNoticeItemService;
import com.sunjet.frontend.service.recycle.RecycleNoticeService;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.BindUtilsExt;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.DocStatus;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.sunjet.frontend.utils.common.CommonHelper.FILTER_PARTS_ERROR;
import static com.sunjet.frontend.utils.common.CommonHelper.FILTER_PARTS_LEN;


/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 故障件返回通知单 表单 VM
 */
public class RecycleNoticeFormVM extends FormVM {

    @WireVariable
    private RecycleNoticeService recycleNoticeService;
    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;
    @WireVariable
    private PartService partService;


    @Getter
    @Setter
    private RecycleNoticeInfo recycleNoticeInfo = new RecycleNoticeInfo();  //故障件返回通知单vo
    @Getter
    @Setter
    private RecycleNoticeItemInfo recycleNoticeItemInfo;    //故障件返回通知单子行vo
    @Getter
    @Setter
    private List<RecycleNoticeItemInfo> recycleNoticeItemInfoList;  //故障件返回通知列表
    @Getter
    @Setter
    private List<PartInfo> parts = new ArrayList<>();   //配件列表
    @Getter
    @Setter
    private DealerInfo dealerInfo;  //服务站
    @Getter
    @Setter
    private List<DealerInfo> dealerInfoList = new ArrayList<>();    //服务站列表
    @Getter
    @Setter
    private RecycleNoticeItemInfo recycleNoticePart;


    @Init(superclass = true)
    public void init() {

        if (StringUtils.isNotBlank(objId)) {
            recycleNoticeInfo = recycleNoticeService.findOneById(objId);

            if (recycleNoticeItemInfoList == null && recycleNoticeInfo.getObjId() != null) {
                recycleNoticeItemInfoList = recycleNoticeItemService.findByNoticeId(recycleNoticeInfo.getObjId());
                recycleNoticeInfo.setRecycleNoticeItemInfoList(recycleNoticeItemInfoList);

            }
        } else {
            recycleNoticeInfo = new RecycleNoticeInfo();
        }
        setActiveUserMsg(this.recycleNoticeInfo);
    }


    @Command
    @NotifyChange("*")
    public void submit() {
        //当主表非首次保存时，子表需要手工关联主表
        this.recycleNoticeInfo = this.recycleNoticeService.save(this.recycleNoticeInfo);
        this.updateUIState();
        showDialog();
    }

    /**
     * 添加物料
     */
    @Command
    @NotifyChange("recycleNoticeInfo")
    public void addPart() {
        if (recycleNoticeItemInfoList == null) {
            recycleNoticeItemInfoList = new ArrayList<>();
        }
        recycleNoticeItemInfo = new RecycleNoticeItemInfo();
        if (recycleNoticeItemInfo.getAmount() == 0) {
            recycleNoticeItemInfo.setAmount(1);
        }
        this.recycleNoticeInfo.getRecycleNoticeItemInfoList().add(recycleNoticeItemInfo);
    }


    /**
     * 选中故障零件
     *
     * @param part
     */
    @Command
    public void selectRecycleNoticePart(@BindingParam("model") RecycleNoticeItemInfo part) {
        this.recycleNoticePart = part;
    }


    /**
     * 查询配件列表
     */
    @Command
    @NotifyChange("parts")
    public void searchParts() {
        if (this.keyword.trim().length() >= FILTER_PARTS_LEN) {
            this.parts = partService.findAllByKeyword(this.keyword.trim());
            if (this.parts.size() < 1) {
                ZkUtils.showInformation("未查询到相关物料！", "提示");
            }
        } else {
            ZkUtils.showInformation(FILTER_PARTS_ERROR, "提示");
            return;
        }

    }

    /**
     * 选中配件
     *
     * @param partInfo
     */
    @Command
    @NotifyChange("*")
    public void selectPart(@BindingParam("model") PartInfo partInfo) {
        recycleNoticePart.setPartCode(partInfo.getCode());
        recycleNoticePart.setPartName(partInfo.getName());
        this.keyword = "";
        this.parts.clear();
    }

    @Command
    @NotifyChange("*")
    public void deletePart(@BindingParam("model") RecycleNoticeItemInfo recycleNoticeItemInfo) {
        recycleNoticeItemInfoList.remove(recycleNoticeItemInfo);
        this.recycleNoticeInfo.getRecycleNoticeItemInfoList().remove(recycleNoticeItemInfo);
        if (StringUtils.isNotBlank(recycleNoticeItemInfo.getObjId())) {
            recycleNoticeItemService.delete(recycleNoticeItemInfo.getObjId());
        }
    }

    /**
     * 初始化后加载窗体
     *
     * @param view
     */
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view) {
        Selectors.wireComponents(view, this, false);
        //win = (org.zkoss.zul.Window) view;
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

    @Command
    @NotifyChange("recycleNoticeInfo")
    public void selectDealer(@BindingParam("model") DealerInfo dealer) {
        this.dealerInfo = dealer;
        this.recycleNoticeInfo.setDealerCode(dealer.getCode());
        this.recycleNoticeInfo.setDealerName(dealer.getName());
        this.keyword = "";
    }


    /**
     * 提交,启动流程
     */
    @Command
    @NotifyChange("*")
    public void startProcess() {
        ZkUtils.showQuestion("是否已经确认并提交此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                if (startProcessInstance(this.recycleNoticeInfo)) {
                    this.recycleNoticeInfo = recycleNoticeService.save(this.recycleNoticeInfo);
                    flowDocInfo = this.recycleNoticeInfo;
                    Map<String, String> map = recycleNoticeService.startProcess(this.recycleNoticeInfo, getActiveUser());
                    ZkUtils.showInformation(map.get("message"), map.get("result"));
                    if ("提交成功".equals(map.get("message"))) {
                        this.canEdit = false;
                        this.readonly = true;
                        this.canShowFlowImage = true;
                    }
                    this.flowDocInfo = findInfoById(flowDocInfo.getObjId());
                    this.updateUIState();
                }

            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消提交", "提示");
            }
        });

    }

    @Override
    protected Boolean checkValid() {
        //if (this.recycleNotice.getStatus() > 0) {
        //    ZkUtils.showInformation("单据状态非[" + this.getStatusName(0) + "]状态,不能保存！", "提示");
        //    return false;
        //}
        if (this.recycleNoticeInfo.getDealerCode() == null) {
            ZkUtils.showInformation("请选择服务站！", "提示");
            return false;
        }
        if (this.recycleNoticeInfo.getRecycleNoticeItemInfoList().size() < 1) {
            ZkUtils.showInformation("请选择物料！", "提示");
            return false;
        }

        return true;
    }


    /**
     * 作废单据
     */
    @Command
    public void desertTask() {
        ZkUtils.showQuestion("是否作废此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                recycleNoticeService.desertTask(this.recycleNoticeInfo.getObjId());
                canHandleTask = false;
                canDesertTask = false;
                canEdit = false;
                this.updateUIState();
                showDialog();
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消作废", "提示");
            }
        });
    }


    /**
     * 保存实体
     *
     * @param flowDocInfo
     * @return
     */
    @Override
    protected FlowDocInfo saveInfo(FlowDocInfo flowDocInfo) {
        return recycleNoticeService.save((RecycleNoticeInfo) flowDocInfo);
    }

    @Override
    protected FlowDocInfo findInfoById(String objId) {
        return recycleNoticeService.findOneById(objId);
    }

    @Override
    protected void updateUIState() {
        BindUtilsExt.postNotifyChange(null, null, this,
                Arrays.asList("recycleNoticeInfo", "handle", "canHandleTask", "canDesertTask", "readonly", "canEdit", "canShowFlowImage"));
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_RECYCLE_NOTICE_LIST, null);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.LIST_TASK, null);
    }

    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm() {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        try {

            String srcDocID = this.recycleNoticeInfo.getSrcDocID();
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            }
            String title = this.recycleNoticeInfo.getSrcDocType();
            if ("三包服务单".equals(title)) {
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动服务单".equals(title)) {
                url = "/views/asm/activity_maintenance_form.zul";
            }

            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
