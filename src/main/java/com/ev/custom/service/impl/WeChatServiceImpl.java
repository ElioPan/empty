package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.service.WeChatService;
import com.ev.custom.vo.WxDeptEntity;
import com.ev.custom.vo.WxUserEntity;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.HttpClientUtils;
import com.ev.framework.utils.WeChatUtil;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.UserService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

@Service
public class WeChatServiceImpl implements WeChatService {
    @Value("${wechat.corpid}")
    private String corpid;

    @Value("${wechat.agentid}")
    private String agentid;

    @Value("${wechat.mobilesecret}")
    private String mobilesecret;

    @Value("${wechat.corpsecret}")
    private String corpsecret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;
    
    public static final String EXPIRE_DATE = "expireDate";

    public static final String CREATE_USER_URI = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";

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

    @Override
    public String getMobileAccessToken(Date now) throws IOException, ParseException {
        JSONObject jsAccessToken = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_ACCESS_TOKEN));
        /**
         * 首先校验mobileAccessToken是否过期
         */
        if(jsAccessToken.size()==0){
            jsAccessToken = WeChatUtil.getAccessToken(corpid,mobilesecret);
            jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
            redisTemplate.opsForValue().set(Constant.WECHAT_MOBILE_ACCESS_TOKEN,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsAccessToken.get(EXPIRE_DATE).toString(), DateFormatUtil.DATE_PATTERN).compareTo(now)<0){
                jsAccessToken = WeChatUtil.getAccessToken(corpid,mobilesecret);
                jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
                redisTemplate.opsForValue().set(Constant.WECHAT_MOBILE_ACCESS_TOKEN,jsAccessToken.toString());
            }
        }
        return jsAccessToken.optString("access_token");
    }

    @Override
    public JSONObject getAccessToken(Date now) throws IOException, ParseException {
        JSONObject jsAccessToken = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_ACCESS_TOKEN));
        /**
         * 首先校验accessToken是否过期
         */
        if(jsAccessToken.size()==0){
            jsAccessToken = WeChatUtil.getAccessToken(corpid,corpsecret);
            jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
            redisTemplate.opsForValue().set(Constant.WECHAT_ACCESS_TOKEN,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsAccessToken.get(EXPIRE_DATE).toString(), DateFormatUtil.DATE_PATTERN).compareTo(now)<0){
                jsAccessToken = WeChatUtil.getAccessToken(corpid,corpsecret);
                jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
                redisTemplate.opsForValue().set(Constant.WECHAT_ACCESS_TOKEN,jsAccessToken.toString());
            }
        }
        return jsAccessToken;
    }

    @Override
    public JSONObject getJsapiTicket(String accessToken,Date now) throws IOException, ParseException {
        JSONObject jsApiTicket = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_JSAPI_TICKET));
        if(jsApiTicket.size()==0){
            jsApiTicket = WeChatUtil.getJsapiTicket(accessToken);
            jsApiTicket.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
            redisTemplate.opsForValue().set(Constant.WECHAT_JSAPI_TICKET,jsApiTicket.toString());
        }else{
            if(DateUtils.parseDate(jsApiTicket.get(EXPIRE_DATE).toString(),DateFormatUtil.DATE_PATTERN).compareTo(now)<0){
                jsApiTicket = WeChatUtil.getJsapiTicket(accessToken);
                jsApiTicket.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
                redisTemplate.opsForValue().set(Constant.WECHAT_JSAPI_TICKET,jsApiTicket.toString());
            }
        }
        return jsApiTicket;
    }

    @Override
    public JSONObject getSignature(String url,Date now) throws IOException, ParseException {
        /**
         * 判断JSAPITICKET是否过期
         */
        String jsAccessTokenStr = getAccessToken(now).optString("access_token");
        JSONObject jsApiTicket = getJsapiTicket(jsAccessTokenStr,now);
        /**
         * 生成签名
         */
        String noncestr = UUID.randomUUID().toString();
        String signatureStr = "jsapi_ticket="+ jsApiTicket.optString("ticket")+"&noncestr="+ noncestr +"&timestamp="+now.getTime() +"&url="+url;
        String signature = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signatureStr.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        JSONObject results = new JSONObject();
        results.put("appId","wx6188bd4995b3f83b");
        results.put("timestamp",now.getTime());
        results.put("nonceStr",noncestr);
        results.put("signature",signature);
        return results;
    }

    /**
     * 创建企业微信成员
     * @param userDO
     * @return
     */
    @Override
    public JSONObject createUser(UserDO userDO) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        WxUserEntity wxUserEntity = new WxUserEntity();
        wxUserEntity.setUserid(userDO.getUsername());
        wxUserEntity.setName(userDO.getName());
        List<Long> deptId = new ArrayList<Long>();
        deptId.add(userDO.getDeptId());
        wxUserEntity.setDepartment(deptId);
        wxUserEntity.setMobile(userDO.getMobile());
        wxUserEntity.setEmail(userDO.getEmail());
        String url = CREATE_USER_URI.replace("ACCESS_TOKEN", accessToken);
        String params = JSON.toJSONString(wxUserEntity);
        String json = HttpClientUtils.sendJsonStr(url, params);
        return JSONObject.fromObject(json);

    }

    @Override
    public JSONObject getUser(String userId) throws IOException, ParseException{
        String accessToken = getMobileAccessToken(new Date());
        String url = GET_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("USERID",userId);
        String json = HttpClientUtils.doGet(url,null);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject updateUser(UserDO userDO) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        WxUserEntity wxUserEntity = new WxUserEntity();
        wxUserEntity.setUserid(userDO.getUsername());
        wxUserEntity.setName(userDO.getName());
        List<Long> deptId = new ArrayList<Long>();
        deptId.add(userDO.getDeptId());
        wxUserEntity.setDepartment(deptId);
        wxUserEntity.setMobile(userDO.getMobile());
        wxUserEntity.setEmail(userDO.getEmail());
        String url = UPDATE_USER_URI.replace("ACCESS_TOKEN", accessToken);
        String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(wxUserEntity));
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject deleteUser(String userId) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        String url = DELETE_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("USERID",userId);
        String json = HttpClientUtils.doGet(url,null);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject batchDeleteUser(List<String> userIds) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        String url = BATCH_DELETE_USER_URI.replace("ACCESS_TOKEN", accessToken);
        JSONObject params = new JSONObject();
        params.put("useridlist",userIds);
        String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(params));
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject getUserSimpleList(String deptId, Integer fetchChild) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        String url = SIMPLE_LIST_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID",deptId).replace("FETCH_CHILD",fetchChild.toString());
        String json = HttpClientUtils.doGet(url,null);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject getUserList(String deptId, Integer fetchChild) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        String url = LIST_USER_URI.replace("ACCESS_TOKEN", accessToken).replace("DEPARTMENT_ID",deptId).replace("FETCH_CHILD",fetchChild.toString());
        String json = HttpClientUtils.doGet(url,null);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject createDepartment(DeptDO deptDO) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        WxDeptEntity wxDeptEntity = new WxDeptEntity();
        wxDeptEntity.setId(deptDO.getDeptId());
        wxDeptEntity.setName(deptDO.getName());
        wxDeptEntity.setParentid(deptDO.getParentId()==0?null:deptDO.getParentId());
        wxDeptEntity.setOrder(deptDO.getOrderNum());
        String url = CREATE_DEPT_URI.replace("ACCESS_TOKEN", accessToken);
        String params = JSON.toJSONString(wxDeptEntity);
        String json = HttpClientUtils.sendJsonStr(url, params);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject updateDeptment(DeptDO deptDO) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        WxDeptEntity wxDeptEntity = new WxDeptEntity();
        wxDeptEntity.setId(deptDO.getDeptId());
        wxDeptEntity.setName(deptDO.getName());
        wxDeptEntity.setParentid(deptDO.getParentId()==0?null:deptDO.getParentId());
        wxDeptEntity.setOrder(deptDO.getOrderNum());
        String url = UPDATE_DEPT_URI.replace("ACCESS_TOKEN", accessToken);
        String json = HttpClientUtils.sendJsonStr(url, JSON.toJSONString(wxDeptEntity));
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject deleteDepartment(String deptId) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        String url = DELETE_DEPT_URI.replace("ACCESS_TOKEN", accessToken).replace("ID",deptId);
        String json = HttpClientUtils.doGet(url,null);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject getDepartmentList(String deptId) throws IOException, ParseException {
        String accessToken = getMobileAccessToken(new Date());
        String url = LIST_DEPT_URI.replace("ACCESS_TOKEN", accessToken).replace("ID",deptId);
        String json = HttpClientUtils.doGet(url,null);
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject sendTextMessage(NoticeDO noticeDO,List<Long> userId) throws IOException, ParseException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser",userId);
        jsonObject.put("msgtype","text");
        jsonObject.put("agentid",agentid);
        JSONObject contentObject = new JSONObject();
        contentObject.put("content",noticeDO.getContent());
        jsonObject.put("text",contentObject);
        jsonObject.put("safe",0);
        jsonObject.put("enable_id_trans",0);
        jsonObject.put("enable_duplicate_check",0);
        String accessToken = getAccessToken(new Date()).optString("access_token");
        String url = SEND_MESSAGE.replace("ACCESS_TOKEN", accessToken);
        String json = HttpClientUtils.sendJsonStr(url,JSON.toJSONString(jsonObject));
        return JSONObject.fromObject(json);
    }

    @Override
    public JSONObject sendTextCardMessage(NoticeDO noticeDO,List<Long> userId) throws IOException, ParseException {
        String userIds = userService.selectByIdSet(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser",userIds);
        jsonObject.put("msgtype","textcard");
        jsonObject.put("agentid",agentid);
        /**
         * 卡片内容封装
         */
        JSONObject textcardObject = new JSONObject();
        textcardObject.put("title",noticeDO.getTitle());
        textcardObject.put("description",noticeDO.getContent());
        textcardObject.put("url","http://120.132.17.220:18090/gyhl-app/#/pages/daily/dailyDetail?id=136&num=2");
        textcardObject.put("btntxt","查看详情");
        jsonObject.put("textcard",textcardObject);
        jsonObject.put("safe",0);
        jsonObject.put("enable_id_trans",0);
        jsonObject.put("enable_duplicate_check",0);
        String accessToken = getAccessToken(new Date()).optString("access_token");
        String url = SEND_MESSAGE.replace("ACCESS_TOKEN", accessToken);
        String json = HttpClientUtils.sendJsonStr(url,JSON.toJSONString(jsonObject));
        return JSONObject.fromObject(json);
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
