package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.utils.common.CommonHelper;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.FormVM;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 合作商  表单
 * Created by Administrator on 2017/7/13.
 */
public class AgencyFormVM extends FormVM {

    @WireVariable
    AgencyService agencyService;

    @Getter
    @Setter
    private AgencyInfo agencyInfo;
    @Getter
    @Setter
    private Boolean readonly;
    @Getter
    @Setter
    private List<Boolean> enableds = new ArrayList<>();   //启用状态列表
    @Getter
    @Setter
    private boolean selectEnabled;   // 是否启用
    @Getter
    @Setter
    private Boolean canModify = false;

    @Init(superclass = true)
    public void init() {
        this.setCanModify(hasPermission("AgencyEntity:modify"));
        enableds.add(true);
        enableds.add(false);

        if (StringUtils.isNotBlank(objId)) {
            agencyInfo = agencyService.findOne(objId);
            this.selectEnabled = agencyInfo.getEnabled();
        } else {
            agencyInfo = new AgencyInfo();
        }


    }


    /***
     * 保存实体
     */
    @Command
    public void saveAgency() {
        //if ("是".equals(this.getSelectEnabled())) {
        //    agencyInfo.setEnabled(true);
        //} else if ("否".equals(this.getSelectEnabled())) {
        //    agencyInfo.setEnabled(false);
        //} else {
        //    //默认设置
        agencyInfo.setEnabled(selectEnabled);
        //}

        agencyService.save(agencyInfo);
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_AGENCY_LIST, null);
        ZkUtils.showInformation("保存成功", "提示");
    }


    /**
     * 文件路径
     *
     * @param filename
     * @return
     */
    public String getFilePath(String filename) {
        return "files" + CommonHelper.UPLOAD_DIR_AGENCY + filename;
    }

}
