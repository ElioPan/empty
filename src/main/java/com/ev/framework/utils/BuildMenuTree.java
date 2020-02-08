package com.ev.framework.utils;

import com.ev.common.domain.MenuTree;

import java.util.ArrayList;
import java.util.List;

public class BuildMenuTree {

    public static <T> MenuTree<T> build(List<MenuTree<T>> nodes) {

        if (nodes == null) {
            return null;
        }
        List<MenuTree<T>> topNodes = new ArrayList<MenuTree<T>>();

        for (MenuTree<T> children : nodes) {

            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                topNodes.add(children);

                continue;
            }

            for (MenuTree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setChildren(true);
                    continue;
                }
            }

        }
        MenuTree<T> root = new MenuTree<T>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setId("-1");
            root.setParentId("");
            root.setHasParent(false);
            root.setChildren(true);
            root.setChildren(topNodes);
            root.setText("顶级节点");
        }

        return root;
    }

    public static <T> List<MenuTree<T>> buildList(List<MenuTree<T>> nodes, String idParam) {
        if (nodes == null) {
            return null;
        }
        List<MenuTree<T>> topNodes = new ArrayList<MenuTree<T>>();
        for (MenuTree<T> children : nodes) {
            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);
                continue;
            }
            for (MenuTree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setChildren(true);
                    continue;
                }
            }

        }
        return topNodes;
    }

}