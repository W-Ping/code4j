package ${(pojo.packageRoot)!""}${(pojo.packageName)!};

<#if (pojo.superPojoInfo)??>
import ${pojo.superPojoInfo.packageName}.${pojo.superPojoInfo.pojoName};
</#if>
import org.springframework.stereotype.Service;
import ${(pojo.interfaceInfo.packageRoot)!""}${(pojo.interfaceInfo.packageName)!}.${(pojo.interfaceInfo.pojoName)!};

/**
* ${(pojo.jdbcTableInfo.remark)!}
* @author ${(pojo.author)!}
* @date Created in ${.now}
*/
@Service
<#if (pojo.superPojoInfo)??>
public class ${(pojo.pojoName)!} extends ${(pojo.superPojoInfo.superClassName)!} implements ${(pojo.interfaceInfo.pojoName)!}{
<#else>
public class ${(pojo.pojoName)!} implements ${(pojo.interfaceInfo.pojoName)!}{
</#if>

}