package ${(pojo.packageRoot)!""}${(pojo.packageName)!};

<#if (pojo.superPojoInfo)??>
    import ${pojo.superPojoInfo.superClassPackage}.${pojo.superPojoInfo.superClassName};
</#if>
import javax.persistence.Column;
import javax.persistence.Table;

<#list columnPackages as pak >import ${pak};</#list>
/**
* ${(pojo.jdbcTableInfo.remark)!}
* @author ${(pojo.author)!}
* @date Created in ${.now}
*/
@Table(name = "${(pojo.jdbcTableInfo.tableName)!}")
<#if (pojo.superPojoInfo)??>public class ${(pojo.pojoName)!} extends ${pojo.superPojoInfo.superClassName}{
<#else>public class ${(pojo.pojoName)!}{
</#if>
<#list tableColumnInfos as model >
<#if model.ignore==false>
    /**
    *${model.comment!}
    */
    @Column(name = "${model.column!}")
    private <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if> ${model.javaProperty!};
</#if>

</#list>
<#list tableColumnInfos as model ><#if model.ignore==false>
    public <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if>  get${model.javaProperty?cap_first}() {
       return ${model.javaProperty!};
    }
    public void set${model.javaProperty?cap_first}(final <#if model.javaType?contains(".")>${model.javaType?substring(model.javaType?last_index_of(".")+1)}<#else>${model.javaType!}</#if>  ${model.javaProperty!}) {
     this.${model.javaProperty!} = ${model.javaProperty!};
    }
    </#if>
</#list>
}