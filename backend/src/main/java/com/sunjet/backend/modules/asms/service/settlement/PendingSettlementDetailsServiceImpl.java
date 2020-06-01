package com.sunjet.backend.modules.asms.service.settlement;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.Enum.DocStatus;
import com.sunjet.backend.modules.asms.entity.settlement.PendingSettlementDetailsEntity;
import com.sunjet.backend.modules.asms.entity.settlement.view.PendingSettlementDetailsViews;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsRepository;
import com.sunjet.backend.modules.asms.repository.settlement.PendingSettlementDetailsViewsRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailInfo;
import com.sunjet.dto.asms.settlement.PendingSettlementDetailItems;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zyh on 2016/11/14.
 * 待结算清单(配件／服务／运费)
 */
@Transactional
@Service("pendingSettlementDetailsService")
public class PendingSettlementDetailsServiceImpl implements PendingSettlementDetailsService {

    @Autowired
    private PendingSettlementDetailsRepository pendingSettlementDetailsRepository;//dao
    @Autowired
    private PendingSettlementDetailsViewsRepository pendingSettlementDetailsViewsRepository;//view


    /**
     * 根据单据编号获取一个实体对象
     *
     * @param srcDocID
     * @return
     */
    @Override
    public PendingSettlementDetailsEntity getOneBySrcDocID(String srcDocID) {
        try {
            PendingSettlementDetailsEntity entity = pendingSettlementDetailsRepository.getOneBySrcDocID(srcDocID);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据结算单ID 获取待结算单列表
     *
     * @param objId
     * @return
     */
    @Override
    public List<PendingSettlementDetailInfo> getPendingsBySettlementID(String objId) {
        try {
            List<PendingSettlementDetailsEntity> entityList = pendingSettlementDetailsRepository.getPendingsBySettlementID(objId);
            List<PendingSettlementDetailInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (PendingSettlementDetailsEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new PendingSettlementDetailInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取服务结算单
     *
     * @param code
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PendingSettlementDetailInfo> getDealerSelttlements(String code, Date startDate, Date endDate) {
        try {
            List<PendingSettlementDetailsEntity> entityList = pendingSettlementDetailsRepository.getDealerSelttlements(code, startDate, endDate);
            List<PendingSettlementDetailInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (PendingSettlementDetailsEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new PendingSettlementDetailInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取运费结算单
     *
     * @param code
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PendingSettlementDetailInfo> getFreightSelttlements(String code, Date startDate, Date endDate) {
        try {
            List<PendingSettlementDetailsEntity> entityList = pendingSettlementDetailsRepository.getFreightSelttlements(code, startDate, endDate);
            List<PendingSettlementDetailInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (PendingSettlementDetailsEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new PendingSettlementDetailInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取配件运费单
     *
     * @param agencyCode
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PendingSettlementDetailInfo> getAgencySelttlements(String agencyCode, Date startDate, Date endDate) {
        try {
            List<PendingSettlementDetailsEntity> entityList = pendingSettlementDetailsRepository.getAgencySelttlements(agencyCode, startDate, endDate);
            List<PendingSettlementDetailInfo> infoList = new ArrayList<>();
            if (entityList != null) {
                for (PendingSettlementDetailsEntity entity : entityList) {
                    infoList.add(BeanUtils.copyPropertys(entity, new PendingSettlementDetailInfo()));
                }
            }
            return infoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过vo查一个实体
     *
     * @param pendingSettlementDetailsEntity
     * @return
     */
    @Override
    public PendingSettlementDetailsEntity save(PendingSettlementDetailsEntity pendingSettlementDetailsEntity) {
        try {
            PendingSettlementDetailsEntity entity = pendingSettlementDetailsRepository.save(pendingSettlementDetailsEntity);
            return entity;
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
    public PendingSettlementDetailInfo findOne(String objId) {
        try {
            PendingSettlementDetailsEntity entity = pendingSettlementDetailsRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new PendingSettlementDetailInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过vo删除一个实体
     *
     * @param pendingSettlementDetailInfo
     * @return
     */
    @Override
    public boolean delete(PendingSettlementDetailInfo pendingSettlementDetailInfo) {
        try {
            pendingSettlementDetailsRepository.delete(pendingSettlementDetailInfo.getObjId());
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
            pendingSettlementDetailsRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<PendingSettlementDetailsViews> getPageList(PageParam<PendingSettlementDetailItems> pageParam) {
        //1.查询条件
        PendingSettlementDetailItems pendingSettlementDetailItems = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PendingSettlementDetailsViews> specification = null;

        if (pendingSettlementDetailItems != null) {
            specification = Specifications.<PendingSettlementDetailsViews>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(pendingSettlementDetailItems.getSettlementDocType()), "settlementDocType", pendingSettlementDetailItems.getSettlementDocType())
                    .eq(StringUtils.isNotEmpty(pendingSettlementDetailItems.getAgencyCode()), "agencyCode", pendingSettlementDetailItems.getAgencyCode())
                    .eq(StringUtils.isNotEmpty(pendingSettlementDetailItems.getAgencyName()), "agencyName", pendingSettlementDetailItems.getAgencyName())
                    .eq(StringUtils.isNotEmpty(pendingSettlementDetailItems.getDealerCode()), "dealerCode", pendingSettlementDetailItems.getDealerCode())
                    .eq(StringUtils.isNotEmpty(pendingSettlementDetailItems.getDealerName()), "dealerName", pendingSettlementDetailItems.getDealerName())
                    .eq(StringUtils.isNotEmpty(pendingSettlementDetailItems.getTypeCode()), "typeCode", pendingSettlementDetailItems.getTypeCode())
                    .like(StringUtils.isNotBlank(pendingSettlementDetailItems.getSrcDocNo()), "srcDocNo", "%" + pendingSettlementDetailItems.getSrcDocNo() + "%")
                    //.eq(!pendingSettlementDetailItems.getStatus().equals(DocStatus.ALL.getIndex()), "status", pendingSettlementDetailItems.getStatus())//表单状态
                    //.ge(pendingSettlementDetailItems.getStatus().equals(DocStatus.ALL.getIndex()), "status", -1)
                    .eq(!pendingSettlementDetailItems.getStatus().equals(DocStatus.ALL.getIndex()), "status", pendingSettlementDetailItems.getStatus())
                    .between("createdTime", new Range<Date>(pendingSettlementDetailItems.getStartDate(), pendingSettlementDetailItems.getEndDate()))
                    .build();
        }

        //3.执行查询
        Page<PendingSettlementDetailsViews> pages = pendingSettlementDetailsViewsRepository.findAll(specification, PageUtil.getPageRequest(pageParam));


        //4.数据转换
        //List<PendingSettlementDetailItems> rows = new ArrayList<>();
        //for(PendingSettlementDetailsViews view :pages.getContent()){
        //    PendingSettlementDetailItems item = new PendingSettlementDetailItems();
        //    item = BeanUtils.copyPropertys(view,item);
        //    rows.add(item);
        //}

        ////5.返回
        //return PageUtil.getPageResult(rows,pages,pageParam);
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

}
