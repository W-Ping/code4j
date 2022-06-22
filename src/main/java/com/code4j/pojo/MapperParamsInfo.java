package com.code4j.pojo;

import java.util.List;

/**
 * @author liu_wp
 * @date 2021/1/14
 * @see
 */
public class MapperParamsInfo extends BaseTemplateInfo {
    private SuperPojoInfo superPojoInfo;

    /**
     *
     */
    private List<MapperApiParamsInfo> mapperApiParamsInfos;


    public MapperParamsInfo(List<JdbcMapJavaInfo> tableColumnInfos) {
        super(tableColumnInfos);
    }

    public SuperPojoInfo getSuperPojoInfo() {
        return superPojoInfo;
    }

    public void setSuperPojoInfo(final SuperPojoInfo superPojoInfo) {
        this.superPojoInfo = superPojoInfo;
    }

    public List<MapperApiParamsInfo> getMapperApiParamsInfos() {
        return mapperApiParamsInfos;
    }

    public void setMapperApiParamsInfos(final List<MapperApiParamsInfo> mapperApiParamsInfos) {
        this.mapperApiParamsInfos = mapperApiParamsInfos;
    }

}
