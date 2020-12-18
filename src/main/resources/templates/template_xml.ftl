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
</mapper>