package com.sunjet.backend.utils.activiti.listener.supply;

import com.sunjet.backend.modules.asms.entity.supply.SupplyWaitingItemEntity;
import com.sunjet.backend.modules.asms.repository.supply.SupplyWaitingItemRepository;
import com.sunjet.backend.modules.asms.service.supply.SupplyNoticeItemService;
import com.sunjet.backend.modules.asms.service.supply.SupplyNoticeService;
import com.sunjet.backend.system.service.ProcessService;
import com.sunjet.dto.asms.supply.SupplyNoticeInfo;
import com.sunjet.dto.asms.supply.SupplyNoticeItemInfo;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by SUNJET_QRY on 2017/11/6.
 */
@Component("createPartsSalesWarrantySupplyTaskListener")
public class CreatePartsSalesWarrantySupplyTaskListener implements ExecutionListener {

    @Autowired
    private ProcessService processService;
    @Autowired
    private SupplyNoticeService supplyNoticeService;

    @Autowired
    private SupplyNoticeItemService supplyNoticeItemService;
    @Autowired
    private SupplyWaitingItemRepository supplyWaitingItemRepository;


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        // 1. 获取业务ID
        String businessId = processService.findBusinessIdByProcessInstanceId(delegateExecution.getProcessInstanceId());


        SupplyNoticeInfo supplyNoticeInfo = supplyNoticeService.findOne(businessId);


