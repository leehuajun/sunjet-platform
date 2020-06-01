package com.sunjet.backend.modules.asms.service.basic;

import com.sunjet.backend.modules.asms.entity.basic.VehiclePlatformEntity;
import com.sunjet.backend.modules.asms.repository.basic.VehiclePlatformRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.basic.VehiclePlatformInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lhj
 * @create: 2017-10-18 14:09
 * @description: 说明
 */
@Transactional
@Service("vehiclePlatformService")
public class VehiclePlatformServiceImpl implements VehiclePlatformService {

    @Autowired
    private VehiclePlatformRepository vehiclePlatformRepository;

    @Override
    public VehiclePlatformInfo save(VehiclePlatformInfo info) {
        try {
            VehiclePlatformEntity entity = vehiclePlatformRepository.save(BeanUtils.copyPropertys(info, new VehiclePlatformEntity()));
            return BeanUtils.copyPropertys(entity, new VehiclePlatformInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(String objId) {
        try {
            vehiclePlatformRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public VehiclePlatformInfo findOne(String objId) {
        try {
            VehiclePlatformEntity entity = vehiclePlatformRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new VehiclePlatformInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<VehiclePlatformInfo> findAll() {
        List<VehiclePlatformEntity> entities = vehiclePlatformRepository.findAll();
        List<VehiclePlatformInfo> infos = new ArrayList<>();
        for (VehiclePlatformEntity entity : entities) {
            infos.add(BeanUtils.copyPropertys(entity, new VehiclePlatformInfo()));
        }
        return infos;
    }
}
