package com.sunjet.frontend.utils.zk;

import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.TreeNode;

import java.util.List;

/**
 * @author lhj
 * @create 2015-12-22 下午5:19
 */
public class CustomTreeNode<T> extends DefaultTreeNode {
    private static final long serialVersionUID = -7012663776755277499L;

    private boolean open = false;

    public CustomTreeNode(T data, List<TreeNode<T>> children) {
        super(data, children);
    }

    public CustomTreeNode(T data, List<TreeNode<T>> children, boolean open) {
        super(data, children);
        setOpen(open);
    }

    public CustomTreeNode(T data) {
        super(data);

    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }


}