        //生成待发清单
        if ("配件销售质保".equals(supplyNoticeInfo.getDocType())) {
            if (supplyNoticeInfo.getSupplyNoticeItemInfos().size() > 0) {

                for (SupplyNoticeItemInfo supplyNoticeItemInfo : supplyNoticeInfo.getSupplyNoticeItemInfos()) {
                    //待发货清单
                    SupplyWaitingItemEntity supplyWaitingItem = new SupplyWaitingItemEntity();

                    supplyWaitingItem.setAgencyName(supplyNoticeInfo.getAgencyName());
                    supplyWaitingItem.setAgencyCode(supplyNoticeInfo.getAgencyCode());
                    supplyWaitingItem.setPartCode(supplyNoticeItemInfo.getPartCode());
                    supplyWaitingItem.setPartName(supplyNoticeItemInfo.getPartName());
                    //待发货清单请求数量 = 调拨通知单请求数量
                    supplyWaitingItem.setRequestAmount(supplyNoticeItemInfo.getRequestAmount());   //需求分配
                    supplyWaitingItem.setSurplusAmount(supplyNoticeItemInfo.getRequestAmount());   //可分配
                    //待发货发送数量 = 调拨通知单请求数量 -  待发货需求数量
                    supplyWaitingItem.setSentAmount(0);
                    supplyWaitingItem.setArrivalTime(supplyNoticeItemInfo.getArrivalTime());    //要求到货时间
                    supplyWaitingItem.setSupplyNoticeItemId(supplyNoticeItemInfo.getObjId());   //调拨分配
                    //supplyWaitingItem.setSupplyDisItemId();     //二次分配
                    supplyWaitingItem.setComment(supplyNoticeInfo.getComment());
                    supplyWaitingItem.setEnabled(true);
                    supplyWaitingItem.setCreatedTime(new Date());
                    supplyWaitingItem.setCreaterName(supplyNoticeInfo.getCreaterName());
                    supplyWaitingItem.setCreaterId(supplyNoticeInfo.getCreaterId());
                    supplyWaitingItem.setDealerCode(supplyNoticeInfo.getDealerCode());
                    supplyWaitingItem.setDealerName(supplyNoticeInfo.getDealerName());
                    supplyWaitingItem.setServiceManager(supplyNoticeInfo.getServiceManager());
                    //supplyWaitingItemEntities.add(supplyWaitingItemRepository.save(supplyWaitingItem));
                    supplyWaitingItemRepository.save(supplyWaitingItem);

                    //更改调拨分配数量
                    supplyNoticeItemInfo.setSentAmount(supplyNoticeItemInfo.getRequestAmount());
                    supplyNoticeItemInfo.setSurplusAmount(0);

                    supplyNoticeItemService.save(supplyNoticeItemInfo);

                }
            }

            //if (supplyWaitingItemEntities.size() > 0) {
            //    //供货通知单明细
            //    List<SupplyItemEntity> supplyItemEntities = new ArrayList<>();
            //
            //    double expenseTotal = 0;
            //    for (SupplyWaitingItemEntity SupplyWaitingItemItem : supplyWaitingItemEntities) {
            //        SupplyItemEntity supplyItem = new SupplyItemEntity();
            //        supplyItem.setPartCode(SupplyWaitingItemItem.getPartCode());
            //        supplyItem.setPartName(SupplyWaitingItemItem.getPartName());
            //        supplyItem.setAmount(SupplyWaitingItemItem.getRequestAmount());
            //        //supplyItem.setPrice(item.getPrice());
            //        //supplyItem.setSupplyWaitingItemId(item.getObjId());
            //        supplyItem.setSupplyWaitingItemId(SupplyWaitingItemItem.getObjId());
            //        supplyItem.setSupplyNoticeItemId(SupplyWaitingItemItem.getSupplyNoticeItemId());
            //
            //        List<PartEntity> partEntities = partRepository.findAllByKeyword(SupplyWaitingItemItem.getPartCode());
            //        if (partEntities.size() > 0) {
            //            supplyItem.setPartId(partEntities.get(0).getObjId());
            //            supplyItem.setPrice(partEntities.get(0).getPrice());
            //            supplyItem.setMoney(supplyItem.getAmount() * supplyItem.getPrice());
            //        }
            //        expenseTotal = expenseTotal + supplyItem.getMoney();
            //        DealerEntity dealer = dealerRepository.findOneByCode(SupplyWaitingItemItem.getDealerCode());
            //        supplyRequest.setDealerCode(dealer.getCode());
            //        supplyRequest.setDealerName(dealer.getName());
            //        supplyRequest.setDealerAdderss(dealer.getAddress());
            //        supplyRequest.setReceive(dealer.getStationMaster());
            //        supplyRequest.setOperatorPhone(dealer.getStationMasterPhone());
            //
            //        supplyRequest.setAgencyName(SupplyWaitingItemItem.getAgencyName());
            //        supplyRequest.setAgencyCode(SupplyWaitingItemItem.getAgencyCode());
            //
            //        //变更待返清单数量
            //
            //
            //        ////可分配数量 = 历史可分配数量 - 本次发货数量
            //        //SupplyWaitingItemItem.setSurplusAmount(SupplyWaitingItemItem.getSurplusAmount() - supplyItem.getAmount());
            //        ////已发数量 = 需求数量 - 可分配数量
            //        //SupplyWaitingItemItem.setSentAmount(SupplyWaitingItemItem.getRequestAmount() - SupplyWaitingItemItem.getSurplusAmount());
            //
            //
            //        supplyItemEntities.add(supplyItem);
            //    }
            //
            //    supplyRequest.setSubmitter(supplyNoticeInfo.getSubmitter());
            //    supplyRequest.setSubmitterName(supplyNoticeInfo.getSubmitterName());
            //    supplyRequest.setSubmitterPhone(supplyNoticeInfo.getOperatorPhone());
            //
            //    supplyRequest.setExpenseTotal(expenseTotal);
            //    supplyRequest.setPartExpense(expenseTotal);
            //    SupplyInfo supply = supplyService.save(BeanUtils.copyPropertys(supplyRequest, new SupplyInfo()));
            //    //SupplyEntity supplyEntity = supplyRepository.save(supplyRequest);
            //    //绑定关系
            //    for (SupplyItemEntity supplyItem : supplyItemEntities) {
            //        supplyItem.setSupplyId(supply.getObjId());
            //        supplyItemRepository.save(supplyItem);
            //    }
            //
            //}


        }

    }


}
