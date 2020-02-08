package com.ev.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ev.common.service.ReceiveService;
import com.ev.framework.utils.R;

/**
 * @author AirOrangeWorkSpace
 *
 */
@RequestMapping("/common/serviceCode")
@Controller
public class ServiceCodeController {
	@Autowired
	private ReceiveService service;
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(@RequestParam(name = "serviceCode", required = true, defaultValue = "")String serviceCode) {
		try {
			service.saveServiceCodeForRedis(serviceCode);
			service.saveServiceCodeForMysql(serviceCode);
			return R.ok();
		} catch (Exception e) {
			return R.error(e.getMessage());
		}
	}
}
