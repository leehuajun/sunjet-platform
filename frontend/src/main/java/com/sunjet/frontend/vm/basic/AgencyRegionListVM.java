package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.AgencyInfo;
import com.sunjet.dto.asms.basic.AgencyItem;
import com.sunjet.dto.asms.basic.ProvinceInfo;
import com.sunjet.frontend.service.basic.AgencyService;
import com.sunjet.frontend.service.basic.RegionService;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 合作商覆盖省份
 * @author zyf
 * @create 2017-7-13 上午10:48
 */
public class AgencyRegionListVM extends ListVM<AgencyItem> {

    @WireVariable
    private AgencyService agencyService;

    @WireVariable
    private RegionService regionService;


    @Getter
    @Setter
    private List<AgencyInfo> agencyInfoLists = new ArrayList<>();   // 合作商列表
    @Getter
    @Setter
    private AgencyInfo selectAgency = new AgencyInfo();
    @Getter
    @Setter
    private List<ProvinceInfo> provinceInfoList = new ArrayList<>();
    @Getter
    @Setter
    private List<ProvinceInfo> selectProvinceInfoList = new ArrayList<>();
    @Getter
    @Setter
    private Boolean canModify = false;

    @Init
    public void init() {
        this.setCanModify(hasPermission("AgencyWithRegionEntity:modify"));

        //拿到所有启用状态的合作商
        List<AgencyInfo> enabled = agencyService.findEnabled();
        if (enabled != null && enabled.size() > 0) {
            for (AgencyInfo agencyInfoList : enabled) {
                if (StringUtils.isNotBlank(agencyInfoList.getCode()) && StringUtils.isNotBlank(agencyInfoList.getName())) {
                    agencyInfoLists.add(agencyInfoList);
                }
            }
        }

    }


    /**
     * 选择合作商
     *
     * @param agencyInfo
     */
    @Command
    @NotifyChange("provinceInfoList")
    public void onClickSelectAgency(@BindingParam("model") AgencyInfo agencyInfo) {

        provinceInfoList.clear();
        selectProvinceInfoList.clear();
        selectAgency = agencyInfo;

        //查询所有省份
        provinceInfoList = regionService.findAllProvince();

        selectAgency = agencyService.findOneProvincesById(agencyInfo.getObjId());

        //拿到覆盖的省份
        if (selectAgency.getProvinces() != null && selectAgency.getProvinces().size() > 0) {
            selectProvinceInfoList.addAll(selectAgency.getProvinces());
        }

    }

    /**
     * 选择省份
     *
     * @param provinceInfo
     * @param check
     */
    @Command
    public void selectProvince(@BindingParam("entity") ProvinceInfo provinceInfo, @BindingParam("check") Boolean check) {

        if (check) {
            selectAgency.getProvinces().add(provinceInfo);
        } else {
            selectAgency.getProvinces().remove(provinceInfo);
        }
    }

    /**
     * 检查覆盖的省份是否已选
     *
     * @param provinceInfo
     * @return
     */
    public Boolean checkedProvinceInfo(ProvinceInfo provinceInfo) {

        for (ProvinceInfo province : selectProvinceInfoList) {
            if (provinceInfo.getObjId().equals(province.getObjId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存
     */
    @Command
    public void submit() {
        agencyService.save(selectAgency);
        showDialog();
    }


}