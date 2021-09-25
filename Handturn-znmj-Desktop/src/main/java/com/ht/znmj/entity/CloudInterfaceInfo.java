package com.ht.znmj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 云端接口设置
 */
@Data
@TableName("CloudInterfaceInfo")
public class CloudInterfaceInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @TableField("url")
    private String url;
    //网点编号
    @TableField("ocCode")
    private String ocCode;
    //账号
    @TableField("accountCode")
    private String accountCode;
    //密码
    @TableField("password")
    private String password;
    //设备连接状态  1-已连接   0-未连接
    @TableField("connStatus")
    private String connStatus;
    //最后心跳时间
    @TableField("heartbeatTime")
    private Date heartbeatTime;
    //新增时间
    @TableField("createTime")
    private Date createTime;
    //修改时间
    @TableField("updateTime")
    private Date updateTime;
}
