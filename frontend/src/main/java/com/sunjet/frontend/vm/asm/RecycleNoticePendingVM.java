package com.sunjet.frontend.vm.asm;

import com.sunjet.dto.asms.asm.WarrantyMaintenanceInfo;
import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticeItemInfo;
import com.sunjet.dto.asms.recycle.RecycleNoticePendingItem;
import com.sunjet.dto.system.base.Order;
import com.sunjet.frontend.service.asm.WarrantyMaintenanceService;
import com.sunjet.frontend.service.recycle.RecycleNoticeItemService;
import com.sunjet.frontend.service.recycle.RecycleNoticeService;
import com.sunjet.frontend.utils.exception.TabDuplicateException;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import com.sunjet.utils.common.DateHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhj
 * @create 2015-12-30 上午11:38
 * <p>
 * 待返回故障件列表 VM
 */
public class RecycleNoticePendingVM extends ListVM<RecycleNoticePendingItem> {

    @WireVariable
    private RecycleNoticeService recycleNoticeService;
    @WireVariable
    private RecycleNoticeItemService recycleNoticeItemService;
    @WireVariable
    private WarrantyMaintenanceService warrantyMaintenanceService;
    //@WireVariable
    //private DealerService dealerService;

    @Getter
    @Setter
    private RecycleNoticePendingItem recycleNoticePendingItem = new RecycleNoticePendingItem();     //待返回故障件清单VO
    @Getter
    @Setter
    private RecycleNoticeItemInfo recycleNoticePart = new RecycleNoticeItemInfo();      //故障件返回通知单子行VO
    @Getter
    @Setter
    private boolean choice;
    @Getter
    @Setter
    private Window window;
    @Getter
    @Setter
    private List<RecycleNoticePendingItem> recycleNoticePendingItems = new ArrayList<>();
    @Getter
    @Setter
    private Map<String, RecycleNoticePendingItem> recycleNoticeItemInfoMap = new HashMap<>();

    @Getter
    @Setter
    private String vin;

    @Init(superclass = true)
    public void init() {
        this.setTitle("待返回清单");
        this.setEnableAdd(false);
        if ("admin".equals(getActiveUser().getLogId())) {
            this.setEnableAddRecycleDoc(false);
        } else {
            this.setEnableAddRecycleDoc(hasPermission("RecycleEntity:create"));
        }
        if (getActiveUser().getDealer() != null) {
            recycleNoticePendingItem.setDealerCode(getActiveUser().getDealer().getCode());
            recycleNoticePendingItem.setDealerName(getActiveUser().getDealer().getName());
        }

        //判断权限
        if (getActiveUser().getDealer() != null) {
            //服务站
            recycleNoticePendingItem.setDealerCode(getActiveUser().getDealer().getCode());
        }

        refreshFirstPage(recycleNoticePendingItem, Order.DESC, "createdTime");
        getPageList();

    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = recycleNoticeItemService.getPageList(pageParam);
    }

    /**
     * 查询下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        refreshPage(recycleNoticePendingItem);
        getPageList();
    }

    /**
     * 刷新/ 查询
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_RECYCLE_NOTICE_PENDING)
    @Command
    @NotifyChange("pageResult")
    public void refreshData() {
        if (this.recycleNoticePendingItem.getEndDate().getTime() <= this.recycleNoticePendingItem.getStartDate().getTime()) {
            ZkUtils.showError("日期选择错误！ 结束时间必须大于等于开始时间.", "参数错误");
            return;
        }
        this.recycleNoticePendingItem.getObjIds().clear();
        //vin不为null
        if (StringUtils.isNotBlank(vin)) {
            List<String> objIds = recycleNoticeItemService.findAllRecycleItemsObjIdByVin(vin);
            if (objIds != null && objIds.size() > 0) {
                this.recycleNoticePendingItem.setObjIds(objIds);
            }
        }

        refreshFirstPage(recycleNoticePendingItem);
        getPageList();
    }

    @Command
    @NotifyChange("recycleNoticePendingItem")
    public void selectDealer(@BindingParam("model") DealerInfo model) {
        recycleNoticePendingItem.setDealerCode(model.getCode());
        recycleNoticePendingItem.setDealerName(model.getName());

    }


    /**
     * 重置搜索条件
     */
    @Command
    @NotifyChange("recycleNoticePendingItem")
    public void reset() {
        if (getActiveUser().getDealer() == null) {
            this.recycleNoticePendingItem.setDealerCode("");
            this.recycleNoticePendingItem.setDealerName("");
        }

        this.getDealers().clear();
        this.recycleNoticePendingItem.setEndDate(DateHelper.getEndDateTime());
        this.recycleNoticePendingItem.setStartDate(DateHelper.getFirstOfYear());
        this.recycleNoticePendingItem.setDocNo("");
        this.recycleNoticePendingItem.setSrcDocNo("");
        this.recycleNoticePendingItem.setPartName("");
        this.recycleNoticePendingItem.setPartCode("");
    }

