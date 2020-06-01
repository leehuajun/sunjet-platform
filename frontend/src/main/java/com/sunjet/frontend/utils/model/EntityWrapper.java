package com.sunjet.frontend.utils.model;

/**
 * @author lhj
 * @create 2016-02-25 下午5:26
 */
public class EntityWrapper<T> {
    private T entity;
    private Boolean selected;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T model) {
        this.entity = model;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public EntityWrapper() {
    }

    public EntityWrapper(T model, Boolean selected) {
        this.entity = model;
        this.selected = selected;
    }
}