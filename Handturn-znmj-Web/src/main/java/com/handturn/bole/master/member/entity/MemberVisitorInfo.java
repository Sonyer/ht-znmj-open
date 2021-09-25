package com.handturn.bole.master.member.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.Data;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.entity.BasicEntity;

/**
 * 会员-会员访客信息 Entity
 *
 * @author Eric
 * @date 2020-09-22 08:57:18
 */
@Data
@TableName("member_visitor_info")
@Excel("会员-会员访客信息")
public class MemberVisitorInfo extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("member_id")
    @ExcelField(value = "用户ID")
    private Long memberId;

    /**
     * 用户账号
     */
    @TableField("member_account_code")
    @ExcelField(value = "用户账号")
    private String memberAccountCode;

    /**
     * 真实姓名
     */
    @TableField("id_card_name")
    @ExcelField(value = "真实姓名")
    private String idCardName;

    /**
     * 身份证号码
     */
    @TableField("id_card")
    @ExcelField(value = "身份证号码")
    private String idCard;

    /**
     * 用户手机号
     */
    @TableField("phone_number")
    @ExcelField(value = "用户手机号")
    private String phoneNumber;

    /**
     * 人脸附件-存根地址
     */
    @TableField("face_img_attchment")
    @ExcelField(value = "人脸附件-存根地址")
    private String faceImgAttchment;

    /**
     * 人脸图片-请求地址
     */
    @TableField("face_img_request")
    @ExcelField(value = "人脸图片-请求地址")
    private String faceImgRequest;

    /**
     * 审核状态:INIT-草稿,UPLOAD-已认证
     */
    @TableField("status")
    @ExcelField(value = "审核状态:INIT-草稿,UPLOAD-已认证")
    private String status;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