    /**
     * 全选
     *
     * @param chk
     */
    @Command
    @NotifyChange({"pageResult", "choice"})
    public void checkAll(@BindingParam("chk") Boolean chk) {
        List<RecycleNoticePendingItem> pageContent = pageResult.getRows();
        if (chk == true) {
            for (RecycleNoticePendingItem recycleNoticePendingItem : pageContent) {

                if (this.recycleNoticeItemInfoMap.get(recycleNoticePendingItem.getObjId()) == null) {
                    //this.recycleNoticeItems.add(recycleNoticeItem);
                    this.recycleNoticeItemInfoMap.put(recycleNoticePendingItem.getObjId(), recycleNoticePendingItem);
                }

            }
        } else {
            if (pageContent != null) {
                for (RecycleNoticePendingItem recycleNoticePendingItem : pageContent) {

                    if (this.recycleNoticeItemInfoMap.get(recycleNoticePendingItem.getObjId()) != null) {
                        //this.recycleNoticeItems.remove(recycleNoticeItem);
                        this.recycleNoticeItemInfoMap.remove(recycleNoticePendingItem.getObjId());
                    }

                }
            }

        }
        this.choice = chk;
    }


    /**
     * /选择待返清单
     */
    @Command
    public void selectRecycleNoticePending(@BindingParam("model") RecycleNoticePendingItem recycleNoticeItem, @BindingParam("check") boolean check) {
        boolean result = true;
        if (check == result && recycleNoticeItem != null) {

            recycleNoticeItemInfoMap.put(recycleNoticeItem.getObjId(), recycleNoticeItem);
        } else {
            recycleNoticeItemInfoMap.remove(recycleNoticeItem.getObjId());
        }
    }

    /**
     * /判断是否存在
     */
    public Boolean chkIsExist(RecycleNoticePendingItem recycleNoticeItemInfo) {

        if (this.recycleNoticeItemInfoMap.get(recycleNoticeItemInfo.getObjId()) != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 创建故障件返回单
     */
    @Command
    @NotifyChange({"pageResult", "choice"})
    public void createRecycleDoc() {
        if (recycleNoticeItemInfoMap.isEmpty()) {
            ZkUtils.showInformation("没有选择物料！", "提示");
        } else {

            Map<String, List<RecycleNoticePendingItem>> paramMap = new HashMap<>();
            //map.put("recycleNoticeItem", recycleNoticeItems);
            for (String key : recycleNoticeItemInfoMap.keySet()) {
                //map.keySet()返回的是所有key的值
                RecycleNoticePendingItem recycleNoticeItemEntity = recycleNoticeItemInfoMap.get(key);
                this.recycleNoticePendingItems.add(recycleNoticeItemEntity);
            }
            String url = "/views/asm/recycle_form.zul";
            paramMap.put("recycleNoticeItem", this.recycleNoticePendingItems);

            try {
                ZkTabboxUtil.newTab(URLEncoder.encode(this.getTitle(), "UTF-8"), "故障件返回单", "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
            } catch (TabDuplicateException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (this.recycleNoticePendingItems != null) {
            this.recycleNoticePendingItems.clear();
        }
        this.choice = false;
        recycleNoticeItemInfoMap.clear();
    }

    /**
     * 打开对应的单据
     */
    @Command
    public void openSrcDocForm(@BindingParam("entity") RecycleNoticePendingItem recycleNoticePendingItem) {
        Map<String, Object> paramMap = new HashMap<>();
        String url = "";
        String srcDocID = "";
        try {
            String title = recycleNoticePendingItem.getSrcDocType();
            if ("首保服务单".equals(title)) {
                url = "/views/asm/first_maintenance_form.zul";
                ZkUtils.showInformation("功能开发中", "提示");
                return;
            } else if ("三包服务单".equals(title)) {
                WarrantyMaintenanceInfo warrantyMaintenanceInfo = warrantyMaintenanceService.findOneWithOthersBySrcDocNo(recycleNoticePendingItem.getSrcDocNo());
                srcDocID = warrantyMaintenanceInfo.getObjId();
                url = "/views/asm/warranty_maintenance_form.zul";
            } else if ("活动服务单".equals(title)) {
                ZkUtils.showInformation("功能开发中", "提示");
                url = "/views/asm/activity_maintenance_form.zul";
                return;
            }
            if (StringUtils.isNotBlank(srcDocID)) {
                paramMap.put("objId", srcDocID);
                paramMap.put("businessId", srcDocID);
            } else {
                return;
            }
            ZkTabboxUtil.newTab(srcDocID == null ? URLEncoder.encode(title, "UTF-8") : srcDocID, title, "", true, ZkTabboxUtil.OverFlowType.AUTO, url, paramMap);
        } catch (TabDuplicateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
