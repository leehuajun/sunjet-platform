package com.sunjet.backend.modules.asms.service.basic;

import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehicleModelEntity;
import com.sunjet.backend.modules.asms.repository.basic.VehicleModelRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.basic.VehicleModelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lhj
 * @create: 2017-10-24 12:36
 * @description: 说明
 */
@Transactional
@Service("vehicleModelService")
public class VehicleModelServiceImpl implements VehicleModelService {

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Override
    public VehicleModelInfo save(VehicleModelInfo info) {
        VehicleModelEntity entity = BeanUtils.copyPropertys(info, new VehicleModelEntity());
        entity = vehicleModelRepository.save(entity);
        return BeanUtils.copyPropertys(entity, new VehicleModelInfo());
    }

    @Override
    public boolean delete(String objId) {
        try {
            vehicleModelRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public VehicleModelInfo findOne(String objId) {
        return BeanUtils.copyPropertys(vehicleModelRepository.findOne(objId), new VehicleModelInfo());
    }

    @Override
    public List<VehicleModelInfo> findAll() {
        List<VehicleModelEntity> entities = vehicleModelRepository.findAll();
        List<VehicleModelInfo> infos = new ArrayList<>();
        entities.forEach(entity -> {
            infos.add(BeanUtils.copyPropertys(entity, new VehicleModelInfo()));
        });
        return infos;
    }
}
