package com.sunjet.backend.controller.system;

import com.sunjet.backend.system.service.MenuService;
import com.sunjet.dto.system.admin.MenuInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by SUNJET_WS on 2017/7/20.
 * 系统
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    /**
     * 获取所有的菜单
     *
     * @return
     */
    @ApiOperation(value = "查询所有菜单", notes = "根据用户登录ID获取菜单信息")
    @PostMapping("/menusAll")
    public List<MenuInfo> getMenusAll() {

        return menuService.findAll();
    }

    /**
     * 通过objId获取一个菜单
     *
     * @param objId
     * @return
     */
    @PostMapping("/findOneMenuById")
    public MenuInfo findOneMenuById(@RequestBody String objId) {

        return menuService.findOne(objId);
    }


    /**
     * 保存实体
     *
     * @param menuInfo
     * @return
     */
    @PostMapping("/saveMenu")
    public MenuInfo saveMenu(@RequestBody MenuInfo menuInfo) {

        return menuService.save(menuInfo);
    }


    /**
     * 删除实体
     *
     * @param objId
     * @return
     */
    @DeleteMapping("/delete")
    public boolean delete(@RequestBody String objId) {
        return menuService.delete(objId);
    }


    /**
     * 通过URL查菜单
     *
     * @param url
     * @return
     */
    @ApiOperation(value = "查一个菜单实体", notes = "通过url查菜单实体")
    @PostMapping("findMenuByUrl")
    public MenuInfo findMenuByUrl(@RequestBody String url) {
        return menuService.findMenuByUrl(url);
    }

}
