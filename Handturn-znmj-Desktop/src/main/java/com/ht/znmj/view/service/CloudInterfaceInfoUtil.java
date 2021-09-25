package com.ht.znmj.view.service;

import com.ht.znmj.entity.CloudInterfaceInfo;
import com.ht.znmj.service.ICloudInterfaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 页面调用接口
 */
@Component
public class CloudInterfaceInfoUtil {
    @Autowired
    private ICloudInterfaceInfoService cloudInterfaceInfoService;

    private static CloudInterfaceInfoUtil cloudInterfaceInfoUtil;

    @PostConstruct
    public void init(){
        cloudInterfaceInfoUtil = this;
        cloudInterfaceInfoUtil.cloudInterfaceInfoService = this.cloudInterfaceInfoService;
    }

    public static CloudInterfaceInfo findCloudInterfaceInfoById(String id){
        return cloudInterfaceInfoUtil.cloudInterfaceInfoService.findCloudInterfaceInfoById(id);
    }

    public static CloudInterfaceInfo saveCloudInterfaceInfo(CloudInterfaceInfo cloudInterfaceInfo){
        return cloudInterfaceInfoUtil.cloudInterfaceInfoService.saveCloudInterfaceInfo(cloudInterfaceInfo);
    }
}
