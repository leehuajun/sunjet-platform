package com.sunjet.frontend.vm.basic;

import com.sunjet.dto.asms.basic.VehicleModelInfo;
import com.sunjet.dto.system.admin.DictionaryInfo;
import com.sunjet.frontend.service.basic.VehicleModelService;
import com.sunjet.frontend.service.system.DictionaryService;
import com.sunjet.frontend.utils.zk.ZkUtils;
import com.sunjet.frontend.vm.base.ListVM;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 车辆型号 列表
 * Created by Administrator on 2017/7/13.
 *
 * @author lhj
 */
public class VehicleModelListVM extends ListVM<VehicleModelInfo> {

    @WireVariable
    private VehicleModelService vehicleModelService;
    @WireVariable
    private DictionaryService dictionaryService;

    @Getter
    @Setter
    private String keyword;


    @Getter
    @Setter
    private VehicleModelInfo current = new VehicleModelInfo();

    @Getter
    private List<VehicleModelInfo> infos = new ArrayList<>();

    private List<VehicleModelInfo> originInfos = new ArrayList<>();

    @Getter
    private List<DictionaryInfo> vehicleTypes = new ArrayList<>();
    @Getter
    private List<DictionaryInfo> vehicleTypesForm = new ArrayList<>();
    @Getter
    @Setter
    private DictionaryInfo selectedVehicleType;
    @Getter
    @Setter
    private DictionaryInfo selectedVehicleTypeForm;
    @Getter
    @Setter
    private Boolean canAdd = false;
    @Getter
    @Setter
    private Boolean canSave = false;
    @Getter
    @Setter
    private Boolean canDelete = false;



    @Init(superclass = true)
    public void init() {
        this.setCanAdd(hasPermission("VehicleModelEntity:create"));
        this.setCanSave(hasPermission("VehicleModelEntity:modify"));
        this.setCanDelete(hasPermission("VehicleModelEntity:delete"));
        originInfos = vehicleModelService.findAll();
        infos = originInfos;
        vehicleTypes = dictionaryService.findDictionariesByParentCode("15000");
        vehicleTypes.forEach(item -> vehicleTypesForm.add(item));
        DictionaryInfo info = new DictionaryInfo();
        info.setName("--全部--");
        info.setCode("0");
        info.setValue("0");
        info.setSeq(0);
        this.selectedVehicleType = info;
        vehicleTypes.add(info);

        Collections.sort(vehicleTypes, (o1, o2) -> {
            if (Integer.parseInt(o1.getValue()) > Integer.parseInt(o2.getValue())) {
                return 1;
            } else {
                return -1;
            }
        });

//        findDictionariesByParentCodeWithAll("15000");
    }


    /**
     * 表单提交
     */
    @Command
    @NotifyChange({"infos"})
    public void submit() {
        try {
            if (StringUtils.isBlank(current.getModelCode()) || StringUtils.isBlank(current.getTypeCode())) {
                ZkUtils.showExclamation("车辆型号和车辆类别都不能为空!", "系统提示");
                return;
            }
            current = vehicleModelService.save(current);
            originInfos = vehicleModelService.findAll();
            searchModels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("infos")
    public void refresh() {
        originInfos = vehicleModelService.findAll();
        searchModels();
    }

    @Command
    @NotifyChange("infos")
    public void searchModels() {
        if (StringUtils.isBlank(this.keyword) && this.selectedVehicleType.getValue().equals("0")) {
            this.infos = this.originInfos;
        } else if (StringUtils.isBlank(this.keyword)) {
            this.infos = originInfos.stream()
                    .filter(info -> info.getTypeCode().toUpperCase().equals(this.selectedVehicleType.getCode().toUpperCase()))
                    .collect(Collectors.toList());
        } else if (this.selectedVehicleType.getValue().equals("0")) {
            this.infos = originInfos.stream()
                    .filter(info -> info.getModelCode().toUpperCase().contains(this.keyword.toUpperCase()))
                    .collect(Collectors.toList());
        } else {
            this.infos = originInfos.stream()
                    .filter(info -> info.getTypeCode().toUpperCase().equals(this.selectedVehicleType.getCode().toUpperCase()))
                    .filter(info -> info.getModelCode().toUpperCase().contains(this.keyword.toUpperCase()))
                    .collect(Collectors.toList());
        }

    }

    @Command
    @NotifyChange({"infos", "current"})
    public void deleteEntity() {
        try {
            if (current == null || StringUtils.isBlank(current.getModelCode())) {
                ZkUtils.showExclamation("未选中对象!", "系统提示");
                return;
            }
            Boolean result = vehicleModelService.delete(current.getObjId());
            if (result) {
                ZkUtils.showInformation("删除成功!", "系统提示");
                originInfos = vehicleModelService.findAll();
                this.current = null;
                searchModels();
            } else {
                ZkUtils.showInformation("删除失败!", "系统提示");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange("current")
    public void addVehicleModel() {
        current = new VehicleModelInfo();
    }

    @Command
    @NotifyChange("current")
    public void selectInfo(@BindingParam("model") VehicleModelInfo info) {
        current = info;
    }

    @Command
    @NotifyChange({"vehicleTypesForm", "selectedVehicleTypeForm", "current"})
    public void selectVehicleTypeForm(@BindingParam("model") DictionaryInfo info) {
        this.selectedVehicleTypeForm = info;
        this.current.setTypeCode(this.selectedVehicleTypeForm.getCode());
        this.current.setTypeName(this.selectedVehicleTypeForm.getName());
    }

    @Command
    @NotifyChange("infos")
//    public void textChangingHandler(@BindingParam("v") String value, @ContextParam(ContextType.TRIGGER_EVENT) InputEvent event){
    public void textChangingHandler(@BindingParam("v") String value) {
        System.out.println(LocalDateTime.now().toString());
        this.keyword = value;
        searchModels();
//        BindUtils.postNotifyChange(null,null,this,"infos");
    }
}
