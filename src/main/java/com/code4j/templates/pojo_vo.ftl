package ${(pojo.packageRoot)!""}${(pojo.packageName)!};

<#if (pojo.superInfo)??>
    import ${pojo.superInfo.superClassPackage}.${pojo.superInfo.superClassName};
</#if>

<#list columnPackages as pak >import ${pak};</#list>
/**
* ${(pojo.jdbcTableInfo.remark)!}
* @author ${(pojo.author)!}
* @date Created in ${.now}
*/
<#if (pojo.superInfo)??>
public class ${(pojo.pojoName)!} extends ${pojo.superInfo.superClassName}{
<#else>
public class ${(pojo.pojoName)!}{
</#if>
<#list tableColumnInfos as model >
    /**
    *${model.comment!}
    */
    private <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if> ${model.javaProperty!};

</#list>
<#list tableColumnInfos as model >
    public <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if>  get${model.javaProperty?cap_first}() {
    return ${model.javaProperty!};
    }
    public void set${model.javaProperty?cap_first}(final <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if>  ${model.javaProperty!}) {
    this.${model.javaProperty!} = ${model.javaProperty!};
    }
</#list>
}