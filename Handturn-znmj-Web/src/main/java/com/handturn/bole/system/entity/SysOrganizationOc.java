package com.handturn.bole.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.handturn.bole.common.entity.BasicEntity;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 系统-公司操作运营中心 Entity
 *
 * @author MrBird
 * @date 2019-12-08 19:36:28
 */
@Data
@TableName("sys_organization_oc")
@Excel("系统-公司操作运营中心")
public class SysOrganizationOc extends BasicEntity{

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务组织编号
     */
    @TableField("oc_code")
    @ExcelField(value = "业务组织编号")
    private String ocCode;

    /**
     * 业务组织名称
     */
    @TableField("oc_name")
    @ExcelField(value = "业务组织名称")
    private String ocName;

    /**
     * 业务组织简称
     */
    @TableField("oc_short_name")
    @ExcelField(value = "业务组织简称")
    private String ocShortName;

    /**
     * 系统组织id
     */
    @TableField("org_id")
    @ExcelField(value = "系统组织id")
    private Long orgId;

    /**
     * 网点类型:SYS:系统;ORG_CN:客户中心网点;ORG-OC:客户运营网点;ORG-3TH:客户第三方节点
     */
    @TableField("oc_type")
    @ExcelField(value = "网点类型:SYS:系统;ORG_CN:客户中心网点;ORG-OC:客户运营网点;ORG-3TH:客户第三方节点")
    private String ocType;

    /**
     * 地址
     */
    @TableField("address")
    @ExcelField(value = "地址")
    private String address;

    /**
     * 区域
     */
    @TableField("region")
    @ExcelField(value = "区域")
    private String region;

    /**
     * 城市
     */
    @TableField("city")
    @ExcelField(value = "城市")
    private String city;

    /**
     * 省份
     */
    @TableField("province")
    @ExcelField(value = "省份")
    private String province;

    /**
     * 国家
     */
    @TableField("country")
    @ExcelField(value = "国家")
    private String country;

    /**
     * 邮编
     */
    @TableField("post_code")
    @ExcelField(value = "邮编")
    private String postCode;

    /**
     * 电话
     */
    @TableField("tel")
    @ExcelField(value = "电话")
    private String tel;

    /**
     * 手机
     */
    @TableField("cell")
    @ExcelField(value = "手机")
    private String cell;

    /**
     * 传真
     */
    @TableField("fax")
    @ExcelField(value = "传真")
    private String fax;

    /**
     * 邮箱
     */
    @TableField("email")
    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 联系人
     */
    @TableField("attention_to")
    @ExcelField(value = "联系人")
    private String attentionTo;

    /**
     * 备注
     */
    @TableField("remark")
    @ExcelField(value = "备注")
    private String remark;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;

    /**
     * 查询条件客户组
     */
    @TableField(exist = false)
    private Set<String> clientCodes;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
