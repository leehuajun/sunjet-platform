package com.sunjet.frontend.vm.basic;


import com.sunjet.dto.asms.basic.PartInfo;
import com.sunjet.frontend.service.basic.PartService;
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
 * 配件目录编辑
 *
 * @author zyf
 * @create 2017-7-13 上午12:00
 */
public class PartFormVM extends FormVM {
    @WireVariable
    PartService partService;
    @Getter
    @Setter
    private PartInfo partInfo;   //配件实体

    @Getter
    @Setter
    private List<Boolean> enableds = new ArrayList<>();    //状态列表

    @Getter
    @Setter
    private Boolean enableUpdate = false;      // 编辑按钮状态

    @Getter
    @Setter
    private boolean selectEnabled;   // 是否启用

    @Init(superclass = true)
    public void init() {
        this.setEnableUpdate(hasPermission("PartEntity:modify"));
        enableds.add(true);
        enableds.add(false);
        if (StringUtils.isNotBlank(this.objId)) {
            partInfo = partService.findOne(this.objId);
            this.selectEnabled = partInfo.getEnabled();
        } else {
            partInfo = new PartInfo();
        }

    }


    /**
     * 保存配件
     */
    @Command
    public void savePart() {
        partInfo.setCode(StringUtils.upperCase(partInfo.getCode()));
        partInfo.setEnabled(selectEnabled);
        if (StringUtils.isBlank(partInfo.getObjId())) {
            PartInfo part = partService.findOneByCode(partInfo.getCode());
            if (part == null) {
                partService.save(partInfo);
            } else {
                ZkUtils.showInformation("配件编码重复", "提示");
                return;
            }
        } else {
            partService.save(partInfo);
        }
        showDialog();
        BindUtils.postGlobalCommand(null, null, GlobalCommandValues.REFRESH_PART_LIST, null);

    }

}
