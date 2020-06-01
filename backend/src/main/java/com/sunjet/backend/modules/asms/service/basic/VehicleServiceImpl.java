package com.sunjet.backend.modules.asms.service.basic;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.VehicleEntity;
import com.sunjet.backend.modules.asms.repository.basic.VehicleRepository;
import com.sunjet.backend.system.entity.DictionaryEntity;
import com.sunjet.backend.system.repository.DictionaryRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.VehicleInfo;
import com.sunjet.dto.asms.basic.VehicleInfoExt;
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
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/9/5.
 * 车辆信息
 */
@Transactional
@Service("vehicleService")
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    /**
     * 条件查询实体列表
     *
     * @param keyword
     * @return
     */
    @Override
    public List<VehicleInfo> findAllByKeyword(String keyword) {
        List<VehicleEntity> entityList = vehicleRepository.findAllByKeyword("%" + keyword + "%");
        return entityListToInfoList(entityList);
    }

    public List<VehicleInfo> entityListToInfoList(List<VehicleEntity> entityList) {
        List<VehicleInfo> infoList = null;
        if (entityList != null) {
            infoList = new ArrayList<>();
            for (VehicleEntity entity : entityList) {
                infoList.add(BeanUtils.copyPropertys(entity, new VehicleInfo()));
            }
        }
        return infoList;
    }


    //非首保车辆不在查询
    @Override
    public List<VehicleInfo> findAllByKeywordAndFmDateIsNull(String keyWord) {

        List<VehicleEntity> list = vehicleRepository.findAllByKeywordAndFmDateIsNull("%" + keyWord + "%");
        List<VehicleInfo> infoList = null;
        if (list != null && list.size() > 0) {
            infoList = new ArrayList<>();
            for (VehicleEntity entity : list) {
                infoList.add(BeanUtils.copyPropertys(entity, new VehicleInfo()));
            }
        }
        return infoList;
    }

    @Override
    public VehicleInfo findOneByVin(String vin) {
        try {
            VehicleEntity entity = vehicleRepository.findOneByVin(vin);

            return BeanUtils.copyPropertys(entity, new VehicleInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<VehicleInfo> getPageList(PageParam<VehicleInfo> pageParam) {
        //1.查询条件
        VehicleInfo vehicleInfo = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<VehicleEntity> specification = null;
        if (vehicleInfo != null) {
            boolean bl1 = false;
            boolean bl2 = false;
            if (vehicleInfo.getManufactureDateStart() != null && vehicleInfo.getManufactureDateEnd() != null) {
                bl1 = true;
            }
            if (vehicleInfo.getPurchaseDateStart() != null && vehicleInfo.getPurchaseDateEnd() != null) {
                bl2 = true;
            }
            specification = Specifications.<VehicleEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(vehicleInfo.getVin()), "vin", "%" + vehicleInfo.getVin() + "%")
                    .like(StringUtils.isNotEmpty(vehicleInfo.getVsn()), "vsn", "%" + vehicleInfo.getVsn() + "%")
                    .like(StringUtils.isNotEmpty(vehicleInfo.getVehicleModel()), "vehicleModel", "%" + vehicleInfo.getVehicleModel() + "%")
                    .like(StringUtils.isNotEmpty(vehicleInfo.getSeller()), "seller", "%" + vehicleInfo.getSeller() + "%")
                    .eq(StringUtils.isNotEmpty(vehicleInfo.getDealerName()), "dealerName", vehicleInfo.getDealerName())
                    .eq(StringUtils.isNotEmpty(vehicleInfo.getProvinceName()), "provinceName", vehicleInfo.getProvinceName())
                    .eq(StringUtils.isNotEmpty(vehicleInfo.getServiceManager()), "serviceManager", vehicleInfo.getServiceManager())
//                    .like(StringUtils.isNotEmpty(vehicleInfo.getProvince()), "province", "%" + vehicleInfo.getProvince() + "%")
                    .between(bl1, "manufactureDate", new Range<Date>(vehicleInfo.getManufactureDateStart(), vehicleInfo.getManufactureDateEnd()))
                    .between(bl2, "purchaseDate", new Range<Date>(vehicleInfo.getPurchaseDateStart(), vehicleInfo.getPurchaseDateEnd()))
                    .build();
        }

        //3.执行查询
        Page<VehicleEntity> pages = vehicleRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<VehicleInfo> rows = new ArrayList<>();
        for (VehicleEntity entity : pages.getContent()) {
            VehicleInfo info = BeanUtils.copyPropertys(entity, new VehicleInfo());
            rows.add(info);
        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    /**
     * 通过objId 查找速报车辆的信息
     *
     * @param objIds
     * @return
     */
    @Override
    public List<VehicleInfo> findByVehicleId(ArrayList<String> objIds) {
        try {
            List<VehicleEntity> byVehicleId = vehicleRepository.findByVehicleId(objIds);
            List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            if (byVehicleId != null) {
                for (VehicleEntity entity : byVehicleId) {
                    VehicleInfo info = BeanUtils.copyPropertys(entity, new VehicleInfo());
                    vehicleInfoList.add(info);
                }
            }
            return vehicleInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    /**
     * 保存一个实体
     *
     * @param iconsInfo
     * @return
     */
    @Override
    public VehicleInfo save(VehicleInfo iconsInfo) {
        try {
            VehicleEntity entity = vehicleRepository.save(BeanUtils.copyPropertys(iconsInfo, new VehicleEntity()));
            return BeanUtils.copyPropertys(entity, iconsInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 iconsInfo 对象
     *
     * @param iconsInfo
     * @return
     */
    @Override
    public boolean delete(VehicleInfo iconsInfo) {
        try {
            VehicleEntity entity = BeanUtils.copyPropertys(iconsInfo, new VehicleEntity());
            vehicleRepository.delete(entity);
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
            vehicleRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过 objId 查询一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public VehicleInfo findOne(String objId) {
        try {
            VehicleEntity entity = vehicleRepository.findOne(objId);

            return BeanUtils.copyPropertys(entity, new VehicleInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据vin数组，获取对应的VehicleEntity列表
     *
     * @param vins
     * @return
     */
    @Override
    public List<VehicleInfo> findAllByVinIn(List<String> vins) {
        try {
            List<VehicleEntity> entities = vehicleRepository.findAllByVinIn(vins);
            List<VehicleInfo> infos = new ArrayList<>();
            entities.forEach(entity -> {
                infos.add(BeanUtils.copyPropertys(entity, new VehicleInfo()));
            });
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 导入车辆
     *
     * @param vehicles
     * @return
     */
    @Transactional(value = Transactional.TxType.NEVER)
    @Override
    public List<VehicleInfoExt> importVehicles(List<VehicleInfoExt> vehicles) {
        List<VehicleInfoExt> incorrectInfos = new ArrayList<>();

        List<DictionaryEntity> deList = dictionaryRepository.findListByParentCode("15000");


        vehicles.forEach(info -> {

            if (StringUtils.isBlank(info.getVehicleModel())) {
                info.setErr(5);
                incorrectInfos.add(info);
            } else {
                try {
                    VehicleEntity entity = BeanUtils.copyPropertys(info, new VehicleEntity());
                    if (entity.getVin() != null && !entity.getVin().equals("")) {
                        //启用
                        entity.setEnabled(true);
                        vehicleRepository.save(entity);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    info.setErr(1);
                    incorrectInfos.add(info);
                }
            }
        });

        return incorrectInfos;
    }

    @Transactional(value = Transactional.TxType.NEVER)
    @Override
    public List<VehicleInfoExt> modifyVehicles(List<VehicleInfoExt> infoExts) {
        List<VehicleInfoExt> incorrectInfos = new ArrayList<>();

        List<String> vins = infoExts.stream().map(v -> v.getVin()).collect(Collectors.toList());
        System.out.println(vins);

        List<VehicleEntity> entities = vehicleRepository.findAllByVinIn(vins);
        infoExts.forEach(infoExt -> {
            List<VehicleEntity> list = entities.stream().filter(e -> e.getVin().equals(infoExt.getVin())).collect(Collectors.toList());
            if (list == null || list.size() <= 0) {
                infoExt.setErr(1);
                incorrectInfos.add(infoExt);
            } else {
                VehicleEntity entity = list.get(0);
                entity.setVsn(infoExt.getVsn());
                entity.setVehicleModel(infoExt.getVehicleModel());
                entity.setEngineModel(infoExt.getEngineModel());
                entity.setEngineNo(infoExt.getEngineNo());
                entity.setManufactureDate(infoExt.getManufactureDate());
                entity.setProductDate(infoExt.getProductDate());
                entity.setSeller(infoExt.getSeller());
                entity.setPurchaseDate(infoExt.getPurchaseDate());
                entity.setOwnerName(infoExt.getOwnerName());
                entity.setAddress(infoExt.getAddress());
                entity.setPhone(infoExt.getPhone());
                entity.setMobile(infoExt.getMobile());

                if (StringUtils.isBlank(infoExt.getVehicleModel())) {
                    infoExt.setErr(5);
                    incorrectInfos.add(infoExt);
                } else {
                    try {
                        vehicleRepository.save(entity);
                    } catch (Exception e) {
                        infoExt.setErr(2);
                        incorrectInfos.add(infoExt);
                    }
                }
            }
        });

        return incorrectInfos;
    }
}

