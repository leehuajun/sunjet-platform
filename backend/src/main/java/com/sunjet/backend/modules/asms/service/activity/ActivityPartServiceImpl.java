package com.sunjet.backend.modules.asms.service.activity;


import com.sunjet.backend.modules.asms.entity.activity.ActivityPartEntity;
import com.sunjet.backend.modules.asms.entity.activity.view.ActivityPartView;
import com.sunjet.backend.modules.asms.entity.basic.PartEntity;
import com.sunjet.backend.modules.asms.repository.activity.ActivityPartRepository;
import com.sunjet.backend.modules.asms.repository.activity.ActivityPartViewRepository;
import com.sunjet.backend.modules.asms.repository.basic.PartRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.activity.ActivityPartInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 服务活动配件子行
 */
@Transactional
@Service("activityPartService")
public class ActivityPartServiceImpl implements ActivityPartService {

    @Autowired
    private ActivityPartRepository activityPartRepository;
    @Autowired
    private ActivityPartViewRepository activityPartViewRepository;
    @Autowired
    private PartRepository partRepository;

    /**
     * 通过活动单ID查配件
     *
     * @param activityNoticeId
     * @return
     */
    @Override
    public List<ActivityPartView> findPartByActivityNoticeId(String activityNoticeId) {

        List<ActivityPartView> activityPartViews = activityPartViewRepository.findActivityPartItemsByNoticeId(activityNoticeId);

        return activityPartViews;
    }

    /**
     * 保存 实体
     *
     * @param activityPartInfo
     * @return
     */
    @Override
    public ActivityPartInfo save(ActivityPartInfo activityPartInfo) {
        try {
            ActivityPartEntity entity = BeanUtils.copyPropertys(activityPartInfo, new ActivityPartEntity());
            activityPartRepository.save(entity);
            ActivityPartInfo partInfo = BeanUtils.copyPropertys(entity, new ActivityPartInfo());
            PartEntity partEntity = partRepository.findOne(entity.getPartId());
            //partInfo.setPartCode(partEntity.getCode());
            //partInfo.setPartName(partEntity.getName());
            //partInfo.setWarrantyMileage(partEntity.getWarrantyMileage());
            //partInfo.setWarrantyTime(partEntity.getWarrantyTime());
            //partInfo.setUnit(partEntity.getUnit());
            //partInfo.setPrice(partEntity.getPrice());
            return partInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteById(String objId) {
        try {
            activityPartRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ActivityPartInfo findOneById(String objId) {
        try {
            ActivityPartEntity entity = activityPartRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new ActivityPartInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动通知单ID删除该活动单下面的所有活动配件
     *
     * @param activityNoticeId
     * @return
     */
    @Override
    public boolean deleteByActivityNoticeId(String activityNoticeId) {
        try {
            activityPartRepository.deleteByActivityNoticeId(activityNoticeId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
