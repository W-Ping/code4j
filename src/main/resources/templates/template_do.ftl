package ${(pojoInfo.packageRoot)!""}${(pojoInfo.packageName)!};

<#if (pojoInfo.superPojoInfo)??>
    import ${pojoInfo.superPojoInfo.superClassPackage}.${pojoInfo.superPojoInfo.superClassName};
</#if>
import javax.persistence.Column;
import javax.persistence.Table;

<#if packages??>
    <#list packages as pak >import ${pak};</#list>
</#if>
/**
* ${(pojoInfo.jdbcTableInfo.remark)!}
* @author ${(pojoInfo.author)!}
* @date Created in ${.now}
*/
@Table(name = "${(pojoInfo.jdbcTableInfo.tableName)!}")
<#if (pojoInfo.superPojoInfo)??>public class ${(pojoInfo.pojoName)!} extends ${pojoInfo.superPojoInfo.superClassName}{
<#else>public class ${(pojoInfo.pojoName)!}{
</#if>
<#if (pojoInfo.tableColumnInfos)??>
    <#list pojoInfo.tableColumnInfos as model >
        <#if model.ignore==false>
    /**
     *${model.comment!}
    */
    @Column(name = "${model.column!}")
    private <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if> ${model.javaProperty!};
        </#if>

    </#list>
    <#list pojoInfo.tableColumnInfos as model ><#if model.ignore==false>
    public <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if>  get${model.javaProperty?cap_first}() {
        return ${model.javaProperty!};
    }
    public void set${model.javaProperty?cap_first}(fin al <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if>  ${model.javaProperty!}) {
        this.${model.javaProperty!} = ${model.javaProperty!};
    }
    </#if>
    </#list>
</#if>
}