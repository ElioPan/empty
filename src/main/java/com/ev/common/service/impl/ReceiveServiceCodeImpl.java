package com.ev.common.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ev.common.service.ReceiveService;
import com.ev.framework.utils.JSONUtils;
import com.ev.framework.utils.RSAUtils;
import com.ev.system.domain.CompanyDO;
import com.ev.system.service.CompanyService;

/**
 * @author AirOrangeWorkSpace
 *
 */
@Service
public class ReceiveServiceCodeImpl implements ReceiveService {
	@Autowired
	private CompanyService codeService;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void saveServiceCodeForRedis(String serviceCode) {
		stringRedisTemplate.opsForValue().set("serviceCode", serviceCode);
	}

	@Override
	public void saveServiceCodeForMysql(String serviceCodeBase64) {
		CompanyDO companyDO = new CompanyDO();
		String serviceCode = new String(Base64.getDecoder().decode(serviceCodeBase64.getBytes()), StandardCharsets.UTF_8);
		Map<String, Object> serviceCodeMap = JSONUtils.jsonToMap(serviceCode);
		if(Objects.nonNull(serviceCodeMap)){
			String endTime = serviceCodeMap.getOrDefault("serviceEndTime","").toString();
			String startTime = serviceCodeMap.getOrDefault("serviceStartDate","").toString();
			String serviceLicense = serviceCodeMap.getOrDefault("serviceLicense","").toString();
			companyDO.setServiceCode(serviceLicense);
			companyDO.setServiceEndTime(endTime);
			companyDO.setServiceStartTime(startTime);
			companyDO.setId(1L);
			codeService.update(companyDO);
		}
	}

	@Override
	public String getServiceCodeByRedis() {
		String serviceCode = stringRedisTemplate.opsForValue().get("serviceCode");
		if (Objects.isNull(serviceCode)) {
			String serviceCodeByMysql = getServiceCodeByMysql();
			this.saveServiceCodeForRedis(serviceCodeByMysql);
			return serviceCodeByMysql;
		}
		return serviceCode;
	}

	@Override
	public String getServiceCodeByMysql() {
		return codeService.getServiceCode(1L).getServiceCode();
	}

	@Override
	public boolean is(String serviceCode) throws Exception {
		String serviceCodeBase = new String(Base64.getDecoder().decode(serviceCode.getBytes()), StandardCharsets.UTF_8.name());
		Map<String, Object> serviceCodeMap = JSONUtils.jsonToMap(serviceCodeBase);
		// 验签
		if(Objects.nonNull(serviceCodeMap)){
			String publicKey = serviceCodeMap.get("publicKey").toString();
			String serviceLicense = serviceCodeMap.get("serviceLicense").toString();
			String endTime = serviceCodeMap.get("serviceEndTime").toString();
			return RSAUtils.verify(endTime.getBytes(), publicKey, serviceLicense);
		}
		return  false;
	}

}
