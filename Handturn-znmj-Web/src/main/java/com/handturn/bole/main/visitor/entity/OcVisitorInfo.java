package com.handturn.bole.main.visitor.entity;

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
 * 网点-会员访客信息 Entity
 *
 * @author Eric
 * @date 2020-09-22 08:57:38
 */
@Data
@TableName("oc_visitor_info")
@Excel("网点-会员访客信息")
public class OcVisitorInfo extends BasicEntity{

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
     * 韦根号
     */
    @TableField("wegan")
    @ExcelField(value = "韦根号")
    private String wegan;

    /**
     * 人脸附件-id
     */
    @TableField("face_upload_img_id")
    @ExcelField(value = "人脸附件")
    private Long faceUploadImgId;

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
     * 状态: ENABLED-已启用;DISABLED-已禁用
     */
    @TableField("freeze_status")
    @ExcelField(value = "状态: ENABLED-已启用;DISABLED-已禁用")
    private String freezeStatus;

    /**
     * 新增类型: PAGE_INPUT-页面录入;导入-IMPORT;WX_INVITE-微信邀请
     */
    @TableField("create_type")
    @ExcelField(value = "新增类型: PAGE_INPUT-页面录入;导入-IMPORT;WX_INVITE-微信邀请")
    private String createType;

    //----------查询列表需要---------
    /**
     * 是否查询权限
     */
    @TableField(exist = false)
    private Boolean authCheck;
    /**
     * 查询权限区域ID
     */
    @TableField(exist = false)
    private Long authAreaId;

    /**
     * 查询权限区域ID
     */
    @TableField(exist = false)
    private String layChecked;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
