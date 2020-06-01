package com.sunjet.backend.modules.asms.service.settlement;


import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.entity.settlement.PartExpenseListEntity;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PartExpenseListRepository;
import com.sunjet.backend.modules.asms.service.activity.ActivityDistributionService;
import com.sunjet.backend.modules.asms.service.asm.WarrantyMaintenanceService;
import com.sunjet.backend.modules.asms.service.supply.SupplyService;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.dto.asms.settlement.PartExpenseItemsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 配件费用明细
 * Created by Administrator on 2016/10/26.
 */
@Transactional
@Service("partExpenseListService")
public class PartExpenseListServiceImpl implements PartExpenseListService {
    @Autowired
    private PartExpenseListRepository partExpenseListRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private WarrantyMaintenanceService warrantyMaintenanceService;
    @Autowired
    private ActivityDistributionService activityDistributionService;
    @Autowired
    private SupplyService supplyService;

    /**
     * 通过info查entity
     *
     * @param partExpenseItemsInfo
     * @return
     */
    @Override
    public PartExpenseItemsInfo save(PartExpenseItemsInfo partExpenseItemsInfo) {
        try {
            PartExpenseListEntity entity = partExpenseListRepository.save(BeanUtils.copyPropertys(partExpenseItemsInfo, new PartExpenseListEntity()));
            return BeanUtils.copyPropertys(entity, partExpenseItemsInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过objId查一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public PartExpenseItemsInfo findOne(String objId) {
        try {
            PartExpenseListEntity entity = partExpenseListRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new PartExpenseItemsInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过info删除一个实体
     *
     * @param partExpenseItemsInfo
     * @return
     */
    @Override
    public boolean delete(PartExpenseItemsInfo partExpenseItemsInfo) {
        try {
            partExpenseListRepository.delete(partExpenseItemsInfo.getObjId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过objId删除一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            partExpenseListRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过配件结算单id查找配件费用子行列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<PartExpenseItemsInfo> findByAgencySettlementId(String objId) {
        try {
            List<PartExpenseListEntity> entityList = partExpenseListRepository.findByAgencySettlementId(objId);
            List<PartExpenseItemsInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (PartExpenseListEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new PartExpenseItemsInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过配件结算单id删除同时配件结算子行
     *
     * @param objId
     * @return
     */
    @Override
    public boolean deleteByAgencySettlementId(String objId) {
        try {
            partExpenseListRepository.deleteByAgencySettlementId(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过来源单据查询配件结算Id
     *
     * @param srcDocNo
     * @return
     */
    @Override
    public List<String> findAllAgencySettlementObjIdBySrcDocNo(String srcDocNo) {
        List<String> objIds = new ArrayList<>();
        try {
            List<PartExpenseListEntity> expenseListEntities = partExpenseListRepository.findAllAgencySettlementObjIdBySrcDocNo("%" + srcDocNo + "");
            if (expenseListEntities != null && expenseListEntities.size() > 0) {
                for (PartExpenseListEntity expenseList : expenseListEntities) {
                    objIds.add(expenseList.getAgencySettlementId());
                }
            }
            return objIds;
        } catch (Exception e) {
            e.printStackTrace();
            return objIds;
        }
    }

    /**
     * 通过vin查询配件结算Id
     *
     * @param vin
     * @return
     */
    @Override
    public List<String> findAllAgencySettlementObjIdByVin(String vin) {
        //服务结算单id
        List<String> objIds = new ArrayList<>();

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
            List<String> supplyIds = supplyService.findAllIdByVehicleIds(vehicleObjIds);

            if (supplyIds != null && supplyIds.size() > 0) {
                List<PartExpenseListEntity> expenseListEntities = partExpenseListRepository.findAllAgencySettlementObjIdBySrcId(supplyIds);
                if (expenseListEntities != null && expenseListEntities.size() > 0) {
                    for (PartExpenseListEntity expenseListEntity : expenseListEntities) {
                        objIds.add(expenseListEntity.getAgencySettlementId());
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
