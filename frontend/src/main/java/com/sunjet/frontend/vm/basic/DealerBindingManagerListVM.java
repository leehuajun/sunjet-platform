package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.DealerInfo;
import com.sunjet.dto.asms.basic.DealerItem;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.dto.system.admin.UserInfo;
import com.sunjet.frontend.service.basic.DealerService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.service.system.RoleService;
import com.sunjet.frontend.service.system.UserService;
import com.sunjet.frontend.utils.zk.GlobalCommandValues;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务站 列表
 * Created by Administrator on 2017/7/13.
 */
public class DealerBindingManagerListVM extends ListVM {

    @WireVariable
    private DealerService dealerService;
    @WireVariable
    private RegionService regionService;
    @WireVariable
    private UserService userService;
    @WireVariable
    private RoleService roleService;
    @Getter
    @Setter
    private List<ProvinceInfo> provinceEntities = new ArrayList<>();  // 省份/直辖市 集合
    @Getter
    @Setter
    private ProvinceInfo selectedProvince;        // 选中的 省份/直辖市

    @Getter
    @Setter
    private DealerItem dealerItem = new DealerItem();

    @Getter
    @Setter
    private List<UserInfo> userInfoList = new ArrayList<>();

    @Getter
    @Setter
    private List<DealerInfo> dealerInfoList = new ArrayList<>();

    @Getter
    @Setter
    private UserInfo selectUser = new UserInfo();
    @Getter
    @Setter
    private Map<String, DealerInfo> selectDealer = new HashMap<>();

    @Getter
    @Setter
    private Boolean canModify = false;


    @Init
    public void Init() {
        this.setCanModify(hasPermission("DealerEntity:modify"));
        //获取绑定服务经理的服务站
        userInfoList = userService.findAllByRoleName("服务经理");
        provinceEntities = regionService.findAllProvince();
    }


    /**
     * 关闭窗口
     */
    @GlobalCommand(GlobalCommandValues.REFRESH_DEALER_LIST)
    @NotifyChange("pageResult")
    public void refreshList() {
        this.closeDialog();
    }


    @Command
    @NotifyChange("dealerInfoList")
    public void selectUser(@BindingParam("model") UserInfo user) {
        selectUser = user;
        selectDealer.clear();
        dealerInfoList = dealerService.findAllByServiceManagerId(selectUser.getObjId());
        dealerInfoList.addAll(dealerService.findAllNotServiceManager());

    }

    /**
     * 检查服务站服务经理是否已选择
     *
     * @param dealerInfo
     * @return
     */
    public Boolean checkedDealerInfo(DealerInfo dealerInfo) {
        if (StringUtils.isNotBlank(dealerInfo.getServiceManagerId())) {
            return true;
        }
        return false;
    }


    /**
     * 选择服务站
     *
     * @param dealerInfo
     * @param check
     */
    @Command
    public void selectDealer(@BindingParam("entity") DealerInfo dealerInfo, @BindingParam("check") Boolean check) {
        if (!check) {
            dealerInfo.setServiceManagerId(null);
            dealerInfo.setServiceManagerName(null);
            selectDealer.put(dealerInfo.getObjId(), dealerInfo);
        } else {
            dealerInfo.setServiceManagerId(selectUser.getObjId());
            dealerInfo.setServiceManagerName(selectUser.getName());
            selectDealer.put(dealerInfo.getObjId(), dealerInfo);
        }
    }

    /**
     * 保存
     */
    @Command
    public void submit() {
        for (Map.Entry<String, DealerInfo> stringDealerInfoEntry : selectDealer.entrySet()) {
            DealerInfo value = stringDealerInfoEntry.getValue();
            dealerService.save(value);
        }
        selectDealer.clear();
        showDialog();
    }


    /**
     * 查询
     */
    @Command
    @NotifyChange({"dealerInfoList", "userInfoList"})
    public void refreshData() {
        //搜索服务站
        if (selectUser != null && StringUtils.isNotBlank(selectUser.getObjId())) {
            List<DealerInfo> dealerInfos = new ArrayList<>();
            dealerInfos.addAll(dealerService.findAllByServiceManagerId(selectUser.getObjId()));
            dealerInfos.addAll(dealerService.findAllNotServiceManager());
            this.dealerInfoList.clear();
            for (DealerInfo dealerInfo : dealerInfos) {
                boolean isOk = true;

                if (StringUtils.isNotBlank(dealerItem.getCode())) {
                    if (dealerItem.getCode().trim().equals(dealerInfo.getCode())) {
                        isOk = true;
                    } else {
                        isOk = false;
                    }
                }
                if (StringUtils.isNotBlank(dealerItem.getName())) {
                    if (dealerItem.getName().trim().equals(dealerInfo.getName())) {
                        isOk = true;
                    } else {
                        isOk = false;
                    }
                }
                if (selectedProvince != null) {
                    if (selectedProvince.getObjId().equals(dealerInfo.getProvinceId())) {
                        isOk = true;
                    } else {
                        isOk = false;
                    }
                }
                if (isOk) {
                    this.dealerInfoList.add(dealerInfo);
                }
            }
        }

        List<UserInfo> userList = new ArrayList<>();
        userList.addAll(userService.findAllByRoleName("服务经理"));
        if (StringUtils.isNotBlank(dealerItem.getServiceManagerName())) {
            userInfoList.clear();
            for (UserInfo userInfo : userList) {
                boolean isOk = true;
                if (dealerItem.getServiceManagerName().equals(userInfo.getName())) {
                    isOk = true;
                } else {
                    isOk = false;
                }
                if (isOk) {
                    this.userInfoList.add(userInfo);
                }
            }

        }
    }


    /**
     * 选择省份
     */
    @Command
    public void selectProvince(@BindingParam("event") Event event) {
        this.selectedProvince = ((Listitem) ((Listbox) event.getTarget()).getSelectedItem()).getValue();
    }

    /**
     * 重置查询条件
     */

    @Command
    @NotifyChange({"dealerItem", "selectedProvince"})
    public void reset() {
        this.dealerItem.setName("");
        this.dealerItem.setCode("");
        this.dealerItem.setServiceManagerName("");
        this.selectedProvince = null;
        Init();
    }


}
