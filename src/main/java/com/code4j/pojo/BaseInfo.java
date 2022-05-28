package com.code4j.pojo;

import com.code4j.annotation.Column;

/**
 * @author liu_wp
 * @date 2020/11/18
 * @see
 */
public class BaseInfo implements Cloneable {
    @Column(value = "id", pk = true, auto = true)
    private Long id;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
