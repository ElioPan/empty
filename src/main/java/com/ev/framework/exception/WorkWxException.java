package com.ev.framework.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xy
 */
public class WorkWxException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;

    private int code = 500;

    public WorkWxException(String msgStr) {
        super(msgStr);
        JSONObject jsonObject = JSONObject.parseObject(msgStr);
        String message = jsonObject.get("errmsg").toString();
        Integer code = Integer.parseInt(jsonObject.get("errcode").toString());
        this.code = code;
        this.msg = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
