package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.entity.IconEntity;
import com.sunjet.backend.system.service.IconsService;
import com.sunjet.dto.system.admin.IconInfo;
import com.sunjet.dto.system.base.PageParam;
import com.sunjet.dto.system.base.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by hhn on 2017/7/21.
 */
@Slf4j
@RestController
@RequestMapping("/icons")
public class IconsController {

    @Autowired
    private IconsService iconsService;

    @PostMapping("/findAll")
    public List<IconEntity> findAll() {
        return iconsService.findAll();
    }

    @DeleteMapping("/icon/delete")
    public void delete(String iconId) {
        boolean icon = iconsService.delete(iconId);
    }

    /**
     * 分页查询
     *
     * @param pageParam
     * @return
     */
    @PostMapping("/getPageList")
    public PageResult<IconEntity> getPageList(@RequestBody PageParam<IconEntity> pageParam) {
        return iconsService.getPageList(pageParam);
    }
}
