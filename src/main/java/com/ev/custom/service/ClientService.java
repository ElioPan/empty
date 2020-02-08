package com.ev.custom.service;

import com.ev.custom.domain.ClientDO;
import com.ev.framework.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 客户表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-04 13:56:21
 */
public interface ClientService {
	
	ClientDO get(Long id);
	
	List<ClientDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ClientDO client);
	
	int update(ClientDO client);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    List<Map<String,Object>> listForMap(Map<String, Object> map);

    List<ClientDO> objectList();

	List<Map<String,Object>> checkClientsByparamete(Map<String,Object> map);

	R deletOfDevices(Long[] ids);

	int countOfCheckClients(Map<String,Object> map);

	int countOfClientStatus(Map<String,Object>  map);

	int checkSave(Map<String,Object>map);


    List<String> getAllCode();

    void batchSave(List<ClientDO> clientDOs);
}