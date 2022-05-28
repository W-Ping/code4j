<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${(xmlMap.namespace)!}">
    <!--
        @author ${(xmlMap.author)!}
        @date Created in ${.now}
    -->
    <resultMap id="BaseResultMap" type="${(xmlMap.resultMapType)!}">
        <#if (xmlMap.tableColumnInfos)??><#list xmlMap.tableColumnInfos as model>
            <#if model.ignore==false><result column="${(model.column)!}" property="${(model.javaProperty)!}" jdbcType="${(model.jdbcType)!}"/></#if>
        </#list>
        </#if>
    </resultMap>
    <#if  (xmlMap.xmlApiParamsInfos)??>
        <#list xmlMap.xmlApiParamsInfos as model>
                <#if model.templateId=="8">
                    <!-- 方法 insertDuplicateKey-->
                    <insert id="${(model.apiId)!}">
                        INSERT INTO ${(xmlMap.jdbcTableInfo.tableName)!} (
                        <#list xmlMap.tableColumnInfos as model>
                            ${(model.column)!}<#if model_has_next>,</#if>
                        </#list>
                        )VALUES(
                        <#list xmlMap.tableColumnInfos as model>
                            ${r"#"}{${(model.javaProperty)!}}<#if model_has_next>,</#if>
                        </#list>
                        )
                        ON DUPLICATE KEY UPDATE
                        <#list xmlMap.tableColumnInfos as model>
                            ${(model.column)!}=VALUES(${(model.column)!})<#if model_has_next>,</#if>
                        </#list>
                    </insert>
                 <#elseif (model.templateId=="9")>
                     <!-- 方法 insertListDuplicateKey-->
                     <insert id="${(model.apiId)!}">
                         INSERT INTO ${(xmlMap.jdbcTableInfo.tableName)!} (
                         <#list xmlMap.tableColumnInfos as model>
                             ${(model.column)!}<#if model_has_next>,</#if>
                         </#list>
                         )VALUES
                         <foreach collection="list" item="item" separator=",">
                             (
                             <#list xmlMap.tableColumnInfos as model>
                                 ${r"#"}{item.${(model.javaProperty)!}}<#if model_has_next>,</#if>
                             </#list>
                             )
                         </foreach>
                         ON DUPLICATE KEY UPDATE
                         <#list xmlMap.tableColumnInfos as model>
                             ${(model.column)!}=VALUES(${(model.column)!})<#if model_has_next>,</#if>
                         </#list>
                     </insert>
                <#elseif (model.templateId=="1")>
                    <!-- 方法 insertOne-->
                    <insert id="${(model.apiId)!}">
                        INSERT INTO ${(xmlMap.jdbcTableInfo.tableName)!} (
                        <#list xmlMap.tableColumnInfos as model>
                            ${(model.column)!}<#if model_has_next>,</#if>
                        </#list>
                        )VALUES(
                        <#list xmlMap.tableColumnInfos as model>
                            ${r"#"}{${(model.javaProperty)!}}<#if model_has_next>,</#if>
                        </#list>
                        )
                    </insert>
                <#elseif (model.templateId=="2")>
                    <!-- 方法 insertList-->
                    <insert id="${(model.apiId)!}">
                        INSERT INTO ${(xmlMap.jdbcTableInfo.tableName)!} (
                        <#list xmlMap.tableColumnInfos as model>
                            ${(model.column)!}<#if model_has_next>,</#if>
                        </#list>
                        )VALUES
                        <foreach collection="list" item="item" separator=",">
                            (
                            <#list xmlMap.tableColumnInfos as model>
                                ${r"#"}{item.${(model.javaProperty)!}}<#if model_has_next>,</#if>
                            </#list>
                            )
                        </foreach>
                    </insert>
                 <#elseif (model.templateId=="3")>
                    <!-- 方法 deleteByObject-->
                    <delete id="${(model.apiId)!}" parameterType="${(xmlMap.resultMapType)!}">
                    </delete>
                 <#elseif (model.templateId=="4")>
                    <!-- 方法 updateByObject-->
                    <update id="${(model.apiId)!}" parameterType="${(xmlMap.resultMapType)!}">
                    </update>
                 <#elseif (model.templateId=="5")>
                   <!-- 方法 selectByObject-->
                    <select id="${(model.apiId)!}" resultMap="BaseResultMap" parameterType="${(xmlMap.resultMapType)!}">
                        SELECT * FROM ${(xmlMap.jdbcTableInfo.tableName)!}
                        <#if (model.xmlApiWhereParamsInfos)??>
                            <where>
                                <#list model.xmlApiWhereParamsInfos as w>
                                    <#if w.javaType=="java.lang.String">
                               <if test="${(w.javaProperty)}!=null and ${(w.javaProperty)}!=''">
                                     <#else>
                                <if test="${(w.javaProperty)}!=null">
                                    </#if>
                                AND ${(w.column)!}=${r"#"}{${(w.javaProperty)}}
                               </if>
                                </#list>
                            </where>
                        </#if>
                    </select>
                 <#elseif (model.templateId=="6")>
                    <!-- 方法 selectOne -->
                    <select id="${(model.apiId)!}" resultMap="BaseResultMap" parameterType="${(xmlMap.resultMapType)!}">
                    </select>
                 <#elseif (model.templateId=="7")>
                    <!-- 方法 selectPageByObject -->
                    <select id="${(model.apiId)!}" resultMap="BaseResultMap" parameterType="${(xmlMap.resultMapType)!}">
                        SELECT * FROM ${(xmlMap.jdbcTableInfo.tableName)!} LIMIT ${r"#"}{pageSize} OFFSET ${r"#"}{pageNum}
                    </select>
                </#if>
        </#list>
    </#if>
</mapper>