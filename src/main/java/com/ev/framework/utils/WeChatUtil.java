package com.ev.framework.utils;

import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.vo.WeChatSettingEntity;
import com.ev.custom.vo.WxDeptEntity;
import com.ev.custom.vo.WxUserEntity;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForSYS;
import com.ev.framework.exception.WorkWxException;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信工具类
 *
 */
public class WeChatUtil {

	public static String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	public static String GETACCESSTOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=SECRET";

	public static String GETJSAPITICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";

	public static final String CREATE_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";

	public static final String INVITE_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/batch/invite?access_token=ACCESS_TOKEN";

	public static final String GET_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";

	public static final String UPDATE_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";

	public static final String DELETE_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=ACCESS_TOKEN&userid=USERID";

	public static final String BATCH_DELETE_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=ACCESS_TOKEN";

	public static final String SIMPLE_LIST_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD";

	public static final String LIST_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD";

	public static final String CREATE_DEPT_URI = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=ACCESS_TOKEN";

	public static final String UPDATE_DEPT_URI = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=ACCESS_TOKEN";

	public static final String DELETE_DEPT_URI = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=ACCESS_TOKEN&id=ID";

	public static final String LIST_DEPT_URI = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";

	public static final String SEND_MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

	public static final String OK_CODE = "0";

	public static final String KEY_ERROR_CODE = "errcode";


	/**
	通过code获取用户openid
	*/
	public static JSONObject getOpenid(String code) throws IOException {
		JSONObject jsonObject = null;
		String path = GETOPENID.replace("APPID", Constant.APPID_WECHAT).replace("SECRET", Constant.APPSECRET_WECHAT).replace("CODE", code);
		StringBuffer buffer = new StringBuffer();
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("POST");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		if(!OK_CODE.equals(jsonObject.get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(jsonObject.toString());
		}
		return jsonObject;
	}


	/**
	 * 判断用户是否关注了公众号
	 * @param token
	 * @param openid
	 * @return
	 */
	public static boolean checkIsFollow(String token,String openid){
		//用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
		Integer subscribe = 0;
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.fromObject(message);
			//System.out.println("JSON字符串："+demoJson);
			subscribe = demoJson.getInt("subscribe");

			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1==subscribe?true:false;
	}

	/**
	 * 获取accessToken
	 * @param corpid 企业号appId
	 * @param corpsecret 企业号应用的appSecret
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getAccessToken(String corpid, String corpsecret) throws IOException {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		String path = GETACCESSTOKEN.replace("CORPID", corpid).replace("SECRET", corpsecret);
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		if(!OK_CODE.equals(jsonObject.get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(jsonObject.toString());
		}
		return jsonObject;
	}

	/**
	 * 获取企业号的jsApiTicket
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static JSONObject getJsapiTicket(String accessToken) throws IOException {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		String path = GETJSAPITICKET.replace("ACCESS_TOKEN", accessToken);
		URL url = new URL(path);
		HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);
		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		jsonObject = JSONObject.fromObject(buffer.toString());
		if(!OK_CODE.equals(jsonObject.get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(jsonObject.toString());
		}
		return jsonObject;
	}

	/**
	 * 创建企业微信成员
	 * @param userDO
	 * @return
	 */
	
	public static JSONObject createUser(String accessToken, UserDO userDO) throws IOException, ParseException {
		WxUserEntity wxUserEntity = new WxUserEntity();
		wxUserEntity.setUserid(userDO.getUsername());
		wxUserEntity.setName(userDO.getName());
		wxUserEntity.setEnable(userDO.getStatus());
		List<Long> deptId = new ArrayList<Long>();
		deptId.add(userDO.getDeptId());
		wxUserEntity.setDepartment(deptId);
		wxUserEntity.setMobile(userDO.getMobile());
		wxUserEntity.setEmail(userDO.getEmail());
		String url = CREATE_USER_URI.replace("ACCESS_TOKEN", accessToken);
		String params = JSON.toJSONString(wxUserEntity);
		String json = HttpClientUtils.sendJsonStr(url, params);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);

	}

	/**
	 * 邀请成员加入
	 * @param userIds
	 * @return
	 */
	
