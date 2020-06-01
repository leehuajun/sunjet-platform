package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.ExpenseListEntity;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.ExpenseListRepository;
import com.sunjet.backend.modules.asms.service.activity.ActivityMaintenanceService;
import com.sunjet.backend.modules.asms.service.asm.FirstMaintenanceService;
import com.sunjet.backend.modules.asms.service.asm.WarrantyMaintenanceService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.settlement.ExpenseItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 * 服务站结算列表子行
 */
@Transactional
@Service("expenseListService")
public class ExpenseListServiceImpl implements ExpenseListService {
    @Autowired
    private ExpenseListRepository expenseListRepository;

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @Autowired
    private FirstMaintenanceService firstMaintenanceService;
    @Autowired
    private ActivityMaintenanceService activityMaintenanceService;

    /**
     * 通过ifno 保存一个实体
     *
     * @param expenseItemInfo
     * @return
     */
    @Override
    public ExpenseItemInfo save(ExpenseItemInfo expenseItemInfo) {
        try {
            ExpenseListEntity entity = expenseListRepository.save(BeanUtils.copyPropertys(expenseItemInfo, new ExpenseListEntity()));
            return BeanUtils.copyPropertys(entity, expenseItemInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过objid 查找一个entity
     *
     * @param objId
     * @return
     */
    @Override
    public ExpenseItemInfo findOne(String objId) {
        try {
            ExpenseListEntity entity = expenseListRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new ExpenseItemInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info删除一个实体
     *
     * @param expenseItemInfo
     * @return
     */
    @Override
    public boolean delete(ExpenseItemInfo expenseItemInfo) {
        try {
            expenseListRepository.delete(expenseItemInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objid删除一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            expenseListRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过费用id查找费用需求列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<ExpenseItemInfo> findByDealerSettlementId(String objId) {
        try {
            List<ExpenseListEntity> entityList = expenseListRepository.findByDealerSettlementId(objId);
            List<ExpenseItemInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (ExpenseListEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new ExpenseItemInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过费用删除的同时删除与此关联的费用子行
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByDealerSettlementId(String objId) {
        try {
            expenseListRepository.deleteByDealerSettlementId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过来源编号查询结算单id
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<String> findAllDealerSettlementObjIdBySrcDocNo(String srcDocNo) {
        List<String> objIds = new ArrayList<>();
        try {
            List<ExpenseListEntity> expenseListEntities = expenseListRepository.findAllDealerSettlementObjIdBySrcDocNo("%" + srcDocNo + "");
            if (expenseListEntities != null && expenseListEntities.size() > 0) {
                for (ExpenseListEntity expenseList : expenseListEntities) {
                    objIds.add(expenseList.getDealerSettlementId());
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    /**
     * 通过vin查询服务结算单objid
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllDealerSettlementObjIdByVin(String vin) {
        //服务结算单id
        List<String> objIds = new ArrayList<>();
        //来源单据id
        List<String> srcObjIds = new ArrayList<>();
        //车辆id
        List<String> vehicleObjIds = new ArrayList<>();
        try {
            List<VehicleEntity> vehicleEntities = vehicleRepository.findAllByKeyword("%" + vin + "%");
            //查询车辆id
            if (vehicleEntities != null && vehicleEntities.size() > 0) {
                for (VehicleEntity vehicle : vehicleEntities) {
                    vehicleObjIds.add(vehicle.getObjId());
                }
            }
            //查询来源单据id
            if (vehicleObjIds.size() > 0) {
                //三包单
                List<String> warrantyIds = warrantyMaintenanceService.findAllObjIdsByVehicleId(vehicleObjIds);
                if (warrantyIds != null && warrantyIds.size() > 0) {
                    srcObjIds.addAll(warrantyIds);
                }
                //首保
                List<String> firstIds = firstMaintenanceService.findAllIdByVehcicleIds(vehicleObjIds);
                if (firstIds != null && firstIds.size() > 0) {
                    srcObjIds.addAll(firstIds);
                }
                //活动服务
                List<String> activityIds = activityMaintenanceService.findAllIdByVehcicleIds(vehicleObjIds);
                if (activityIds != null && activityIds.size() > 0) {
                    srcObjIds.addAll(activityIds);
                }
            }
            if (srcObjIds.size() > 0) {
                List<ExpenseListEntity> expenseListEntities = expenseListRepository.findAllDealerSettlementObjIdBySrcId(srcObjIds);
                if (expenseListEntities != null && expenseListEntities.size() > 0) {
                    for (ExpenseListEntity expenseListEntity : expenseListEntities) {
                        objIds.add(expenseListEntity.getDealerSettlementId());
                    }
                }
            }


            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }


}
