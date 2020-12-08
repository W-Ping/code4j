package ${(pojo.packageRoot)!}${(pojo.packageName)!};
import org.springframework.stereotype.Repository;
/**
* ${(pojo.jdbcTableInfo.remark)!}
* @author ${(pojo.author)!}
* @date Created in ${.now}
*/
@Repository
<#if (pojo.superInfo)??>public interface ${(pojo.pojoName)!} extends ${pojo.superInfo.superClassName}{
<#else>public interface ${(pojo.pojoName)!}{
</#if>
}