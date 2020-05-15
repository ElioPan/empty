package com.ev.custom.mqtt;/**
 * @Author xy
 * @Date 2020/5/15 10:54
 * @Description
 */

import java.util.Date;

/**
 * 文件名称： com.ev.custom.mqtt.CacheItem.java</br>
 * 初始作者： xy</br>
 * 创建日期： 2020/5/15 10:54</br>
 * 功能说明： TODO <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者        日期       修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2020-2021 .All rights reserved.<br/>
 */
public class CacheItem {
    private Date createTime = new Date();//创建缓存的时间
    private long expireTime = 1;//缓存期满的时间
    private Object entity;//缓存的实体

    public CacheItem(Object obj,long expires){
        this.entity = obj;
        this.expireTime = expires;
    }

    public boolean isExpired(){
        return (expireTime != -1 && System.currentTimeMillis()-createTime.getTime() > expireTime);
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
