/*
 * Copyright 2018 宋叶全(songyequan@sina.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.handturn.bole.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author by songyequan on 2018/2/6.
 */
@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BasicEntity extends Model
        implements Serializable {

    // 公用字段,插入和更新时将自动填充值
    // 详见:com.scm.db.mybatis.config.MybatisMetaObjectHandler
    // http://baomidou.oschina.io/mybatis-plus-doc/#/auto-fill

    @Version
    @TableField(value = "record_version",fill = FieldFill.INSERT_UPDATE)
    private Long recordVersion = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_date",fill = FieldFill.INSERT)
    private Date createDate;

    @TableField(value = "create_user_code",fill = FieldFill.INSERT)
    private String createUserCode;

    @TableField(value = "create_user_name",fill = FieldFill.INSERT)
    private String createUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "last_upd_date",fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdDate;

    @TableField(value = "last_upd_user_code",fill = FieldFill.INSERT_UPDATE)
    private String lastUpdUserCode;

    @TableField(value = "last_upd_user_name",fill = FieldFill.INSERT_UPDATE)
    private String lastUpdUserName;

    @Override
    protected abstract Serializable pkVal();
}
