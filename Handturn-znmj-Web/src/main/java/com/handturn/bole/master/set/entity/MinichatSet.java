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
 * 智能门禁-小程序参数配置 Entity
 *
 * @author Eric
 * @date 2020-02-28 11:05:13
 */
@Data
@TableName("minichat_set")
@Excel("智能门禁-小程序参数配置")
public class MinichatSet extends BasicEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信小程序APPID
     */
    @TableField("minichat_app_id")
    @ExcelField(value = "微信小程序APPID")
    private String minichatAppId;

    /**
     * 微信小程序SECRET
     */
    @TableField("minichat_app_secret")
    @ExcelField(value = "微信小程序SECRET")
    private String minichatAppSecret;

    /**
     * 商户ID
     */
    @TableField("pay_customer_id")
    @ExcelField(value = "商户ID")
    private String payCustomerId;

    /**
     * 支付密钥
     */
    @TableField("pay_secret")
    @ExcelField(value = "支付密钥")
    private String paySecret;

    /**
     * 付款回调地址
     */
    @TableField("notify_url")
    @ExcelField(value = "付款回调地址")
    private String notifyUrl;

    /**
     * 退款回调地址
     */
    @TableField("refund_notify_url")
    @ExcelField(value = "退款回调地址")
    private String refundNotifyUrl;

    /**
     * 证书存放地址
     */
    @TableField("cert_file_path")
    @ExcelField(value = "证书存放地址")
    private String certFilePath;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
