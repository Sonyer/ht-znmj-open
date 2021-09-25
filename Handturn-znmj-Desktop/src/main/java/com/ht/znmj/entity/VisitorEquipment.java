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
@TableName("VisitorEquipment")
public class VisitorEquipment {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 人员ID
     */
    @TableField("visitorId")
    private String visitorId;

    /**
     * 设备ID
     */
    @TableField("equipmentId")
    private String equipmentId;

    //设备同步  1-已同步  0-未同步
    @TableField("equipmentFlag")
    private String equipmentFlag;

    //云端同步  1-已同步  0-未同步
    @TableField("cloudFlag")
    private String cloudFlag;

    //新增时间
    @TableField("createTime")
    private Date createTime;

    //修改时间
    @TableField("updateTime")
    private Date updateTime;
}
