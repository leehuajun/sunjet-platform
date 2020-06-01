package com.sunjet.backend.modules.asms.service.asm;


import com.sunjet.backend.modules.asms.entity.asm.GoOutEntity;
import com.sunjet.backend.modules.asms.repository.asm.GoOutRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.asm.GoOutInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 活动列表
 */
@Slf4j
@Transactional
@Service("goOutService")
public class GoOutServiceImpl implements GoOutService {
    @Autowired
    private GoOutRepository goOutRepository;

    /**
     * 保存 实体
     *
     * @param goOutInfo
     * @return
     */
    @Override
    public GoOutInfo save(GoOutInfo goOutInfo) {
        try {
            GoOutEntity entity = goOutRepository.save(BeanUtils.copyPropertys(goOutInfo, new GoOutEntity()));
            return BeanUtils.copyPropertys(entity, new GoOutInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 goOutInfo 对象
     *
     * @param goOutInfo
     * @return
     */
    @Override
    public boolean delete(GoOutInfo goOutInfo) {
        try {
            goOutRepository.delete(goOutInfo.getObjId());
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
            goOutRepository.delete(objId);
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
    public GoOutInfo findOne(String objId) {
        try {
            GoOutEntity goOutEntity = goOutRepository.findOne(objId);
            return BeanUtils.copyPropertys(goOutEntity, new GoOutInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过三包单查找外出活动列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<GoOutInfo> findAllByWarrantyMaintenanceObjId(String objId) {
        try {
            List<GoOutEntity> goOutEntities = goOutRepository.findAllByWarrantyMaintenanceObjId(objId);
            List<GoOutInfo> goOutInfos = new ArrayList<>();
            for (GoOutEntity goOutEntity : goOutEntities) {
                goOutInfos.add(BeanUtils.copyPropertys(goOutEntity, new GoOutInfo()));
            }
            return goOutInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过首保服务单查找外出活动列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<GoOutInfo> findAllByFirstMaintenanceId(String objId) {
        try {
            List<GoOutEntity> goOutEntities = goOutRepository.findAllByFirstMaintenanceId(objId);
            List<GoOutInfo> goOutInfos = new ArrayList<>();
            for (GoOutEntity goOutEntity : goOutEntities) {
                goOutInfos.add(BeanUtils.copyPropertys(goOutEntity, new GoOutInfo()));
            }
            return goOutInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过活动服务单ID删除
     *
     * @param activityMaintenanceId
     * @return
     */
    @Override
    public boolean deleteByActivityMaintenanceId(String activityMaintenanceId) {
        try {
            goOutRepository.deleteByActivityMaintenanceId(activityMaintenanceId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 通过活动单Id查找活动外出
     *
     * @param objId
     * @return
     */
    @Override
    public List<GoOutInfo> findAllByActivityMaintenanceObjId(String objId) {
        try {
            List<GoOutEntity> goOutEntities = goOutRepository.findAllByActivityMaintenanceObjId(objId);
            List<GoOutInfo> goOutInfos = new ArrayList<>();
            for (GoOutEntity goOutEntity : goOutEntities) {
                goOutInfos.add(BeanUtils.copyPropertys(goOutEntity, new GoOutInfo()));
            }
            return goOutInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存外出信息列表
     *
     * @param goOutEntityList
     * @return
     */
    @Override
    public List<GoOutEntity> saveList(List<GoOutEntity> goOutEntityList) {
        return goOutRepository.save(goOutEntityList);
    }

}
