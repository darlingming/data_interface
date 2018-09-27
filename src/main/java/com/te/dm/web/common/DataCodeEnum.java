package com.te.dm.web.common;

/**
 * @author DM
 */
public enum DataCodeEnum {

    SUCCESS("20000", "Success"),
    DATA_NOT_FOUND("40004", "Not found"),
    NOT_PROTOCOL_TYPE("40000", "NOT protocol type"),
    ERROR("50000", "Service exception"),
    FAIL_SIGN("40001", "Token Invalid"),
    NOT_SERVICE("40003", "Not service");
    private String code;
    private String msg;

    DataCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
