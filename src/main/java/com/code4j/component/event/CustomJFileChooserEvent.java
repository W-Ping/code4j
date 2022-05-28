package com.code4j.component.event;

/**
 * @author liu_wp
 * @date 2020/11/20
 * @see
 */
public class CustomJFileChooserEvent {
    private Object value;
    private Integer optType;
    private boolean isMultiSelect;

    public CustomJFileChooserEvent(Object value, Integer optType, boolean isMultiSelect) {
        this.value = value;
        this.optType = optType;
        this.isMultiSelect = isMultiSelect;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(final Integer optType) {
        this.optType = optType;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void setMultiSelect(final boolean multiSelect) {
        isMultiSelect = multiSelect;
    }
}
