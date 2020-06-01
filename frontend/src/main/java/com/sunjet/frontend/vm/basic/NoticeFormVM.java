package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.frontend.service.system.NoticeService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyf
 * @create 2017-7-13 上午11:58
 */
public class NoticeFormVM extends FormVM {
    @WireVariable
    NoticeService noticeService;

    @Getter
    @Setter
    private Boolean canEdit = false;


    @Getter
    @Setter
    private NoticeInfo notice = new NoticeInfo();

    @Init(superclass = true)
    public void init() {
        if (StringUtils.isNotBlank(objId)) {
            notice = noticeService.findOneById(objId);
        } else {
            notice = new NoticeInfo();
            canEdit = !canEdit;
        }


    }

    /**
     * 表单提交,保存信息
     */
    @Command
    @NotifyChange("*")
    public void submit() {
        this.notice.setPublishDate(new Date());
        this.notice.setPublisher(getActiveUser().getUsername());
        System.out.println(getActiveUser().getLogId());
        notice.setCreaterId(getActiveUser().getLogId());
        notice.setCreaterName(getActiveUser().getUsername());
        this.notice = noticeService.save(notice);
        if (StringUtils.isNotBlank(objId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity", notice);
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_NOTICE_LIST, map);

        } else {
            BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_NOTICE_LIST, null);
        }
        this.setCanEdit(false);
        showDialog();
    }

    @Command
    @NotifyChange("*")
    public void changeStatus() {
        if (this.notice.getPublisher() != null && this.notice.getPublisher().equals(getActiveUser().getUsername())) {
            this.setReadonly(false);
            this.setCanEdit(!canEdit);
        } else {
            ZkUtils.showError("非发布人不能编辑", "提示");
        }
    }

    /**
     * 上传文件
     *
     * @param event
     */
    @Command
    @NotifyChange("*")
    public void doUploadFile(@BindingParam("event") UploadEvent event) {
        String fileOriginName = event.getMedia().getName();
        String fileRealName = ZkUtils.onUploadFile(event.getMedia(), getFilePath() + CommonHelper.UPLOAD_DIR_NOTICE);
        this.notice.setFileOriginName(fileOriginName);
        this.notice.setFileRealName(fileRealName);
    }

    /**
     * 查看上传图片
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_NOTICE + filename;
    }

    @Command
    @NotifyChange("notice")
    public void deleteFile() {
        this.notice.setFileRealName("");
        this.notice.setFileOriginName("");
    }

    /**
     * 检查编辑按钮
     *
     * @return
     */
    public Boolean checkEdit() {
        if (this.notice.getPublisher() != null && this.notice.getPublisher().equals(getActiveUser().getUsername()))
            return true;
        return false;

    }


}
