package com.ebx.skeleton.entity.cbscene;

import com.ks.skeleton.entity.base.BaseEntity;
import java.sql.Timestamp;
/**
* 
* @author code4j
* @date Created in 2022-3-21 17:42:59
*/
public class CbSceneEntity extends BaseEntity{
    /**
     *场景的ID
    */
    private Long id;
    /**
     *场景名称
    */
    private String name;
    /**
     *如果是个人版，则值为res_id；如果是物业版，值为project_code
    */
    private String resCode;
    /**
     *用户ID
    */
    private Long staffId;
    /**
     *状态:1-启用;2-禁用
    */
    private Integer status;
    /**
     *生效时段，多个时段以逗号分隔，如: 08:00~12:00,13:30~22:00
    */
    private String timeRanges;
    /**
     *重复:日,一,二,三,四,五,六,逗号分隔。
    */
    private String weekday;
    /**
     *创建时间
    */
    private Timestamp createTime;
    /**
     *修改时间
    */
    private Timestamp updateTime;
    /**
     *备注
    */
    private String remark;
    /**
     *场景执行频率
    */
    private Long exeFrequency;
    public Long  getId() {
        return id;
    }
    public void setId(final Long  id) {
        this.id = id;
    }
    public String  getName() {
        return name;
    }
    public void setName(final String  name) {
        this.name = name;
    }
    public String  getResCode() {
        return resCode;
    }
    public void setResCode(final String  resCode) {
        this.resCode = resCode;
    }
    public Long  getStaffId() {
        return staffId;
    }
    public void setStaffId(final Long  staffId) {
        this.staffId = staffId;
    }
    public Integer  getStatus() {
        return status;
    }
    public void setStatus(final Integer  status) {
        this.status = status;
    }
    public String  getTimeRanges() {
        return timeRanges;
    }
    public void setTimeRanges(final String  timeRanges) {
        this.timeRanges = timeRanges;
    }
    public String  getWeekday() {
        return weekday;
    }
    public void setWeekday(final String  weekday) {
        this.weekday = weekday;
    }
    public Timestamp  getCreateTime() {
        return createTime;
    }
    public void setCreateTime(final Timestamp  createTime) {
        this.createTime = createTime;
    }
    public Timestamp  getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(final Timestamp  updateTime) {
        this.updateTime = updateTime;
    }
    public String  getRemark() {
        return remark;
    }
    public void setRemark(final String  remark) {
        this.remark = remark;
    }
    public Long  getExeFrequency() {
        return exeFrequency;
    }
    public void setExeFrequency(final Long  exeFrequency) {
        this.exeFrequency = exeFrequency;
    }
}
