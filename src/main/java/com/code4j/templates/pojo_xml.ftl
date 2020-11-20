<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${(xmlMap.namespace)!}">
    <!--
        @author ${(xmlMap.author)!}
        @date Created in ${.now}
    -->
    <resultMap id="BaseResultMap" type="${(xmlMap.resultMapType)!}" >
        <#if  (xmlMap.tableColumnInfos)??><#list xmlMap.tableColumnInfos as result>
            <result column="${(result.column)!}" property="${(result.javaProperty)!}" jdbcType="${(result.jdbcType)!}" />
                </#list>
        </#if>
    </resultMap>
</mapper>