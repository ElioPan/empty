package com.ev.custom.service.impl;

import com.ev.custom.dao.ClientDao;
import com.ev.custom.domain.ClientDO;
import com.ev.custom.service.ClientService;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.R;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	private ClientDao clientDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;
	
	@Override
	public ClientDO get(Long id){
		return clientDao.get(id);
	}
	
	@Override
	public List<ClientDO> list(Map<String, Object> map){
		return clientDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return clientDao.count(map);
	}
	
	@Override
	public int save(ClientDO client){
		client.setCreateTime(new Date());
		return clientDao.save(client);
	}
	
	@Override
	public int update(ClientDO client){
		client.setUpdateTime(new Date());
		return clientDao.update(client);
	}
	
	@Override
	public int remove(Long id){
		return clientDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return clientDao.batchRemove(ids);
	}

	@Override
	public List<Map<String,Object>> listForMap(Map<String, Object> map) {
		return clientDao.listForMap(map);
	}

	@Override
	public List<ClientDO> objectList() {
		return clientDao.objectList();
	}

	@Override
	public List<Map<String, Object>> checkClientsByparamete(Map<String, Object> map) {
		return clientDao.checkClientsByparamete(map);
	}


	@Override
	public R deletOfDevices(Long[] ids) {
		Map<String, Object> query = Maps.newHashMap();
		query.put("id", ids);
		int rows = clientDao.countOfClientStatus(query);

		if (rows == ids.length) {
			clientDao.deletOfDevices(query);
			return R.ok();
		}else{
			return R.error(messageSourceHandler.getMessage("apis.mes.scrapt.auditOk", null));
		}
	}

	@Override
	public int countOfCheckClients(Map<String, Object> map) {
		return clientDao.countOfCheckClients(map);
	}

	@Override
	public int countOfClientStatus(Map<String, Object> map) {
		return clientDao.countOfClientStatus( map);
	}

	@Override
	public int checkSave(Map<String, Object> map) {
		return clientDao.checkSave(map);
	}

    @Override
    public List<String> getAllCode() {
        return clientDao.getAllCode();
    }

    @Override
    public void batchSave(List<ClientDO> clientDOs) {
        clientDao.batchSave(clientDOs);
    }


}
