package com.sunjet.backend.controller.basic;

import com.sunjet.backend.modules.asms.service.basic.VehicleService;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SUNJET_QRY on 2017/7/27.
 * 车辆信息
 */
@Slf4j
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/getPageList")
    public PageResult<VehicleInfo> getPageList(@RequestBody PageParam<VehicleInfo> pageParam) {
        return vehicleService.getPageList(pageParam);
    }

    @PostMapping("/findOne")
    public VehicleInfo findOne(@RequestBody String objId) {
        return vehicleService.findOne(objId);
    }


    /**
     * 保存
     *
     * @param vehicleInfo
     * @return
     */
    @PostMapping("/save")
    public VehicleInfo save(@RequestBody VehicleInfo vehicleInfo) {
        return vehicleService.save(vehicleInfo);
    }

    @PostMapping("/findAllByKeyword")
    public List<VehicleInfo> findAllByKeyword(@RequestBody String keyword) {
        return vehicleService.findAllByKeyword(keyword);
    }

    @PostMapping("/findByVehicleId")
    public List<VehicleInfo> findByVehicleId(@RequestBody List<String> objIds) {

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(objIds);
        return vehicleService.findByVehicleId(arrayList);
    }

    @PostMapping("/findAllByKeywordAndFmDateIsNull")
    public List<VehicleInfo> findAllByKeywordAndFmDateIsNull(@RequestBody String keyword) {
        return vehicleService.findAllByKeywordAndFmDateIsNull(keyword);
    }

    @PostMapping("/findAllByVinIn")
    public List<VehicleInfo> findAllByVinIn(@RequestBody List<String> vins) {
        return vehicleService.findAllByVinIn(vins);
    }

    @PostMapping("/importVehicles")
    public List<VehicleInfoExt> importVehicles(@RequestBody List<VehicleInfoExt> vehicles) {
        return vehicleService.importVehicles(vehicles);
    }

    @PostMapping("/modifyVehicles")
    public List<VehicleInfoExt> modifyVehicles(@RequestBody List<VehicleInfoExt> vehicles) {
        return vehicleService.modifyVehicles(vehicles);
    }

    @PostMapping("/findOneByVin")
    public VehicleInfo findOneByVin(@RequestBody String vin) {
        return vehicleService.findOneByVin(vin);
    }
}
