package com.ev.mes.service;

import java.util.List;
import java.util.Map;

import com.ev.framework.utils.R;
import com.ev.mes.domain.BomDO;
import org.springframework.web.multipart.MultipartFile;

/**
 * BOM主表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-11-19 08:45:05
 */
public interface BomService {

	BomDO get(Long id);

	List<BomDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(BomDO bom);

	int update(BomDO bom);

	int remove(Long id);

	int batchRemove(Long[] ids);

	Map<String, Object> getDetailInfo(Long id);

    R add(BomDO bom, String childBomArray, String uploadAttachments);

	Map<String, Object> getDetail(Long id);

	R edit(BomDO bom, String childBomArray, Long[] ids, String uploadAttachments);

	int removeHeadAndBody(Long id);

	void batchRemoveHeadAndBody(Long[] ids);

	boolean isAudit(Long id);

	int audit(Long id);

	int reverseAudit(Long id);

	boolean isNoRepeat(String serialNo);

	int countForMap(Map<String, Object> params);

	List<Map<String, Object>> listForMap(Map<String, Object> params);

	R importExcel(MultipartFile file);
}
