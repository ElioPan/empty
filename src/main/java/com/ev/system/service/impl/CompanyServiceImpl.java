package com.ev.system.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.ev.framework.il8n.MessageSourceHandler;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ev.common.service.ReceiveService;
import com.ev.framework.utils.R;
import com.ev.system.dao.CompanyDao;
import com.ev.system.domain.CompanyDO;
import com.ev.system.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Override
	public Map<String, Object> get(Long id) {
		return companyDao.get(id);
	}

	@Override
	public List<CompanyDO> list(Map<String, Object> map) {
		return companyDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return companyDao.count(map);
	}

	@Override
	public int save(CompanyDO company) {
		return companyDao.save(company);
	}

	@Override
	public int update(CompanyDO company) {
		return companyDao.update(company);
	}

	@Override
	public int remove(Long id) {
		return companyDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return companyDao.batchRemove(ids);
	}

	@Override
	public CompanyDO getServiceCode(Long id) {
		return companyDao.getServiceCode(id);
	}

	@Override
	public R importServiceCode(CompanyDO companyDO, MultipartFile importServiceCode) {
		if (importServiceCode.isEmpty()) {
			return R.error(messageSourceHandler.getMessage("license.isEmpty.error",null));
		}
		BufferedReader reader = null;
		InputStream inputStream = null;
		StringBuilder serviceCodeBuilder = new StringBuilder();
		try {
			inputStream = importServiceCode.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				serviceCodeBuilder.append(line);
			}
			String serviceCodeBase64 = new String(serviceCodeBuilder);
			if (!receiveService.is(serviceCodeBase64)) {
				return R.error(messageSourceHandler.getMessage("license.verify.error",null));
			}
			receiveService.saveServiceCodeForRedis(serviceCodeBase64);
			receiveService.saveServiceCodeForMysql(serviceCodeBase64);
			inputStream.close();
			reader.close();
			return R.ok();
		} catch (IOException e) {
			return R.error(messageSourceHandler.getMessage("license.IO.error",null));
		} catch (Exception e) {
			return R.error(messageSourceHandler.getMessage("license.reload.error",null));
		}finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(inputStream);
		}

	}

}
