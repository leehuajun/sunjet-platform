package com.sunjet.backend.modules.asms.service.basic;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.MaintainEntity;
import com.sunjet.backend.modules.asms.entity.basic.MaintainTypeEntity;
import com.sunjet.backend.modules.asms.entity.basic.VehiclePlatformEntity;
import com.sunjet.backend.modules.asms.repository.basic.MaintainRepository;
import com.sunjet.backend.modules.asms.repository.basic.MaintainTypeRepository;
import com.sunjet.backend.modules.asms.repository.basic.VehiclePlatformRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.MaintainInfo;
import com.sunjet.dto.asms.basic.MaintainInfoExt;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/9/12.
 * 项目工时定额
 */
@Transactional
@Service("maintainService")
public class MaintainServiceImpl implements MaintainService {
    @Autowired
    private MaintainRepository maintainRepository;
    @Autowired
    private MaintainTypeRepository maintainTypeRepository;
    @Autowired
    private VehiclePlatformRepository vehiclePlatformRepository;

    /**
     * 获取操作列表集合
     *
     * @return
     */
    @Override
    public List<MaintainInfo> findAll() {
        try {
            List<MaintainEntity> list = this.maintainRepository.findAll();
            List<MaintainInfo> infos = null;
            infos = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (MaintainEntity maintainEntity : list) {
                    infos.add(BeanUtils.copyPropertys(maintainEntity, new MaintainInfo()));
                }
            }
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<MaintainInfo> getPageList(PageParam<MaintainInfo> pageParam) {

        //1.查询条件
        MaintainInfo maintainInfo = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<MaintainEntity> specification = null;
        if (maintainInfo != null) {
            specification = Specifications.<MaintainEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(maintainInfo.getName()), "name", "%" + maintainInfo.getName() + "%")
                    .like(StringUtils.isNotEmpty(maintainInfo.getCode()), "code", "%" + maintainInfo.getCode() + "%")
                    .eq(StringUtils.isNotEmpty(maintainInfo.getVehicleModelId()), "vehicleModelId", maintainInfo.getVehicleModelId())
                    .eq(StringUtils.isNotEmpty(maintainInfo.getVehicleModelName()), "vehicleModelName", maintainInfo.getVehicleModelName())
                    .eq(StringUtils.isNotEmpty(maintainInfo.getVehicleSystemId()), "vehicleSystemId", maintainInfo.getVehicleSystemId())
                    .eq(StringUtils.isNotEmpty(maintainInfo.getVehicleSystemName()), "vehicleSystemName", maintainInfo.getVehicleSystemName())
                    .eq(StringUtils.isNotEmpty(maintainInfo.getVehicleSubSystemId()), "vehicleSubSystemId", maintainInfo.getVehicleSubSystemId())
                    .eq(StringUtils.isNotEmpty(maintainInfo.getVehicleSubSystemName()), "vehicleSubSystemName", maintainInfo.getVehicleSubSystemName())
                    .build();
        }


        //3.执行查询
        Page<MaintainEntity> pages = maintainRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<MaintainInfo> rows = new ArrayList<>();
        for (MaintainEntity entity : pages.getContent()) {
            rows.add(BeanUtils.copyPropertys(entity, new MaintainInfo()));
        }


        //5.组装分页信息及集合信息
        //PageResult<MaintainInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(rows, pages, pageParam);
    }

