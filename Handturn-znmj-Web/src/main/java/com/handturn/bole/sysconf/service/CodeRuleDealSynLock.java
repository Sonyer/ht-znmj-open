package com.handturn.bole.sysconf.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

/**
 * 库存用户ID 值同步线程
 * @param <T>
 */
public abstract class CodeRuleDealSynLock<T> {
    /**
     * @Fields signMap : TODO(存储一个同步信号量，key表示同步的值)
     */
    private static ConcurrentMap<Object, Semaphore> signMap = new ConcurrentHashMap<Object, Semaphore>();

    /**
     * 参数
     */
    public Object paramObj;

    /**
     * 返回结果
     */
    private Object resultObj;

    /**
     * 要执行的方法
     */
    public abstract Object run();
    /**
     * 开始执行操作
     * @param value
     */
    public Object startWork(String value,Object paramObj) {
        Semaphore se = putMapAndGetSemaphore(value);
        this.paramObj = paramObj;
        try {
            se.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.resultObj = run();
        releaseLockMap(value);
        se.release();
        return resultObj;
    }
    /**
     * 释放map
     * @param
     */
    public void releaseLockMap(String a) {
        Semaphore se = signMap.get(a);
        if (se != null) {
            signMap.remove(a);
        }
    }
    /**
     * 放入同步值 获取同步信号量
     * @param
     * @return
     */
    public Semaphore putMapAndGetSemaphore(String value) {
        Semaphore se = signMap.get(value);
        if (se == null) {
            se = new Semaphore(1);
            signMap.put(value, se);
        }
        return signMap.get(value);
    }
}