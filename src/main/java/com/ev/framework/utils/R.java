package com.ev.framework.utils;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private static final String PREFIX = "芯软提示：";

	public R() {
		put("code", 0);
		put("msg", PREFIX+"操作成功");
	}

	public static R error() {
		return error(1, PREFIX+"操作失败");
	}

	public static R error(String msg) {
		return error(500, PREFIX+msg);
	}

	public static R everror(String msg) {
		return error(-1, PREFIX+msg);
	}

	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", PREFIX+msg);
		return r;
	}

    public static R error(int code, String msg, Map<String, Object> map) {
        R r = new R();
        r.put("code", code);
        r.put("msg", PREFIX+msg);
        r.put("data", map);
        return r;
    }

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", PREFIX+msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}


}
