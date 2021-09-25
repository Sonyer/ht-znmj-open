package com.ht.znmj.view.service;

import com.ht.znmj.common.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 页面调用接口
 */
@Component
public class ApplicationUtil {
    @Autowired
    private ApplicationProperties applicationProperties;

    private static ApplicationUtil applicationUtil;

    @PostConstruct
    public void init(){
        applicationUtil = this;
        applicationUtil.applicationProperties = this.applicationProperties;
    }

    public static ApplicationProperties getApplicationProperties(){
            return applicationUtil.applicationProperties;
    }
}
