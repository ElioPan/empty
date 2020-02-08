package com.ev.common.controller;

import org.springframework.stereotype.Controller;
import com.ev.framework.utils.ShiroUtils;
import com.ev.system.domain.UserDO;

@Controller
public class BaseController {
	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}