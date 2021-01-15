package ${(baseInfo.packageRoot)!}${(baseInfo.packageName)!};
import org.springframework.stereotype.Repository;
<#if (packages)??><#list packages as model>
import ${model};
</#list></#if>
/**
* ${(baseInfo.jdbcTableInfo.remark)!}
* @author ${(baseInfo.author)!}
* @date Created in ${.now}
*/
@Repository
<#if (baseInfo.superInfo)??>public interface ${(baseInfo.pojoName)!} extends ${baseInfo.superInfo.superClassName}{
<#else>public interface ${(baseInfo.pojoName)!}{
</#if>
<#if (apiInfos)??>
    <#list apiInfos as model>
        <#if (model.parameterType)??>
         <#if model.isPageSelect>
         <#if model.parameterTypeIsList>
  ${model.resultType} ${model.apiId}(@Param("List") ${(model.parameterType)!} list);
                <#else>
  ${model.resultType} ${model.apiId}(${(model.parameterType)!} object);
            </#if>
         <#else>
  ${model.resultType} ${model.apiId}();
        </#if>
        </#if>
        <#if model.isPageSelect>
  ${model.resultType} ${model.apiId}(${(model.parameterType)!} object,@Param("pageNum") int pageNum,@Param("pageSize") int pageSize);
        </#if>
    </#list>
</#if>
}