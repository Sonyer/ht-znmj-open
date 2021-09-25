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
 * 网点-会员访客日志 Entity
 *
 * @author Eric
 * @date 2020-12-06 17:24:27
 */
@Data
@TableName("oc_visite_log")
@Excel("网点-会员访客日志")
public class OcVisiteLog extends BasicEntity{

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
     * 设备信息信息ID
     */
    @TableField("equipment_id")
    @ExcelField(value = "设备信息信息ID")
    private Long equipmentId;

    /**
     * 设备系统编码
     */
    @TableField("equipment_code")
    @ExcelField(value = "设备系统编码")
    private String equipmentCode;

    /**
     * 设备出厂编码
     */
    @TableField("equipment_md_code")
    @ExcelField(value = "设备出厂编码")
    private String equipmentMdCode;

    /**
     * 设备型号编号
     */
    @TableField("equipment_model_code")
    @ExcelField(value = "设备型号编号")
    private String equipmentModelCode;

    /**
     * 设备序号
     */
    @TableField("seq_num")
    @ExcelField(value = "设备序号")
    private String seqNum;

    /**
     * 区域名称
     */
    @TableField("area_name")
    @ExcelField(value = "区域名称")
    private String areaName;

    /**
     * 出入标记：0-未知位置 1-入口  2-出口
     */
    @TableField("in_out_flag")
    @ExcelField(value = "出入标记：0-未知位置 1-入口  2-出口")
    private String inOutFlag;

    /**
     * 网点访客信息ID
     */
    @TableField("oc_visitor_id")
    @ExcelField(value = "网点访客信息ID")
    private Long ocVisitorId;

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
    @TableField("wegin")
    @ExcelField(value = "韦根号")
    private String wegin;

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
     * 日志类型：0-离线数据 1-在线数据
     */
    @TableField("log_flag")
    @ExcelField(value = "日志类型：0-离线数据 1-在线数据")
    private String logFlag;

    /**
     * 开闸时间
     */
    @TableField("open_time")
    @ExcelField(value = "开闸时间")
    private Date openTime;

    /**
     * 开闸状态
     */
    @TableField("open_status")
    @ExcelField(value = "开闸状态")
    private String openStatus;

    @TableField(exist = false)
    private Date openTime_start;
    @TableField(exist = false)
    private Date openTime_end;



    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
