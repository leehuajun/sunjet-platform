package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.CommissionPartEntity;
import com.sunjet.backend.modules.asms.entity.asm.view.ActivityMaintenanceCommissionPartView;
import com.sunjet.backend.modules.asms.entity.asm.view.WarrantyMaintenanceCommissionPartView;
import com.sunjet.backend.modules.asms.repository.asm.ActivityMaintenanceCommissionPartViewRepository;
import com.sunjet.backend.modules.asms.repository.asm.CommissionPartRepository;
import com.sunjet.backend.modules.asms.repository.asm.WarrantyMaintenanceCommissionPartViewRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.asm.CommissionPartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 配件需求列表
 */
@Slf4j
@Transactional
@Service("commissionPartService")
public class CommissionPartServiceImpl implements CommissionPartService {
    @Autowired
    private CommissionPartRepository commissionPartRepository;

    @Autowired
    private WarrantyMaintenanceCommissionPartViewRepository commissionPartViewRepository;

    @Autowired
    private ActivityMaintenanceCommissionPartViewRepository activityMaintenanceCommissionPartViewRepository;

    /**
     * 保存 实体
     *
     * @param commissionPartInfo
     * @return
     */
    @Override
    public CommissionPartInfo save(CommissionPartInfo commissionPartInfo) {
        try {
            CommissionPartEntity entity = commissionPartRepository.save(BeanUtils.copyPropertys(commissionPartInfo, new CommissionPartEntity()));

            return BeanUtils.copyPropertys(entity, new CommissionPartInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 commissionPartInfo 对象
     *
     * @param commissionPartInfo
     * @return
     */
    @Override
    public boolean delete(CommissionPartInfo commissionPartInfo) {
        try {
            commissionPartRepository.delete(commissionPartInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            commissionPartRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public CommissionPartInfo findOne(String objId) {
        try {
            CommissionPartEntity commissionPartEntity = commissionPartRepository.findOne(objId);
            return BeanUtils.copyPropertys(commissionPartEntity, new CommissionPartInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过三包单objid查找配件列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<CommissionPartInfo> findAllByWarrantyMaintenanceObjId(String objId) {

        try {
            List<CommissionPartEntity> commissionPartEntityList = commissionPartRepository.findAllByWarrantyMaintenanceObjId(objId);
            List<CommissionPartInfo> commissionPartInfos = new ArrayList<>();
            for (CommissionPartEntity commissionPartEntity : commissionPartEntityList) {
                commissionPartInfos.add(BeanUtils.copyPropertys(commissionPartEntity, new CommissionPartInfo()));
            }
            log.info("CommissionPartServicelmpl:findAllByWarrantyMaintenanceObjId:success");
            return commissionPartInfos;
        } catch (Exception e) {
            log.info("CommissionPartServicelmpl:findAllByWarrantyMaintenanceObjId:error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动服务单ID删除实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByActivityMaintenanceId(String objId) {
        try {
            commissionPartRepository.deleteByActivityMaintenanceId(objId);

            log.info("CommissionPartServicelmpl:deleteByActivityMaintenanceId:success");
            return true;
        } catch (Exception e) {
            log.info("CommissionPartServicelmpl:deleteByActivityMaintenanceId:error:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过活单服务单ID查找实体
     *
     * @param objId
     * @return
     */
    @Override
    public List<CommissionPartInfo> findAllByActivityMaintenanceObjId(String objId) {
        try {
            List<CommissionPartEntity> commissionPartEntityList = commissionPartRepository.findAllByActivityMaintenanceObjId(objId);
            List<CommissionPartInfo> commissionPartInfos = new ArrayList<>();
            for (CommissionPartEntity commissionPartEntity : commissionPartEntityList) {
                commissionPartInfos.add(BeanUtils.copyPropertys(commissionPartEntity, new CommissionPartInfo()));
            }
            log.info("CommissionPartServicelmpl:findAllByActivityMaintenanceObjId:success");
            return commissionPartInfos;
        } catch (Exception e) {
            log.info("CommissionPartServicelmpl:findAllByActivityMaintenanceObjId:error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过三包服务单idList查找配件需求view
     *
     * @param warrantyMaintenanceIdList
     * @return
     */
    @Override
    public List<WarrantyMaintenanceCommissionPartView> findAllByWarrantyMaintenanceIdList(List<String> warrantyMaintenanceIdList) {
        try {
            List<WarrantyMaintenanceCommissionPartView> commissionPartViewList = commissionPartViewRepository.findCommissionPartInfoByWarrantyMaintenanceIdList(warrantyMaintenanceIdList);
            return commissionPartViewList;
        } catch (Exception e) {
            log.info("CommissionPartServicelmpl:findAllByWarrantyMaintenanceIdList:error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动服务单idList查找配件需求view
     *
     * @param activityMaintenanceIdList
     * @return
     */
    @Override
    public List<ActivityMaintenanceCommissionPartView> findAllByActivityMaintenanceIdList(List<String> activityMaintenanceIdList) {
        try {
            List<ActivityMaintenanceCommissionPartView> commissionPartViewList = activityMaintenanceCommissionPartViewRepository.findCommissionPartInfoByActivityMaintenanceIdList(activityMaintenanceIdList);
            return commissionPartViewList;
        } catch (Exception e) {
            log.info("CommissionPartServicelmpl:findAllByActivityMaintenanceIdList:error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据三包单id删除维修配件
     *
     * @param objId
     * @return
     */
    @Override
    public Boolean deleteByWarrantyMaintenanceObjId(String objId) {
        try {
            commissionPartRepository.deleteByWarrantyMaintenanceObjId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存配件列表
     *
     * @param commissionPartEntityList
     * @return
     */
    @Override
    public List<CommissionPartEntity> saveList(List<CommissionPartEntity> commissionPartEntityList) {
        return commissionPartRepository.save(commissionPartEntityList);
    }


}
