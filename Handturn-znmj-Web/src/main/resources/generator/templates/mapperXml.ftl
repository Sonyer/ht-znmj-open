<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackage}.${mapperPackage}.${className}Mapper">

    <resultMap id="${className?uncap_first}" type="${basePackage}.${entityPackage}.${className}">
        <#if columns??>
        <#list columns as column>
            <#if column.isKey = true>
        <id column="${column.field?uncap_first}" property="${column.field?uncap_first}"/> <!--//${column.remark}-->
            <#else>
        <result column="${column.field?uncap_first}" property="${column.field?uncap_first}"/>  <!--//${column.remark}-->
            </#if>
        </#list>
        </#if>
    </resultMap>

    <sql id="SqlWith${className}SelectAll">
        SELECT
        <#if columns??>
        <#list columns as column>
            ${className}.${column.name} AS ${column.field?uncap_first},
        </#list>
        </#if>
        FROM ${tableName} AS ${className}
    </sql>

    <select id="findForPage" parameterType="${className?uncap_first}" resultType="${className?uncap_first}">
        <include refid="SqlWith${className}SelectAll"/>
        WHERE 1=1

        <#if columns??>
        <#list columns as column>
            <if test="${className?uncap_first}.${column.field?uncap_first} != null and ${className?uncap_first}.${column.field?uncap_first} != ''">
                AND ${className}.${column.name} = ----{${className?uncap_first}.${column.field?uncap_first}}
            </if>
        </#list>
        </#if>
    </select>
</mapper>
