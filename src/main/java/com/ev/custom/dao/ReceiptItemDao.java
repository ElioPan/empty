package com.ev.custom.dao;

import com.ev.custom.domain.ReceiptItemDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-01 14:40:37
 */
@Mapper
public interface ReceiptItemDao {

	List<Map<String,Object>> get(Long rid);
	
	List<ReceiptItemDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ReceiptItemDO receiptItem);
	
	int update(ReceiptItemDO receiptItem);
	
	int remove(Long rid);
	
	int batchRemove(Long[] rids);
}
