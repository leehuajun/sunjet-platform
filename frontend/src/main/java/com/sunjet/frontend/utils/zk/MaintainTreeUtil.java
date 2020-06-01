package com.sunjet.frontend.utils.zk;


import com.sunjet.dto.asms.basic.MaintainTypeInfo;
import com.sunjet.dto.system.admin.MenuInfo;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class MaintainTreeUtil {
    private static String ROOT_BACKGROUND = "rgb(230,230,230)";
    private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public static CustomTreeNode getRoot(List<MaintainTypeInfo> maintainTypeList) {
        CustomTreeNode<MaintainTypeInfo> root = new CustomTreeNode<MaintainTypeInfo>(null, new ArrayList<TreeNode<MaintainTypeInfo>>(), true);
        for (MaintainTypeInfo maintainTypeInfo : maintainTypeList) {
            if (maintainTypeInfo.getParentId() == null || maintainTypeInfo.getParentId().trim().equals("")) {    // 表示是根节点
//                maintainTypeInfo.setBackground(ROOT_BACKGROUND);
                //if (menuEntity.getIcon().trim().equals(""))
                //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                CustomTreeNode<MaintainTypeInfo> node;
                if (isLeaf(maintainTypeInfo, maintainTypeList)) {     // 叶节点
                    node = new CustomTreeNode<MaintainTypeInfo>(maintainTypeInfo);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
                    node = new CustomTreeNode<MaintainTypeInfo>(maintainTypeInfo, new ArrayList<TreeNode<MaintainTypeInfo>>(), false);
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
                getNode(node, maintainTypeList);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param maintainTypeInfo
     * @param maintainTypeInfoList
     * @return 叶节点, 返回true, 否则返回false
     */
    public static Boolean isLeaf(MaintainTypeInfo maintainTypeInfo, List<MaintainTypeInfo> maintainTypeInfoList) {

        for (MaintainTypeInfo maintType : maintainTypeInfoList) {
            if (maintType.getParentId() != null && !maintType.getParentId().trim().equals("")) {
                if (maintType.getParentId().equals(maintainTypeInfo.getObjId())) {
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
    private static void getNode(CustomTreeNode<MaintainTypeInfo> parent, List<MaintainTypeInfo> maintainTypeInfoList) {
        MaintainTypeInfo parentEntity = (MaintainTypeInfo) parent.getData();
        for (MaintainTypeInfo maintainTypeInfo : maintainTypeInfoList) {
            if (maintainTypeInfo.getParentId() != null && !maintainTypeInfo.getParentId().equals("")) {
                if (maintainTypeInfo.getParentId().equals(parentEntity.getObjId())) {
//                    maintainTypeInfo.setBackground(NODE_BACKGROUND);
                    //if (menuEntity.getIcon().trim().equals(""))
                    //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                    CustomTreeNode<MaintainTypeInfo> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                    if (isLeaf(maintainTypeInfo, maintainTypeInfoList)) {
                        child = new CustomTreeNode<>(maintainTypeInfo);
                    } else {
                        child = new CustomTreeNode<>(maintainTypeInfo, new ArrayList<TreeNode<MaintainTypeInfo>>(), false);
                    }
                    parent.add(child);
                    if (!isLeaf(maintainTypeInfo, maintainTypeInfoList))
                        getNode(child, maintainTypeInfoList);
                }
            }

        }
    }
}
