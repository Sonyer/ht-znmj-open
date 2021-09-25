package com.ht.znmj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ht.znmj.entity.CloudInterfaceInfo;

/**
 * 信息 Service接口
 *
 * @author Eric
 * @date 2020-01-29 14:34:06
 */
public interface ICloudInterfaceInfoService extends IService<CloudInterfaceInfo> {
    /**
     * 通过ID查询
     */
    CloudInterfaceInfo findCloudInterfaceInfoById(String id);

    /**
     * 保存信息
     * @param cloudInterfaceInfo
     */
    CloudInterfaceInfo saveCloudInterfaceInfo(CloudInterfaceInfo cloudInterfaceInfo);

}