    @Override
    public MaintainInfo findOneById(String objId) {
        try {
            MaintainEntity entity = maintainRepository.findOne(objId);
            return BeanUtils.copyPropertys(entity, new MaintainInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存 实体
     *
     * @param maintainInfo
     * @return
     */
    @Override
    public MaintainInfo save(MaintainInfo maintainInfo) {
        try {
            MaintainEntity entity = maintainRepository.save(BeanUtils.copyPropertys(maintainInfo, new MaintainEntity()));
            return BeanUtils.copyPropertys(entity, new MaintainInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除 --> 通过 maintainInfo 对象
     *
     * @param maintainInfo
     * @return
     */
    @Override
    public boolean delete(MaintainInfo maintainInfo) {
        try {
            MaintainEntity entity = BeanUtils.copyPropertys(maintainInfo, new MaintainEntity());
            maintainRepository.delete(entity);
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
    public boolean deleteByObjId(String objId) {
        try {
            maintainRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关键字搜素
     *
     * @param maintainEntity
     * @return
     */
    @Override
    public List<MaintainEntity> findAllByFilter(MaintainEntity maintainEntity) {
        return maintainRepository.findAllByFilter("%" + maintainEntity.getCode() + "%", "%" + maintainEntity.getName() + "%", "%" + maintainEntity.getVehicleModelName() + "%", "%" + maintainEntity.getVehicleSystemName() + "%", "%" + maintainEntity.getVehicleSubSystemName() + "%");
    }

    @Transactional(value = Transactional.TxType.NEVER)
    @Override
    public List<MaintainInfoExt> importData(List<MaintainInfoExt> infos) {

        List<MaintainInfoExt> incorrectInfos = new ArrayList<>();
        List<MaintainInfoExt> correctInfos = new ArrayList<>();
        List<MaintainTypeEntity> list = maintainTypeRepository.findAll();
        List<VehiclePlatformEntity> platformEntities = vehiclePlatformRepository.findAll();
        Map<String, List<MaintainTypeEntity>> map = new HashMap<>();

        list.forEach(item -> {
            String key = StringUtils.isBlank(item.getParentId()) ? "top" : item.getParentId();
            if (map.get(key) == null) {
                List<MaintainTypeEntity> ios = new ArrayList<>();
                map.put(key, ios);
                map.get(key).add(item);
            } else {
                map.get(key).add(item);
            }
        });

        infos.forEach(info -> {
            // 车型平台或者车辆系统或者子系统为空，表示该条记录有脏数据
            if (StringUtils.isBlank(info.getVehicleModelName())
                    || StringUtils.isBlank(info.getVehicleSystemName())
                    || StringUtils.isBlank(info.getVehicleSubSystemName())) {
                incorrectInfos.add(info);
            } else {

                List<VehiclePlatformEntity> vpeList = platformEntities.stream().filter(entity -> entity.getName().equals(info.getVehicleModelName()))
                        .collect(Collectors.toList());
                if (vpeList.size() > 0) {
                    info.setVehicleModelId(vpeList.get(0).getObjId());
                }

                map.get("top").forEach(model1 -> {
                    if (model1.getName().equals(info.getVehicleSystemName())) {
                        info.setVehicleSystemId(model1.getObjId());

                        map.get(info.getVehicleSystemId()).forEach(model2 -> {
                            if (model2.getName().equals(info.getVehicleSubSystemName())) {
                                info.setVehicleSubSystemId(model2.getObjId());
                                correctInfos.add(info);
                            }
                        });
                    }
                });


                if (StringUtils.isBlank(info.getVehicleModelId())) {
                    info.setErr(1);
                    incorrectInfos.add(info);
                } else if (StringUtils.isBlank(info.getVehicleSystemId())) {
                    info.setErr(2);
                    incorrectInfos.add(info);
                } else if (StringUtils.isBlank(info.getVehicleSubSystemId())) {
                    info.setErr(3);
                    incorrectInfos.add(info);
                }
            }
        });
        System.out.println("接收到的info数量：" + infos.size());
        System.out.println("正确的info数量：" + correctInfos.size());
        System.out.println("正确的infos：" + correctInfos);
        System.out.println("错误的info数量：" + incorrectInfos.size());
        System.out.println("错误的infos：" + incorrectInfos);

        correctInfos.forEach(info -> {
            try {
                MaintainEntity entity = BeanUtils.copyPropertys(info, new MaintainEntity());
                maintainRepository.save(entity);
            } catch (Exception ex) {
                info.setErr(4);
                incorrectInfos.add(info);
            }
        });
        System.out.println("错误的info数量：" + incorrectInfos.size());


        return incorrectInfos;
    }

    /**
     * 通过 objId 查找一个实体
     *
     * @param objId
     * @return
     */
    @Override
    public MaintainInfo findOne(String objId) {
        try {
            MaintainEntity maintainEntity = maintainRepository.findOne(objId);
            return BeanUtils.copyPropertys(maintainEntity, new MaintainInfo());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
