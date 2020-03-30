package com.ev.framework.utils;

import com.ev.framework.config.Constant;
import com.ev.framework.exception.WorkWxException;
import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 微信工具类
 *
 */
public class WeChatUtil {

	public static String GETOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	public static String GETACCESSTOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=CORPID&corpsecret=SECRET";

	public static String GETJSAPITICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";

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
}
