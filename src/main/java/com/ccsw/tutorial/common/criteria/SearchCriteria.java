package com.ccsw.tutorial.common.criteria;

public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;
    private Object value2;

    public SearchCriteria(String key, String operation, Object value) {

        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(String key, String operation, Object value, Object value2) {

        this.key = key;
        this.operation = operation;
        this.value = value;
        this.value2 = value2;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
