package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ev.apis.model.DsResultResponse;
import com.ev.framework.utils.R;
import com.ev.custom.dao.DeviceDao;
import com.ev.custom.domain.DeviceDO;
import com.ev.custom.service.DeviceService;
import com.ev.custom.service.DictionaryService;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.system.service.DeptService;
import com.ev.system.service.UserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	@Autowired
	private MessageSourceHandler messageSourceHandler;


	
	@Override
	public DeviceDO get(Long id){
		return deviceDao.get(id);
	}
	
	@Override
	public List<DeviceDO> list(Map<String, Object> map){
		return deviceDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return deviceDao.count(map);
	}

	@Override
	public int parentCount() {
		return deviceDao.countParent();
	}

	@Override
	public int save(DeviceDO device){
		return deviceDao.save(device);
	}
	
	@Override
	public int update(DeviceDO device){
		return deviceDao.update(device);
	}
	
	@Override
	public int remove(Long id){
		return deviceDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return deviceDao.batchRemove(ids);
	}

	@Override
	public R apiDelete(Long[] deviceId) {
		Map<String,Object> query=new HashMap<String,Object>();
		//查询设备是否有子设备，有子设备不可删除，无子设备可删除
		Long ids[]=new Long[deviceId.length];
		for (int i=0;i<deviceId.length ;i++){
			DeviceDO deviceDo= this.deviceDao.get(deviceId[i]);
			if( deviceDo!=null) {
				if(!(deviceDo.getParentId()!=null)){
					ids[i]=deviceId[i];
				}else{
					//"已关联单据，不允许删除！"
					return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
				}
			}else{
				//"已关联单据，不允许删除！"
				return R.error(messageSourceHandler.getMessage("common.dailyReport.batchRemove",null));
			}
		}

		query.put("id",ids);
		deviceDao.deletOfDevices(query);
		return  R.ok();
	}

	@Override
	public Map<String, Object> getDeviceQRCode(Long deviceId) {
		Map<String,Object> results = Maps.newHashMap();
		DeviceDO device = this.deviceDao.get(deviceId);
		if (device!=null){
			JSONObject ob = new JSONObject(new LinkedHashMap());
			ob.put("name", device.getName() == null ? "" : device.getName());
			ob.put("serialno", device.getSerialno() == null ? "":device.getSerialno());
			ob.put("imageUrl", "/files/device/"+device.getSerialno()+".jpg");
			results.put("data",ob);
		}
		return results;
	}


	@Override
	public Map<String, Object> listApi(int pageno,int pagesize,String name) {
		Map<String,Object> dictMap = this.dictionaryService.getDictMap();
		Map<String,Object> userMap = this.userService.getUserMap();
		Map<String,Object> deptMap = this.deptService.getDeptMap();

		Map<String,Object> results = Maps.newHashMap();
		Map<String, Object> params = new HashMap<>();
		params.put("name",name);
//		params.put("deptId",deptId);
		params.put("offset",pageno-1);
		params.put("limit",pagesize);
//		List<DeviceDO> devices = deviceDao.list(params);

		List<DeviceDO> devices= deviceDao.listOfDeviceDetail(params);

//		Long total = (long)deviceDao.count(params);

		int  total=deviceDao.coumtOflistOfDevice(params);
		List<Map<String,Object>> datas = new ArrayList<>();

		if(devices!=null && devices.size()>0){
			for (DeviceDO device:devices){
				Map map = new HashMap();
				map.put("id", device.getId());
				map.put("serialno", device.getSerialno() == null ? "" : device.getSerialno());
				map.put("name", device.getName() == null ? "" : device.getName());
				map.put("model", device.getModel() == null ? "" : device.getModel());
				map.put("type", device.getType() == null ? "" : dictMap.get(device.getType().toString()));
				map.put("usingStatus", device.getUsingStatus() == null ? "" : dictMap.get(device.getUsingStatus().toString()));
				map.put("site", device.getSite() == null ? "" : device.getSite());
				map.put("deptName", device.getDeptId() == null ? "" : deptMap.get(device.getDeptId().toString()));
				map.put("engineerId", device.getEngineerId() == null ? "" : device.getEngineerId());
				map.put("userName", device.getUserId() == null ? "" : userMap.get(device.getUserId().toString()));
				map.put("usingTime", device.getUsingTime() == null ? "" : device.getUsingTime());
				datas.add(map);
			}
			DsResultResponse dsRet = new DsResultResponse();
			dsRet.setDatas(datas);
			dsRet.setPageno(pageno);
			dsRet.setPagesize(pagesize);
//			dsRet.setTotalRows(total.intValue());
			dsRet.setTotalRows(total);
			dsRet.setTotalPages((int) (total  +  pagesize  - 1) / pagesize);
			results.put("data",dsRet);
		}
		return results;
	}


	@Override
	public Map<String, Object> getDeviceDetail(Long deviceId) {
		Map<String,Object> results = Maps.newHashMap();
		DeviceDO device = this.deviceDao.get(deviceId);
		if(device!=null){
			results.put("data",device);
		}
		return results;
	}

	@Override
	public Map<String, Object> getDeviceMap() {
		List<DeviceDO> devices = deviceDao.list(new HashMap<String,Object>(16));
		Map<String,Object> map = new HashMap<>();
		if(devices!=null && devices.size()>0){
			for (DeviceDO device:devices) {
				map.put(device.getId().toString(),device.getName());
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getDevicesByUser(Long userId) {
		Map<String,Object> results = Maps.newHashMap();
		UserDO user = this.userService.get(userId);

		Map<String, Object> params = new HashMap<String, Object>();
		boolean isAdmin = this.userService.checkAdmin(user);

		if(isAdmin){
			//admin看所有部门下的设备
		}else{
			//非admin看
			DeptDO dept = this.deptService.get(user.getDeptId());//(params.get("deptId).toString())
			params.put("deptId",dept.getDeptId());
//			System.out.println("==========dept.getDeptId()======="+dept.getDeptId().toString()+"====================");
//			System.out.println("==========params======="+params.get("deptId").toString()+"====================");
		}

		List<Map<String, Object>> deviceListByDeptId = deviceDao.listByDeptId(params);
		DsResultResponse dsRet = new DsResultResponse();

		if(deviceListByDeptId!=null&&deviceListByDeptId.size()>0){
			dsRet.setDatas(deviceListByDeptId);
			results.put("data",dsRet);
		}else{
			results.put("data","此人所在的部门没有设备！");
		}
		return results;

//		if(devices!=null && devices.size()>0){
//			List<Map<String,Object>> datas = new ArrayList<>();
//			for(DeviceDO device:devices){
//				Map map = new HashMap();
//				map.put("id", device.getId());
//				map.put("name", device.getName());
//				//model 规格型号
//				map.put("model", device.getModel());
//				//type设备类型
//				map.put("type", device.getType());
//				//using_status  使用状况
//				map.put("usingStatus", device.getUsingStatus());
//				//site   安装地点
//				map.put("site", device.getSite());
//				//dept_id  使用部门
//				map.put("deptId", device.getDeptId());
//				//user_id  负责人
//				map.put("userId", device.getUserId());
//				//buy_time  购买时间
//				map.put("buyTime", device.getBuyTime());
//				//serialno  设备编号
//				map.put("serialno", device.getSerialno());
//				datas.add(map);
//			}
	}

	@Override
	public Map<String,Object> oneDeviceDetail(Map<String, Object> map) {
			return this.deviceDao.oneDeviceDetail(map);
	}

	@Override
	public List<Map<String, Object>> advancedQueryLists(Map<String, Object> map) {
		return deviceDao.advancedQueryLists(map);
	}

	@Override
	public int advancedCounts(Map<String, Object> map) {
		return deviceDao.advancedCounts(map);
	}

	@Override
	public List<Map<String, Object>> getAllDevices() {
		return deviceDao.getAllDevices();
	}

	@Override
	public List<DeviceDO> listOfDeviceDetail(Map<String, Object> map) {
		return deviceDao.listOfDeviceDetail(map);
	}


	@Override
	public List<Map<String, Object>> childListDev(Map<String, Object> map) {
		List<Map<String, Object>> childListss = deviceDao.childList(map);
		return childListss;
	}


	@Override
	public List<Map<String, Object>> deviceTypeIdName(){
		 List<Map<String, Object>> maps = deviceDao.deviceType();
		return maps;
	}

	@Override
	public int updateParentId(Long id,Long[] ids) {
		int count=0;
		for(int i=0;i<ids.length;i++){
			DeviceDO deviceDo =new DeviceDO();
			deviceDo.setId(ids[i]);
			deviceDo.setParentId(id);
			count+=deviceDao.updateParentId( deviceDo);
		}
		return count;
	}

	@Override
	public int desvinculao(Long[] ids) {
		int count=0;

		for(int i=0;i<ids.length;i++){
			DeviceDO deviceDo =new DeviceDO();
			deviceDo.setId(ids[i]);
			deviceDo.setParentId(null);
			count+=deviceDao.updateParentId( deviceDo);
		}
		return count;
	}

	@Override
	public List<Map<String, Object>> deviceHaveNoChildDevice() {
		return deviceDao.haveNoChildDevice();
	}

	@Override
	public List<Map<String, Object>> childrenDevice(Map<String, Object> map) {
		return deviceDao.deviceChildren(map);
	}


	@Override
	public R countOfAllDevice(){

		Map<String, Object> results = Maps.newHashMap();
		int countOfDevice = deviceDao.countOfDevice();
		int countOfClosingDown = deviceDao.countOfClosingDown();
		int countOfoperation = deviceDao.countOfoperation();
		results.put("countOfDevice",countOfDevice);
		results.put("countOfClosingDown",countOfClosingDown);
		results.put("countOfoperation",countOfoperation);

		return R.ok(results);
	}

	@Override
	public int countOfChildList(Map<String, Object> map) {
		return deviceDao.countOfChildList(map);
	}

    @Override
    public List<Map<String, Object>> listForMap(Map<String, Object> param) {
        return deviceDao.listForMap(param);
    }

    @Override
    public List<String> getAllCode() {
        return deviceDao.getAllCode();
    }

    @Override
    public void batchSave(List<DeviceDO> deviceDOs) {
        deviceDao.batchSave(deviceDOs);
    }

}
