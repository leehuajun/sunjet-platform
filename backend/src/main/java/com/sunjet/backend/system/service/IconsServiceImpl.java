package com.sunjet.backend.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.system.entity.IconEntity;
import com.sunjet.backend.system.repository.IconsRepository;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.system.admin.IconInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 图标
 */
@Transactional
@Service("iconService")
public class IconsServiceImpl implements IconsService {

    @Autowired
    private IconsRepository iconsRepository;

    /**
     * 删除 -->  通过 objId
     *
     * @param objId
     * @return
     */
    @Override
    public boolean delete(String objId) {
        try {
            iconsRepository.delete(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    @Override
    public PageResult<IconEntity> getPageList(PageParam<IconEntity> pageParam) {

        //1.查询条件
        IconEntity iconEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<IconEntity> specification = null;
        if (iconEntity != null) {
            specification = Specifications.<IconEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(iconEntity.getName()), "name", iconEntity.getName())
                    .build();
        }

        //3.执行查询
        Page<IconEntity> pages = iconsRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
//        List<IconInfo> rows = new ArrayList<>();
//        for (IconEntity entity : pages.getContent()) {
//            IconInfo info = entityToInfo(entity);
//            rows.add(info);
//        }

        //5.组装分页信息及集合信息
        //PageResult<IconsInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    @Override
    public List<IconEntity> findAll() {
        try {
            return this.iconsRepository.findAll();
//            List<IconEntity> list = this.iconsRepository.findAll();
//            List<IconInfo> infos = null;
//            infos = new ArrayList<>();
//            if(list!=null&&list.size()>0) {
//                for (IconEntity iconsEntity : list) {
//                    infos.add(entityToInfo(iconsEntity));
//                }
//            }
//            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * info 转为 实体
     *
     * @param iconInfo
     * @return
     */
//    private IconEntity infoToEntity(IconInfo iconInfo) {
//        return IconEntity.IconEntityBuilder
//                .anIconEntity()
//                .withCreaterId(iconInfo.getCreaterId())
//                .withCreaterName(iconInfo.getCreaterName())
//                .withModifierId(iconInfo.getModifierId())
//                .withName(iconInfo.getName())
//                .withModifierName(iconInfo.getModifierName())
//                .withObjId(iconInfo.getObjId())
//                .withEnabled(iconInfo.getEnabled())
//                .build();
//    }

    /**
     * entity 转为 info
     *
     * @param iconEntity
     * @return
     */
//    private IconInfo entityToInfo(IconEntity iconEntity) {
//        return IconInfo.iconsInfoBuilder
//                .aniconsInfo()
//                .withObjId(iconEntity.getObjId())
//                .withCreaterId(iconEntity.getCreaterId())
//                .withEnabled(iconEntity.getEnabled())
//                .withName(iconEntity.getName())
//                .withCreaterName(iconEntity.getCreaterName())
//                .withModifierId(iconEntity.getModifierId())
//                .withModifierName(iconEntity.getModifierName())
//                .build();
//    }
}