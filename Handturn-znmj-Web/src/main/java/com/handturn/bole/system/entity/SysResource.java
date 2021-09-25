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

/**
 * 系统-资源 Entity
 *
 * @author Eric
 * @date 2019-12-01 10:31:46
 */
@Data
@TableName("sys_resource")
@Excel("系统-资源")
public class SysResource extends BasicEntity {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单编码
     */
    @TableField("resource_code")
    @ExcelField(value = "菜单编码")
    private String resourceCode;

    /**
     * 菜单名称
     */
    @TableField("resource_name")
    @ExcelField(value = "菜单名称")
    private String resourceName;

    /**
     * //菜单类型:MENU_ROOT-根目录;MENU_DIR-目录;MENU-菜单;RESOURCE_COMPONENT-按钮,MENU_ACTION-请求
     */
    @TableField("resource_node_type")
    @ExcelField(value = "菜单类型")
    private String resourceNodeType;

    /**
     * 排序
     */
    @TableField("sort_no")
    @ExcelField(value = "排序")
    private Integer sortNo;

    /**
     * 父节点id
     */
    @TableField("parent_resource_id")
    private Long parentResourceId;

    /**
     * 根节点id
     */
    @TableField("root_resource_id")
    private Long rootResourceId;

    /**
     * 菜单访问路径
     */
    @TableField("url")
    @ExcelField(value = "菜单访问路径")
    private String url;

    /**
     * 权限标识
     */
    @TableField("perms")
    @ExcelField(value = "权限标识")
    private String perms;

    /**
     * 状态: ENABLED-有效;DISABLED-无效
     */
    @TableField("status")
    @ExcelField(value = "状态: ENABLED-有效;DISABLED-无效")
    private String status;

    /**
     * 图标
     */
    @TableField("icon")
    @ExcelField(value = "图标")
    private String icon;

    /**
     * 是否弹窗(1:是 0:否)，点击菜单时是否新开窗口
     */
    @TableField("is_open_window")
    private Byte isOpenWindow;

    /**
     * 描述
     */
    @TableField("description")
    @ExcelField(value = "描述")
    private String description;

    /**
     * 菜单英文名
     */
    @TableField("resource_name_en")
    @ExcelField(value = "菜单英文名")
    private String resourceNameEn;


    /**
     * 父资源名称
     */
    @TableField(exist = false)
    @ExcelField(value = "父资源名称")
    private String parentResourceName;

    /**
     * 主模块名
     */
    @TableField(exist = false)
    @ExcelField(value = "主模块名称")
    private String rootResourceName;



    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
