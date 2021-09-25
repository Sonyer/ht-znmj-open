package com.handturn.bole.master.set.entity;

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
 * 智能门禁-站点配置 Entity
 *
 * @author Eric
 * @date 2020-02-28 09:41:18
 */
@Data
@TableName("sit_set")
@Excel("智能门禁-站点配置")
public class SitSet extends BasicEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 版权说明
     */
    @TableField("copyright")
    @ExcelField(value = "版权说明")
    private String copyright;

    /**
     * 关于我们
     */
    @TableField("about_us")
    @ExcelField(value = "关于我们")
    private String aboutUs;

    /**
     * 说明文档地址
     */
    @TableField("inst_doc_request")
    @ExcelField(value = "说明文档地址")
    private String instDocRequest;


    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
