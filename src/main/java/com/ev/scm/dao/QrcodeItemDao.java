package com.ev.scm.dao;

import com.ev.scm.domain.QrcodeItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-02-20 14:28:20
 */
@Mapper
public interface QrcodeItemDao {

	QrcodeItemDO get(Long id);
	
	List<QrcodeItemDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(QrcodeItemDO qrcodeItem);
	
	int update(QrcodeItemDO qrcodeItem);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
