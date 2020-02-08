package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.R;
import com.ev.framework.utils.ShiroUtils;
import com.ev.custom.dao.NewsDao;
import com.ev.custom.domain.ContentAssocDO;
import com.ev.custom.domain.NewsDO;
import com.ev.custom.service.ContentAssocService;
import com.ev.custom.service.NewsService;
import com.ev.framework.il8n.MessageSourceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@EnableTransactionManagement
@Service
public class NewsServiceImpl implements NewsService {
	@Autowired
	private NewsDao newsDao;
	@Autowired
	private MessageSourceHandler messageSourceHandler;

	@Autowired
	private ContentAssocService contentAssocService;

	@Override
	public NewsDO get(Long id) {
		return newsDao.get(id);
	}

	@Override
	public List<NewsDO> list(Map<String, Object> map) {
		return newsDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return newsDao.count(map);
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return newsDao.listForMap(map);
	}

	@Override
	public int countForMap(Map<String, Object> map) {
		return newsDao.countForMap(map);
	}

	@Override
	public int save(NewsDO news) {
		return newsDao.save(news);
	}

	@Override
	public int update(NewsDO news) {
		return newsDao.update(news);
	}

	@Override
	public int remove(Long id) {
		return newsDao.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return newsDao.batchRemove(ids);
	}

	@Override
	public R dealDetails(Long newsId) {

		Map<String, Object> results = new HashMap<>();

		NewsDO newsDO = newsDao.get(newsId);
		if (newsDO != null) {
			//获取附件
			Map<String, Object> contentMap = new HashMap<String, Object>() {{
				put("assocId", newsId);
				put("assocType", Constant.NEWS_PRESS_RELEASE);
			}};
			List<ContentAssocDO> contentAssocDOS = contentAssocService.list(contentMap);

			results.put("initFileList", contentAssocDOS);
			results.put("newsHead", newsDO);

			return R.ok(results);
		}
		return R.error(messageSourceHandler.getMessage("common.massge.haveNoThing",null));
	}

	@Override
	public R listOfCanDelet(Long[] ids) {
		Map<String,Object> result = new HashMap<>();
		Map<String,Object> query = new HashMap<>();

		Long userId= ShiroUtils.getUserId();

		query.put("createBy",userId);
		query.put("id",ids);
		List<Map<String, Object>> mapList = newsDao.listOfCanDelet(query);

		if(Objects.equals(mapList.size(),ids.length)){
			Long deletIds[]=new Long[mapList.size()];
			for(int i=0;i<mapList.size();i++){
				Map<String,Object> maps=mapList.get(i);
				deletIds[i]=Long.parseLong(maps.get("id").toString());
			}
			newsDao.batchRemove(deletIds);
			contentAssocService.removeByAssocIdAndType(deletIds, Constant.NEWS_PRESS_RELEASE);
			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("common.device.batchDelet",null));
	}

	@Override
	public R saveChangOfUpdate(NewsDO newsDO, String pathAndName) {
		long newsId = newsDO.getId();
		if (Objects.nonNull(newsId)) {
			newsDao.update(newsDO);

			Long deletIds[] = new Long[1];
			deletIds[0] = newsId;
			contentAssocService.removeByAssocIdAndType(deletIds, Constant.NEWS_PRESS_RELEASE);

			JSONArray nameAndPath=new JSONArray();
			if(!"".equals(pathAndName)&&null!=pathAndName) {
				nameAndPath = JSONArray.parseArray(pathAndName);
			}
//				JSONArray nameAndPath = JSONArray.parseArray(pathAndName);

			contentAssocService.saveList(newsDO.getId(), nameAndPath, Constant.NEWS_PRESS_RELEASE);
			return R.ok();
		}
		return R.error(messageSourceHandler.getMessage("apis.check.buildWinStockD",null));
	}


}