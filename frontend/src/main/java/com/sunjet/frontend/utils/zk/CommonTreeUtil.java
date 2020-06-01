package com.sunjet.frontend.utils.zk;

import com.sunjet.dto.system.base.TreeNodeInfo;
import org.zkoss.zul.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:32
 */
public class CommonTreeUtil<T> {
    private static String ROOT_BACKGROUND = "rgb(230,230,230)";
    private static String NODE_BACKGROUND = "rgb(249,249,249)";

    public CommonTreeUtil() {
    }


    public CustomTreeNode getRoot(List<T> nodeList) {
        CustomTreeNode<T> root = new CustomTreeNode<T>(null, new ArrayList<TreeNode<T>>(), true);
        for (T t : nodeList) {
            TreeNodeInfo nodeEntity = (TreeNodeInfo) t;
            if (nodeEntity.getParent() == null || nodeEntity.getParent().toString().equals("")) {    // 表示是根节点
                CustomTreeNode<T> node;
                if (isLeaf(t, nodeList)) {     // 叶节点
                    node = new CustomTreeNode<T>(t);
                    //menuEntity.setIcon("");
                } else {   // 非 页节点
                    //menuEntity.setIcon(Config.DEFAULT_TREENODE_ICON);
                    node = new CustomTreeNode<T>(t, new ArrayList<TreeNode<T>>(), false);
                }
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);
//                menuEntity.setChildrenCount(childrenCount);
//                if(childrenCount==0) {     // 叶节点
//                    node = new CustomTreeNode<MenuEntity>(menuEntity);
//                    //menuEntity.setIcon("");
//                }else{   // 非 页节点
//                    //menuEntity.setIcon(Config.DEFAULT_TREENODE_ICON);
//                    node = new CustomTreeNode<MenuEntity>(menuEntity, new ArrayList<TreeNode<MenuEntity>>(),menuEntity.getOpen());
//                }
                root.add(node);
                getNode(node, nodeList);
            }
        }
        return root;
    }

    /**
     * 判断是否是叶节点
     *
     * @param node
     * @param nodeList
     * @return 叶节点, 返回true, 否则返回false
     */
    public Boolean isLeaf(T node, List<T> nodeList) {
        for (T t : nodeList) {
            if ((((TreeNodeInfo) t).getParent()) == ((TreeNodeInfo) node)) {
                return false;
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
    private void getNode(CustomTreeNode<T> parent, List<T> nodeList) {
        for (T t : nodeList) {
            if (((TreeNodeInfo) t).getParent() == ((TreeNodeInfo) parent.getData())) {

                CustomTreeNode<T> child;
//                Integer childrenCount = getChildrenCount(menuEntity,menuEntityList);

                if (isLeaf(t, nodeList)) {
                    child = new CustomTreeNode<T>(t);
                } else {
                    child = new CustomTreeNode<T>(t, new ArrayList<TreeNode<T>>(), false);
                }
                parent.add(child);
                if (!isLeaf(t, nodeList))
                    getNode(child, nodeList);
            }
        }
    }
}
