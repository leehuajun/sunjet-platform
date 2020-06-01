package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.frontend.service.system.NoticeService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkTabboxUtil;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

import java.util.List;

/**
 * @author zyf
 * @create 2017-7-13 上午11:58
 */
public class NoticeListVM extends ListVM<NoticeInfo> {


    @WireVariable
    NoticeService noticeService;
    @Setter
    @Getter
    private NoticeInfo noticeInfo;

    @Init
    public void init() {
        this.setEnableAdd(hasPermission("NoticeEntity:create"));
        this.setEnableDelete(hasPermission("NoticeEntity:delete"));
        this.setTitle("公告");
        this.setFormUrl("/views/basic/notice_form.zul");
        refreshFirstPage(noticeInfo);
        getPageList();
    }


    /**
     * 点击下一页
     *
     * @param event
     */
    @Command
    @NotifyChange("pageResult")
    public void gotoPageNo(@BindingParam("e") Event event) {
        this.setTitle("公告");
        //设置分页参数
        refreshPage(noticeInfo);
        //刷新分页
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
//        setSort(Order.DESC,"isTop");
        pageResult = noticeService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    @GlobalCommand(GlobalCommandValues.REFRESH_NOTICE_LIST)
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(noticeInfo);
        //刷新分页
        getPageList();
    }

    /**
     * 删除
     *
     * @param objId
     */
    @Command
    @NotifyChange("pageResult")
    public void deleteEntity(@BindingParam("objId") String objId, @BindingParam("tabs") List<Tab> tabList) {
        ZkUtils.showQuestion("如果此单据已经打开将会被关闭\n是否删除此单据？", "询问", event -> {
            int clickedButton = (Integer) event.getData();
            if (clickedButton == Messagebox.OK) {
                NoticeInfo noticeInfo = noticeService.findOneById(objId);
                if (noticeInfo.getCreaterId().equals(getActiveUser().getLogId())) {
                    ZkTabboxUtil.closeOneByObjId(tabList, objId);
                    noticeService.delete(objId);
                } else {
                    ZkUtils.showInformation("非创建人不能删除！", "提示");
                }
                getPageList();
                BindUtils.postNotifyChange(null, null, this, "pageResult");
            } else {
                // 用户点击的是取消按钮
                ZkUtils.showInformation("取消删除", "提示");
            }
        });

    }



}
