package com.handturn.bole.main.visitor.entity;

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
 * 网点-会员访客审核 Entity
 *
 * @author Eric
 * @date 2020-09-22 08:57:57
 */
@Data
@TableName("oc_visitor_apply")
@Excel("网点-会员访客审核")
public class OcVisitorApply extends BasicEntity{

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
     * 用户访客ID
     */
    @TableField("member_visitor_id")
    @ExcelField(value = "用户访客ID")
    private Long memberVisitorId;

    /**
     * 网点访客ID
     */
    @TableField("oc_visitor_id")
    @ExcelField(value = "网点访客ID")
    private Long ocVisitorId;

    /**
     * 网点编码
     */
    @TableField("oc_code")
    @ExcelField(value = "网点编码")
    private String ocCode;

    /**
     * 组织编码
     */
    @TableField("org_code")
    @ExcelField(value = "组织编码")
    private String orgCode;

    /**
     * 网点名称
     */
    @TableField("oc_name")
    @ExcelField(value = "网点名称")
    private String ocName;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ExcelField(value = "组织名称")
    private String orgName;

    /**
     * 申请权限区域编码
     */
    @TableField("oc_auth_area_code")
    @ExcelField(value = "申请权限区域编码")
    private String ocAuthAreaCode;

    /**
     * 访客类型:INNER_USER-内部员工,OUTER_USER-外来人员
     */
    @TableField("visitor_type")
    @ExcelField(value = "访客类型:INNER_USER-内部员工,OUTER_USER-外来人员")
    private String visitorType;

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
     * 部门名称
     */
    @TableField("dep_name")
    @ExcelField(value = "部门名称")
    private String depName;

    /**
     * 职位名称
     */
    @TableField("position_name")
    @ExcelField(value = "职位名称")
    private String positionName;

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
     * 摘要信息，其他信息系统填充位置
     */
    @TableField("abstract_msg")
    @ExcelField(value = "摘要信息，其他信息系统填充位置")
    private String abstractMsg;

    /**
     * 审核状态:UNAUDIT-未审核,AUDITING-审核中,AUDITED-已审核,AUDITED_FAIL-审核失败
     */
    @TableField("audit_status")
    @ExcelField(value = "审核状态:UNAUDIT-未审核,AUDITING-审核中,AUDITED-已审核,AUDITED_FAIL-审核失败")
    private String auditStatus;

    /**
     * 审核消息
     */
    @TableField("audit_message")
    @ExcelField(value = "审核消息")
    private String auditMessage;

    /**
     * 有效开始时间
     */
    @TableField("effective_time_begin")
    @ExcelField(value = "有效开始时间")
    private Date effectiveTimeBegin;

    /**
     * 有效结束时间
     */
    @TableField("effective_time_end")
    @ExcelField(value = "有效结束时间")
    private Date effectiveTimeEnd;

    @TableField(exist = false)
    private String ids;

    @TableField(exist = false)
    private String ocAuthAreaName;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
