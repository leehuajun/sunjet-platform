package com.sunjet.backend.modules.asms.service.basic;


import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;
import com.sunjet.backend.modules.asms.entity.basic.NoticeEntity;
import com.sunjet.backend.modules.asms.repository.basic.NoticeRepository;
import com.sunjet.backend.utils.BeanUtils;
import com.sunjet.backend.utils.PageUtil;
import com.sunjet.dto.asms.basic.NoticeInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 * 公告信息
 */
@Transactional
@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * 保存 实体
     *
     * @param notice
     * @return
     */
    @Override
    public NoticeEntity save(NoticeEntity notice) {
        try {
//            NoticeEntity entity = noticeRepository.save(BeanUtils.copyPropertys(noticeInfo, new NoticeEntity()));
            return noticeRepository.save(notice);
//            return BeanUtils.copyPropertys(entity, new NoticeInfo());
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
    public boolean delete(String objId) {
        try {
            noticeRepository.delete(objId);
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
    public NoticeEntity findOne(String objId) {
        try {
            return noticeRepository.findOne(objId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有的公告
     *
     * @return
     */
    @Override
    public List<NoticeEntity> findAll() {
        return noticeRepository.findAll();
    }

    /**
     * 获取公告分页
     *
     * @param pageParam
     * @return
     */
    @Override
    public PageResult<NoticeEntity> getPageList(PageParam<NoticeEntity> pageParam) {
        //1.查询条件
        NoticeEntity notice = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<NoticeEntity> specification = null;
        if (notice != null) {
            specification = Specifications.<NoticeEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(notice.getTitle()), "title", "%" + notice.getTitle() + "%")
                    .build();
        }

        Sort sort = Sorts.builder()
                .desc("isTop")
                .desc("publishDate")
                .build();
        Pageable pageRequest = new PageRequest(pageParam.getPage(), pageParam.getPageSize(), sort);

        //3.执行查询
        Page<NoticeEntity> pages = noticeRepository.findAll(specification, pageRequest);
//        Page<NoticeEntity> pages = noticeRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //4.数据转换
        List<NoticeEntity> noticeEntityList = pages.getContent();
//        List<NoticeInfo> rows = new ArrayList<>();
//        for (NoticeEntity noticeEntity : noticeEntityList) {
//            rows.add(BeanUtils.copyPropertys(noticeEntity, new NoticeInfo()));
//        }
//
//        Sort sort = Sorts.builder()
//                .desc(StringUtils.isNotBlank(request.getName()), "name")
//                .asc("birthday")
//                .build();


        //5.返回
        return PageUtil.getPageResult(noticeEntityList, pages, pageParam);
    }

    @Override
    public List<NoticeEntity> findLastNotices(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
//        Date today = calendar.getTime();
        return noticeRepository.findLastNotices(calendar.getTime());
    }

}

