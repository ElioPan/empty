package com.ev.common.domain;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class MenuTree<T> {
    /**
     * 节点ID
     */
    private String id;
    /**
     * 显示节点文本
     */
    private String text;
    /**
     * 节点的子节点
     */
    private List<MenuTree<T>> children = new ArrayList<MenuTree<T>>();
    /**
     * 父ID
     */
    private String parentId;
    /**
     * 是否有父节点
     */
    private boolean hasParent = false;
    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路由
     */
    private String routePath;
    /**
     *顺序号
     */
    private String orderNumber;
    /**
     * 类型
     */
    private String type;
    /**
     * 地址
     */
    private String url;

    /**
     * 终端
     */
    private String terminal;
    /**
     * 图标地址
     */
    private String iconUrl;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MenuTree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTree<T>> children) {
        this.children = children;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean isParent) {
        this.hasParent = isParent;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setChildren(boolean isChildren) {
        this.hasChildren = isChildren;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public MenuTree(String id, String text, List<MenuTree<T>> children, boolean isParent, boolean isChildren, String parentID, String icon, String routePath, String orderNumber, String type, String url, String terminal, String iconUrl) {
        super();
        this.id = id;
        this.text = text;
        this.children = children;
        this.hasParent = isParent;
        this.hasChildren = isChildren;
        this.parentId = parentID;
        this.icon = icon;
        this.routePath = routePath;
        this.orderNumber = orderNumber;
        this.type = type;
        this.url = url;
        this.terminal = terminal;
        this.iconUrl = iconUrl;
    }

    public MenuTree() {
        super();
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }
}
