package com.handturn.bole.master.set.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.master.set.entity.NotifySet;

import java.util.List;
import java.util.Set;

/**
 * 智能门禁-通知设置 Service接口
 *
 * @author Eric
 * @date 2020-06-09 18:43:12
 */
public interface INotifySetService extends IService<NotifySet> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param setNotify setNotify
     * @return ICustomPage<NotifySet>
     */
    ICustomPage<NotifySet> findNotifySets(QueryRequest request, NotifySet setNotify);

    /**
     * 修改
     *
     * @param setNotify setNotify
     */
    NotifySet saveNotifySet(NotifySet setNotify);

    /**
     * 启用
     *
     * @param setNotifyIds setNotifyIds
     */
    void enableNotifySet(String[] setNotifyIds);

    /**
    * 禁用
    *
    * @param setNotifyIds setNotifyIds
    */
    void disableNotifySet(String[] setNotifyIds);


    /**
    * 通过Id获取信息
    * @param setNotifyId
    * @return
    */
    NotifySet findNotifySetById(Long setNotifyId);

    /**
     * 通过Code获取信息
     * @param notifyType
     * @return
     */
    NotifySet findNotifySetByType(String notifyType, Set<String> statuses);

    /**
     * 通过使用类型获取信息
     * @return
     */
    List<NotifySet> findNotifySetByIs(Set<String> notifyType, String isOnInner, String isOnWx, String isOnSms, Set<String> statuses);
}
