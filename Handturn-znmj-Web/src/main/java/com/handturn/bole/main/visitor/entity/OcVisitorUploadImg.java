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
 * 网点-访客上传图片信息 Entity
 *
 * @author Eric
 * @date 2020-09-26 08:34:23
 */
@Data
@TableName("oc_visitor_upload_img")
@Excel("网点-访客上传图片信息")
public class OcVisitorUploadImg extends BasicEntity{

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 状态:INIT-初始状态,BINDED-已绑定
     */
    @TableField("status")
    @ExcelField(value = "状态:INIT-初始状态,BINDED-已绑定")
    private String status;

    /**
     * 上传信息
     */
    @TableField("upload_error_message")
    @ExcelField(value = "上传信息")
    private String uploadErrorMessage;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
