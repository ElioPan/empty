package com.ev.custom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ev.custom.dao.ClientLinkmanDao;
import com.ev.custom.domain.ClientLinkmanDO;
import com.ev.custom.service.ClientLinkmanService;



@Service
public class ClientLinkmanServiceImpl implements ClientLinkmanService {
	@Autowired
	private ClientLinkmanDao clientLinkmanDao;
	
	@Override
	public ClientLinkmanDO get(Long id){
		return clientLinkmanDao.get(id);
	}
	
	@Override
	public List<ClientLinkmanDO> list(Map<String, Object> map){
		return clientLinkmanDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return clientLinkmanDao.count(map);
	}
	
	@Override
	public int save(ClientLinkmanDO clientLinkman){
		clientLinkman.setCreateTime(new Date());
		return clientLinkmanDao.save(clientLinkman);
	}
	
	@Override
	public int update(ClientLinkmanDO clientLinkman){

		clientLinkman.setUpdateTime(new Date());
		return clientLinkmanDao.update(clientLinkman);
	}
	
	@Override
	public int remove(Long id){
		return clientLinkmanDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return clientLinkmanDao.batchRemove(ids);
	}

	@Override
	public int updateByClientId(ClientLinkmanDO client) {
		return clientLinkmanDao.updateByClientId(client);
	}

	@Override
	public int removeByClientId(Long[] id) {
		return clientLinkmanDao.removeByClientId(id);
	}

    @Override
    public void batchSave(List<ClientLinkmanDO> clientLinkmanDOs) {
        clientLinkmanDao.batchSave(clientLinkmanDOs);
    }

}
