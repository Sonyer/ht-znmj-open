package com.handturn.bole.master.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * 智能门禁-会员通知 Entity
 *
 * @author Eric
 * @date 2020-05-19 09:22:40
 */
@Data
@TableName("member_notify")
@Excel("智能门禁-会员通知")
public class MemberNotify {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    @TableField("member_id")
    @ExcelField(value = "会员ID")
    private Long memberId;

    /**
     * 会员编号
     */
    @TableField("account_code")
    @ExcelField(value = "会员编号")
    private String accountCode;

    /**
     * 通知类型
     */
    @TableField("notify_type")
    @ExcelField(value = "通知类型")
    private String notifyType;

    /**
     * 内部通知消息
     */
    @TableField("inner_notify_message")
    @ExcelField(value = "内部通知消息")
    private String innerNotifyMessage;

    /**
     * 内部消息跳转页面
     */
    @TableField("inner_notify_to_page")
    @ExcelField(value = "内部消息跳转页面")
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
     * 通知状态:UNCACHE-未缓存,CACHED-已缓存
     */
    @TableField("cache_status")
    @ExcelField(value = "通知状态:UNCACHE-未缓存,CACHED-已缓存")
    private String cacheStatus;

    /**
     * 通知状态:UNREAD-未读,READED-已读,DELETED-删除
     */
    @TableField("read_status")
    @ExcelField(value = "通知状态:UNREAD-未读,READED-已读,DELETED-删除")
    private String readStatus;

    /**
     * WX通知发送状态UNSEND-未发送;SEND-已发送
     */
    @TableField("send_status")
    @ExcelField(value = "WX通知发送状态UNSEND-未发送;SEND-已发送")
    private String sendStatus;


    /**
     * 新增时间
     */
    @TableField("create_date")
    @ExcelField(value = "新增时间")
    private Date createDate;

    /**
     * 最后修改时间
     */
    @TableField("last_upd_date")
    @ExcelField(value = "最后修改时间")
    private Date lastUpdDate;
}
