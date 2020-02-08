package com.ev.custom.service;

import com.ev.custom.domain.ClientLinkmanDO;

import java.util.List;
import java.util.Map;

/**
 * 客户联系人表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:02
 */
public interface ClientLinkmanService {
	
	ClientLinkmanDO get(Long id);
	
	List<ClientLinkmanDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ClientLinkmanDO clientLinkman);
	
	int update(ClientLinkmanDO clientLinkman);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	int updateByClientId(ClientLinkmanDO client);


	int removeByClientId(Long[] ids);

    void batchSave(List<ClientLinkmanDO> clientLinkmanDOs);
}
