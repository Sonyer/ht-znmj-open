package com.handturn.bole.master.set.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.entity.BasicEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * 智能门禁-通知设置 Entity
 *
 * @author Eric
 * @date 2020-06-09 18:43:12
 */
@Data
@TableName("notify_set")
@Excel("智能门禁-通知设置")
public class NotifySet extends BasicEntity {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通知类型
     */
    @TableField("notify_type")
    @ExcelField(value = "通知类型")
    private String notifyType;

    /**
     * 开启平台通知
     */
    @TableField("is_on_inner")
    @ExcelField(value = "开启平台通知")
    private String isOnInner;

    /**
     * 开启微信通知
     */
    @TableField("is_on_wx")
    @ExcelField(value = "开启微信通知")
    private String isOnWx;

    /**
     * 开启SMS通知
     */
    @TableField("is_on_sms")
    @ExcelField(value = "开启SMS通知")
    private String isOnSms;

    /**
     * 使用人群
     */
    @TableField("owner_type")
    @ExcelField(value = "使用人群")
    private String ownerType;

    /**
     * 消息描述
     */
    @TableField("notify_dec")
    @ExcelField(value = "消息描述")
    private String notifyDec;

    /**
     * 内部通知消息
     */
    @TableField("inner_notify_message")
    @ExcelField(value = "内部通知消息")
    private String innerNotifyMessage;

    /**
     * 内部通知消息
     */
    @TableField("inner_notify_to_page")
    @ExcelField(value = "内部通知消息")
    private String innerNotifyToPage;

    /**
     * WX通知模板ID
     */
    @TableField("wx_notify_template_id")
    @ExcelField(value = "WX通知模板ID")
    private String wxNotifyTemplateId;

    /**
     * WX通知JSON串
     */
    @TableField("wx_notify_data_json")
    @ExcelField(value = "WX通知JSON串")
    private String wxNotifyDataJson;

    /**
     * 微信通知跳转页面
     */
    @TableField("wx_notify_to_page")
    @ExcelField(value = "微信通知跳转页面")
    private String wxNotifyToPage;

    /**
     * 短信通知模板编码
     */
    @TableField("sms_notify_template_code")
    @ExcelField(value = "短信通知模板编码")
    private String smsNotifyTemplateCode;

    /**
     * 短信通知电话号码
     */
    @TableField("sms_phone_number")
    @ExcelField(value = "短信通知电话号码")
    private String smsPhoneNumber;

    /**
     * 短信通知JSON串
     */
    @TableField("sms_notify_data_json")
    @ExcelField(value = "短信通知JSON串")
    private String smsNotifyDataJson;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
