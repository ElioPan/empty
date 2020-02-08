package com.ev.custom.dao;

import com.ev.custom.domain.ClientLinkmanDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 客户联系人表
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-05 15:54:02
 */
@Mapper
public interface ClientLinkmanDao {

	ClientLinkmanDO get(Long id);
	
	List<ClientLinkmanDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ClientLinkmanDO clientLinkman);
	
	int update(ClientLinkmanDO clientLinkman);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	int removeByClientId(Long[] ids);

	int updateByClientId(ClientLinkmanDO client);

    int batchSave(List<ClientLinkmanDO> clientLinkmanDOs);
}
