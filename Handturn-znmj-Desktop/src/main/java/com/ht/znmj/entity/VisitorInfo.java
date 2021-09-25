package com.ht.znmj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 人员信息
 */
@Data
@TableName("VisitorInfo")
public class VisitorInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("name")
    private String name;

    //手机号码
    @TableField("phoneNumber")
    private String phoneNumber;

    //部门名称
    @TableField("department")
    private String department;

    //岗位名称
    @TableField("positor")
    private String positor;

    //身份证
    @TableField("idCard")
    private String idCard;

    //韦根号
    @TableField("wigan")
    private String wigan;

    //开始时间
    @TableField("startTime")
    private Date startTime;

    //结束时间
    @TableField("endTime")
    private Date endTime;

    //人脸图片地址
    @TableField("faceFilePath")
    private String faceFilePath;

    //备注
    @TableField("remark")
    private String remark;

    //云端同步  1-已同步  0-未同步
    @TableField("cloudFlag")
    private String cloudFlag;

    @TableField("cloudId")
    private String cloudId;

    //新增时间
    @TableField("createTime")
    private Date createTime;

    //修改时间
    @TableField("updateTime")
    private Date updateTime;


    @TableField(exist = false)
    private String equipmentId;
    @TableField(exist = false)
    private String notInEquipmentId;

    @TableField(exist = false)
    private Long startUpdateTime;
    @TableField(exist = false)
    private Long endUpdateTime;
    @TableField(exist = false)
    private String isActive;
}
