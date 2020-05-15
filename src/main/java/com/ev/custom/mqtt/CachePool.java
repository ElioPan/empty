package com.ev.custom.mqtt;/**
 * @Author xy
 * @Date 2020/5/15 10:54
 * @Description
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件名称： com.ev.custom.mqtt.CachePool.java</br>
 * 初始作者： xy</br>
 * 创建日期： 2020/5/15 10:54</br>
 * 功能说明： TODO <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者        日期       修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2020-2021 .All rights reserved.<br/>
 */
public class CachePool {
    private static CachePool instance;//缓存池唯一实例
    private static Map<String,Object> cacheItems;//缓存Map

    private CachePool(){
        cacheItems = new HashMap<String,Object>();
    }
    /**
     * 得到唯一实例
     * @return
     */
    public synchronized static CachePool getInstance(){
        if(instance == null){
            instance = new CachePool();
        }
        return instance;
    }
    /**
     * 获取缓存列表
     */
    public synchronized Map<String,Object> getAllCacheItems(){
        return cacheItems;
    }

    /**
     * 清除所有Item缓存
     */
    public synchronized void clearAllItems(){
        cacheItems.clear();
    }
    /**
     * 获取缓存实体
     * @param name
     * @return
     */
    public synchronized Object getCacheItem(String name){
        if(!cacheItems.containsKey(name)){
            return null;
        }
        CacheItem cacheItem = (CacheItem) cacheItems.get(name);
        if(cacheItem.isExpired()){
            return null;
        }
        return cacheItem.getEntity();
    }
    /**
     * 存放缓存信息
     * @param name
     * @param obj
     * @param expires
     */
    public synchronized void putCacheItem(String name,Object obj,long expires){
        if(!cacheItems.containsKey(name)){
            cacheItems.put(name, new CacheItem(obj, expires));
        }
        CacheItem cacheItem = (CacheItem) cacheItems.get(name);
        cacheItem.setCreateTime(new Date());
        cacheItem.setEntity(obj);
        cacheItem.setExpireTime(expires);
    }
    public synchronized void putCacheItem(String name,Object obj){
        putCacheItem(name,obj,-1);
    }

    /**
     * 移除缓存数据
     * @param name
     */
    public synchronized void removeCacheItem(String name){
        if(!cacheItems.containsKey(name)){
            return;
        }
        cacheItems.remove(name);
    }

    /**
     * 获取缓存数据的数量
     * @return
     */
    public int getSize(){
        return cacheItems.size();
    }
}
