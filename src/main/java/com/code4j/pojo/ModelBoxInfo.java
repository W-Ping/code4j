package com.code4j.pojo;

import com.code4j.component.CustomJTextField;

/**
 * @author lwp
 * @date 2022-06-21
 */
public class ModelBoxInfo {
    private CustomJTextField packNameFiled;
    private CustomJTextField packageField;
    private CustomJTextField pojoPathField;
    private CustomJTextField superPathField;

    private CustomJTextField resultClassField;
    private Object bindObject;

    public ModelBoxInfo(CustomJTextField packNameFiled, CustomJTextField packageField, CustomJTextField pojoPathField, CustomJTextField superPathField, CustomJTextField resultClassField, Object bindObject) {
        this.packNameFiled = packNameFiled;
        this.packageField = packageField;
        this.pojoPathField = pojoPathField;
        this.superPathField = superPathField;
        this.resultClassField = resultClassField;
        this.bindObject = bindObject;
    }

    public CustomJTextField getPackNameFiled() {
        return packNameFiled;
    }

    public void setPackNameFiled(CustomJTextField packNameFiled) {
        this.packNameFiled = packNameFiled;
    }

    public CustomJTextField getPackageField() {
        return packageField;
    }

    public void setPackageField(CustomJTextField packageField) {
        this.packageField = packageField;
    }

    public CustomJTextField getPojoPathField() {
        return pojoPathField;
    }

    public void setPojoPathField(CustomJTextField pojoPathField) {
        this.pojoPathField = pojoPathField;
    }

    public CustomJTextField getSuperPathField() {
        return superPathField;
    }

    public void setSuperPathField(CustomJTextField superPathField) {
        this.superPathField = superPathField;
    }

    public Object getBindObject() {
        return bindObject;
    }

    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }

    public CustomJTextField getResultClassField() {
        return resultClassField;
    }

    public void setResultClassField(CustomJTextField resultClassField) {
        this.resultClassField = resultClassField;
    }
}
