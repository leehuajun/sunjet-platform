package com.sunjet.frontend.vm.system;

import com.sunjet.dto.system.admin.IconInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.frontend.service.system.IconsService;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统图标
 *
 * @author zyf
 * @create 2017-7-13 上午11:01
 */
public class IconListVM extends ListVM<IconInfo> {

    @WireVariable
    private IconsService iconsService;

    @Getter
    @Setter
    private IconInfo iconInfo = new IconInfo();
    @Getter
    @Setter
    private List<IconInfo> infos = new ArrayList<>();
    @Getter
    @Setter
    private List<IconInfo> all = new ArrayList<>();

    @Init
    public void init() {
        all = iconsService.findAll();
        infos = all;
    }


    /**
     * 刷新
     */
//    @Command
//    @NotifyChange("infos")
//    public void refreshData(){
//        this.infos = this.all.stream()
//                .filter(info->info.getName().toUpperCase().contains(this.keyword.toUpperCase()))
//                .collect(Collectors.toList());
//    }
}
