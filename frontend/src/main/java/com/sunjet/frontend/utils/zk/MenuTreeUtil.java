package com.sunjet.frontend.utils.zk;


import com.sunjet.dto.system.admin.MenuInfo;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class MenuTreeUtil {
    private static String ROOT_BACKGROUND = "rgb(230,230,230)";
    private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public static CustomTreeNode getRoot(List<MenuInfo> menuEntityList) {
        CustomTreeNode<MenuInfo> root = new CustomTreeNode<MenuInfo>(null, new ArrayList<TreeNode<MenuInfo>>(), true);
        for (MenuInfo menuInfo : menuEntityList) {
            if (menuInfo.getParent() == null) {    // 表示是根节点
                menuInfo.setBackground(ROOT_BACKGROUND);
                //if (menuEntity.getIcon().trim().equals(""))
                //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                CustomTreeNode<MenuInfo> node;
                if (isLeaf(menuInfo, menuEntityList)) {     // 叶节点
                    node = new CustomTreeNode<MenuInfo>(menuInfo);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
                    node = new CustomTreeNode<MenuInfo>(menuInfo, new ArrayList<TreeNode<MenuInfo>>(), menuInfo.getOpen());
                }
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);
//                menuEntity.setChildrenCount(childrenCount);
//                if(childrenCount==0) {     // 叶节点
//                    node = new CustomTreeNode<MenuEntity>(menuEntity);
//                    //menuEntity.setIcon("");
//                }else{   // 非 页节点
//                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
//                    node = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(),menuEntity.getOpen());
//                }
                root.add(node);
                getNode(node, menuEntityList);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param menuInfo
     * @param menuEntityList
     * @return 叶节点, 返回true, 否则返回false
     */
    public static Boolean isLeaf(MenuInfo menuInfo, List<MenuInfo> menuEntityList) {

        for (MenuInfo menu : menuEntityList) {
            if (menu.getParent() != null) {
                if (menu.getParent().getObjId().equals(menuInfo.getObjId())) {
                    return false;
                }
            }
        }

        return true;


    }

    //    public static Integer getChildrenCount(MenuEntity menuEntity,List<MenuEntity> menuEntityList){
//        Integer childCount = 0;
//        for(MenuEntity menu: menuEntityList){
//            if(menu.getParent()==menuEntity){
//                childCount++;
//            }
//        }
//        return childCount;
//    }
    private static void getNode(CustomTreeNode<MenuInfo> parent, List<MenuInfo> menuInfoList) {
        MenuInfo parentEntity = (MenuInfo) parent.getData();
        for (MenuInfo menuInfo : menuInfoList) {
            if (menuInfo.getParent() != null) {
                if (menuInfo.getParent().getObjId().equals(parentEntity.getObjId())) {
                    menuInfo.setBackground(NODE_BACKGROUND);
                    //if (menuEntity.getIcon().trim().equals(""))
                    //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                    CustomTreeNode<MenuInfo> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                    if (isLeaf(menuInfo, menuInfoList)) {
                        child = new CustomTreeNode<MenuInfo>(menuInfo);
                    } else {
                        child = new CustomTreeNode<MenuInfo>(menuInfo, new ArrayList<TreeNode<MenuInfo>>(), menuInfo.getOpen());
                    }
                    parent.add(child);
                    if (!isLeaf(menuInfo, menuInfoList))
                        getNode(child, menuInfoList);
                }
            }

        }
    }
}
