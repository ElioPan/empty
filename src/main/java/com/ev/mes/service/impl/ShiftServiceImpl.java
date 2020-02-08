package com.ev.mes.service.impl;

import com.ev.framework.utils.R;
import com.ev.mes.dao.ShiftDao;
import com.ev.mes.domain.ShiftDO;
import com.ev.mes.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class ShiftServiceImpl implements ShiftService {
	@Autowired
	private ShiftDao shiftDao;
	
	@Override
	public ShiftDO get(Long id){
		return shiftDao.get(id);
	}
	
	@Override
	public List<ShiftDO> list(Map<String, Object> map){
		return shiftDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return shiftDao.count(map);
	}
	
	@Override
	public int save(ShiftDO shift){
		return shiftDao.save(shift);
	}
	
	@Override
	public int update(ShiftDO shift){
		return shiftDao.update(shift);
	}
	
	@Override
	public int remove(Long id){
		return shiftDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return shiftDao.batchRemove(ids);
	}

	@Override
	public R saveAddChangeShift(ShiftDO  shiftDO){
		if(Objects.nonNull(shiftDO.getId())){
			//修改
			shiftDao.update(shiftDO);
			return R.ok();
		}else{
			//新增
			//<if test="maxNo != null and maxNo != ''"> and LEFT(work_orderNo,12) = #{maxNo} </if>
//			String prefix = DateFormatUtil.getWorkOrderno(ConstantForMES.SHIFT_BZPZ, new Date());
//			Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
//			params.put("maxNo", prefix);
//			params.put("offset", 0);
//			params.put("limit", 1);
//			List<ShiftDO> list = shiftDao.list(params);
//			String suffix = null;
//			if (list.size() > 0) {
//				suffix = list.get(0).getCode();
//			}
//			shiftDO.setCode(DateFormatUtil.getWorkOrderno(prefix, suffix));
			shiftDao.save(shiftDO);
			return R.ok();
		}
	}

	@Override
	public List<Map<String, Object>> listForMap(Map<String, Object> map) {
		return shiftDao.listForMap(map);
	}

	@Override
	public int deletOfShift(Map<String, Object> map) {
		return shiftDao.deletOfShift(map);
	}


}
