package com.sunjet.frontend.vm.system;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.system.admin.MessageInfo;
import com.sunjet.frontend.service.system.MessageService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * @author 李華軍
 * @create 2017-10-18 上午11:58
 */
public class MessageListVM extends ListVM<MessageInfo> {


    @WireVariable
    MessageService messageService;

    @Setter
    @Getter
    private MessageInfo messageInfo = new MessageInfo();
    @Setter
    @Getter
    private String isRead = "全部";

    @Init
    public void init() {
        this.setTitle("消息通知");
//        this.setFormUrl("/views/basic/notice_form.zul");
        DealerInfo dealer = getActiveUser().getDealer();
        if (dealer != null) {
            this.messageInfo.setOrgId(dealer.getObjId());
            //this.messageInfo.setIsRead(null);
        } else {
            this.messageInfo.setLogId(getActiveUser().getLogId());
            //this.messageInfo.setIsRead(null);
        }
        refreshFirstPage(this.messageInfo);
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
        refreshPage(this.messageInfo);
        //刷新分页
        getPageList();
    }


    /**
     * 分页
     */
    @Command
    public void getPageList() {
        pageResult = messageService.getPageList(pageParam);
    }


    /**
     * 刷新
     */
    @Command
    @NotifyChange("pageResult")
    @GlobalCommand(GlobalCommandValues.REFRESH_MESSAGE_LIST)
    public void refreshData() {
        //设置分页参数
        refreshFirstPage(this.messageInfo);
        //刷新分页
        getPageList();
    }

    /**
     * @param objId
     * @param url
     * @param title
     */
    @Override
    @Command
    public void openForm(@BindingParam("objId") String objId, @BindingParam("url") String url, @BindingParam("title") String title) {
        this.messageInfo = messageService.findOne(objId);
        this.messageInfo.setIsRead(true);
        this.messageInfo = messageService.save(this.messageInfo);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_MESSAGE_LIST, null);
        super.openForm(this.messageInfo.getRefId(), url, title);
    }

    /**
     * 选择服务站等级
     */
    @Command
    @NotifyChange()
    public void selectIsRead() {

        if (isRead.equals("已读")) {
            this.messageInfo.setIsRead(true);
        } else if (isRead.equals("未读")) {
            this.messageInfo.setIsRead(false);
        } else {
            this.messageInfo.setIsRead(null);
        }
    }

}