	public static JSONObject inviteUser(String accessToken, List<String> userIds) throws IOException, ParseException {
		String url = INVITE_USER_URI.replace("ACCESS_TOKEN", accessToken);
		JSONObject params = new JSONObject();
		params.put("user",userIds);
		String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(params));
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject getUser(String accessToken, String userId) throws IOException, ParseException{
		String url = GET_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("USERID",userId);
		String json = HttpClientUtils.doGet(url,null);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject updateUser(String accessToken, UserDO userDO) throws IOException, ParseException {
		WxUserEntity wxUserEntity = new WxUserEntity();
		wxUserEntity.setUserid(userDO.getUsername());
		wxUserEntity.setName(userDO.getName());
		wxUserEntity.setEnable(userDO.getStatus());
		List<Long> deptId = new ArrayList<Long>();
		deptId.add(userDO.getDeptId());
		wxUserEntity.setDepartment(deptId);
		wxUserEntity.setMobile(userDO.getMobile());
		wxUserEntity.setEmail(userDO.getEmail());
		String url = UPDATE_USER_URI.replace("ACCESS_TOKEN", accessToken);
		String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(wxUserEntity));
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject deleteUser(String accessToken, String userId) throws IOException, ParseException {
		String url = DELETE_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("USERID",userId);
		String json = HttpClientUtils.doGet(url,null);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject batchDeleteUser(String accessToken,List<String> userIds) throws IOException, ParseException {
		String url = BATCH_DELETE_USER_URI.replace("ACCESS_TOKEN", accessToken);
		JSONObject params = new JSONObject();
		params.put("useridlist",userIds);
		String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(params));
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject getUserSimpleList(String accessToken,String deptId, Integer fetchChild) throws IOException, ParseException {
		String url = SIMPLE_LIST_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID",deptId).replace("FETCH_CHILD",fetchChild.toString());
		String json = HttpClientUtils.doGet(url,null);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject getUserList(String accessToken,String deptId, Integer fetchChild) throws IOException, ParseException {
		String url = LIST_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID",deptId).replace("FETCH_CHILD",fetchChild.toString());
		String json = HttpClientUtils.doGet(url,null);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject createDepartment(String accessToken,DeptDO deptDO) throws IOException, ParseException {
		WxDeptEntity wxDeptEntity = new WxDeptEntity();
		wxDeptEntity.setId(deptDO.getDeptId());
		wxDeptEntity.setName(deptDO.getName());
		wxDeptEntity.setParentid(deptDO.getParentId()==0?null:deptDO.getParentId());
		wxDeptEntity.setOrder(deptDO.getOrderNum());
		String url = CREATE_DEPT_URI.replace("ACCESS_TOKEN", accessToken);
		String params = JSON.toJSONString(wxDeptEntity);
		String json = HttpClientUtils.sendJsonStr(url, params);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject updateDeptment(String accessToken,DeptDO deptDO) throws IOException, ParseException {
		WxDeptEntity wxDeptEntity = new WxDeptEntity();
		wxDeptEntity.setId(deptDO.getDeptId());
		wxDeptEntity.setName(deptDO.getName());
		wxDeptEntity.setParentid(deptDO.getParentId()==0?null:deptDO.getParentId());
		wxDeptEntity.setOrder(deptDO.getOrderNum());
		String url = UPDATE_DEPT_URI.replace("ACCESS_TOKEN", accessToken);
		String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(wxDeptEntity));
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject deleteDepartment(String accessToken,String deptId) throws IOException, ParseException {
		String url = DELETE_DEPT_URI.replace("ACCESS_TOKEN", accessToken).replace("ID",deptId);
		String json = HttpClientUtils.doGet(url,null);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}

	
	public static JSONObject getDepartmentList(String accessToken,String deptId) throws IOException, ParseException {
		String url = LIST_DEPT_URI.replace("ACCESS_TOKEN", accessToken).replace("ID",deptId);
		String json = HttpClientUtils.doGet(url,null);
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}
	
	public static JSONObject sendTextCardMessage(String appId,String appUrl, String accessToken,NoticeDO noticeDO,String userName) throws IOException, ParseException {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser",userName);
		jsonObject.put("msgtype","textcard");
		jsonObject.put("agentid",appId);
		/**
		 * 卡片内容封装
		 */
		JSONObject textcardObject = new JSONObject();
		textcardObject.put("title",noticeDO.getTitle());
		textcardObject.put("description",noticeDO.getContent());
		textcardObject.put("url",appUrl+JSONObject.fromObject(noticeDO.getContentDetail()).get("url"));
		textcardObject.put("btntxt","查看详情");
		jsonObject.put("textcard",textcardObject);
		jsonObject.put("safe",0);
		jsonObject.put("enable_id_trans",0);
		jsonObject.put("enable_duplicate_check",0);
		String url = SEND_MESSAGE.replace("ACCESS_TOKEN", accessToken);
		String json = HttpClientUtils.sendJsonStr(url,JSON.toJSONString(jsonObject));
		if(!OK_CODE.equals(JSONObject.fromObject(json).get(KEY_ERROR_CODE).toString())){
			throw new WorkWxException(json);
		}
		return JSONObject.fromObject(json);
	}
}
