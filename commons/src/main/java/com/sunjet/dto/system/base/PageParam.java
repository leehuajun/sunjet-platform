package com.sunjet.dto.system.base;

//import com.sunjet.utils.common.CommonHelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wfb on 17-5-5.
 * 分页查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam<T> implements Serializable {


    private int page = 0;//页码(当前是第几页)
    //    private int pageSize = CommonHelper.baseGridHeight / 27;   //页面大小(一页显示几行)
    private int pageSize = 50;   //页面大小(一页显示几行)
    //    private List<> orders = new ArrayList<>();
    private String order = "desc";//排序  asc desc
    private String orderName = "createdTime";//排序字段

    private T infoWhere;//实体条件
}
