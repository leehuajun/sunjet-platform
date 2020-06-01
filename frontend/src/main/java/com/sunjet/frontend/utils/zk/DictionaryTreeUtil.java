package com.sunjet.frontend.utils.zk;


import com.sunjet.dto.system.admin.DictionaryInfo;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class DictionaryTreeUtil {
    private static String ROOT_BACKGROUND = "rgb(230,230,230)";
    private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public static CustomTreeNode getRoot(List<DictionaryInfo> dictionaryEetityList) {
        CustomTreeNode<DictionaryInfo> root = new CustomTreeNode<DictionaryInfo>(null, new ArrayList<TreeNode<DictionaryInfo>>(), true);
        for (DictionaryInfo dictionaryInfo : dictionaryEetityList) {
            if (dictionaryInfo.getParent() == null) {    // 表示是根节点
                //dictionaryInfo.setBackground(ROOT_BACKGROUND);
                //if (menuEntity.getIcon().trim().equals(""))
                //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                CustomTreeNode<DictionaryInfo> node;
                if (isLeaf(dictionaryInfo, dictionaryEetityList)) {     // 叶节点
                    node = new CustomTreeNode<DictionaryInfo>(dictionaryInfo);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(ConfigHelper.DEFAULT_TREENODE_ICON);
                    node = new CustomTreeNode<DictionaryInfo>(dictionaryInfo, new ArrayList<TreeNode<DictionaryInfo>>(), false);
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
                getNode(node, dictionaryEetityList);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param dictionaryInfo
     * @param dictionaryEntityList
     * @return 叶节点, 返回true, 否则返回false
     */
    public static Boolean isLeaf(DictionaryInfo dictionaryInfo, List<DictionaryInfo> dictionaryEntityList) {

        for (DictionaryInfo dictionaryInfo1 : dictionaryEntityList) {
            if (dictionaryInfo1.getParent() != null) {
                if (dictionaryInfo1.getParent().getObjId().equals(dictionaryInfo.getObjId())) {
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
    private static void getNode(CustomTreeNode<DictionaryInfo> parent, List<DictionaryInfo> dictionaryInfos) {
        DictionaryInfo parentEntity = (DictionaryInfo) parent.getData();
        for (DictionaryInfo dictionaryInfo : dictionaryInfos) {
            if (dictionaryInfo.getParent() != null) {
                if (dictionaryInfo.getParent().getObjId().equals(parentEntity.getObjId())) {
                    dictionaryInfo.setBackground(NODE_BACKGROUND);
                    //if (menuEntity.getIcon().trim().equals(""))
                    //    menuEntity.setIcon(CacheManager.getConfigValue("treenode_icon"));
                    CustomTreeNode<DictionaryInfo> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                    if (isLeaf(dictionaryInfo, dictionaryInfos)) {
                        child = new CustomTreeNode<DictionaryInfo>(dictionaryInfo);
                    } else {
                        child = new CustomTreeNode<DictionaryInfo>(dictionaryInfo, new ArrayList<TreeNode<DictionaryInfo>>(), false);
                    }
                    parent.add(child);
                    if (!isLeaf(dictionaryInfo, dictionaryInfos))
                        getNode(child, dictionaryInfos);
                }
            }

        }
    }
}
