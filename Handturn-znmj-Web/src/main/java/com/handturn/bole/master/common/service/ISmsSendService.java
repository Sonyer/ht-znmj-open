package com.handturn.bole.master.common.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * 短信通知服务
 */
public interface ISmsSendService {

    /**
     * 短信通知服务
     * @param templetCode
     * @param phoneNumber
     * @param requestData
     * @return
     */
    SendSmsResponse sendSms(String templetCode, String phoneNumber, String requestData, String outId) throws ClientException;

}
