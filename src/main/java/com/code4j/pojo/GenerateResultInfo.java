package com.code4j.pojo;

/**
 * @author liu_wp
 * @date 2020/12/18
 * @see
 */
public class GenerateResultInfo {
    private String generateType;
    private String generatePath;
    private String generateStatus;

    public GenerateResultInfo(String generateType, String generateStatus, String generatePath) {
        this.generateType = generateType;
        this.generateStatus = generateStatus;
        this.generatePath = generatePath;
    }

    public String getGenerateType() {
        return generateType;
    }

    public void setGenerateType(final String generateType) {
        this.generateType = generateType;
    }

    public String getGeneratePath() {
        return generatePath;
    }

    public void setGeneratePath(final String generatePath) {
        this.generatePath = generatePath;
    }

    public String getGenerateStatus() {
        return generateStatus;
    }

    public void setGenerateStatus(final String generateStatus) {
        this.generateStatus = generateStatus;
    }
}
