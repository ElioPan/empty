package com.ev.custom.service.impl;

import com.alibaba.fastjson.JSON;
import com.ev.custom.domain.NoticeDO;
import com.ev.custom.service.WeChatService;
import com.ev.custom.vo.WeChatSettingEntity;
import com.ev.custom.vo.WxDeptEntity;
import com.ev.custom.vo.WxUserEntity;
import com.ev.framework.config.Constant;
import com.ev.framework.config.ConstantForSYS;
import com.ev.framework.exception.WorkWxException;
import com.ev.framework.utils.DateFormatUtil;
import com.ev.framework.utils.HttpClientUtils;
import com.ev.framework.utils.WeChatUtil;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import com.ev.system.service.SettingService;
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
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;
    
    public static final String EXPIRE_DATE = "expireDate";

    @Override
    public Boolean checkIsUse() {
        WeChatSettingEntity setting = JSON.parseObject(settingService.getSettingValue(ConstantForSYS.QIYE_WECHAT_SETTING),WeChatSettingEntity.class);
        if(setting != null && setting.getUserOrNot()==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public JSONObject getAccessToken(Date now) throws IOException, ParseException {
        /**
         * 获取企业微信配置信息
         */
        WeChatSettingEntity setting = JSON.parseObject(settingService.getSettingValue(ConstantForSYS.QIYE_WECHAT_SETTING),WeChatSettingEntity.class);
        JSONObject jsAccessToken = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_ACCESS_TOKEN));
        /**
         * 首先校验accessToken是否过期
         */
        if(jsAccessToken.size()==0){
            jsAccessToken = WeChatUtil.getAccessToken(setting.getCorpId(),setting.getAppSecret());
            jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
            redisTemplate.opsForValue().set(Constant.WECHAT_ACCESS_TOKEN,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsAccessToken.get(EXPIRE_DATE).toString(), DateFormatUtil.DATE_PATTERN).compareTo(now)<0){
                jsAccessToken = WeChatUtil.getAccessToken(setting.getCorpId(),setting.getAppSecret());
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
         * 获取企业微信配置信息
         */
        WeChatSettingEntity setting = JSON.parseObject(settingService.getSettingValue(ConstantForSYS.QIYE_WECHAT_SETTING),WeChatSettingEntity.class);

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
        results.put("appId",setting.getAppId());
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
        return WeChatUtil.createUser(getMobileAccessToken(new Date()),userDO);

    }

    /**
     * 保存成员
     *
     * @param userDO
     */
    @Override
    public JSONObject saveUser(UserDO userDO) throws IOException, ParseException {
        try{
            getUser(userDO.getUsername());
            return updateUser(userDO);
        }catch (WorkWxException ex){
            return createUser(userDO);
        }
    }

    /**
     * 邀请成员加入
     * @param userIds
     * @return
     */
    @Override
    public JSONObject inviteUser(List<String> userIds) throws IOException, ParseException {
        return WeChatUtil.inviteUser(getMobileAccessToken(new Date()),userIds);
    }

    @Override
    public JSONObject getUser(String userId) throws IOException, ParseException{
        return WeChatUtil.getUser(getMobileAccessToken(new Date()),userId);
    }

    @Override
    public JSONObject updateUser(UserDO userDO) throws IOException, ParseException {
        return WeChatUtil.updateUser(getMobileAccessToken(new Date()),userDO);
    }

    @Override
    public JSONObject deleteUser(String userId) throws IOException, ParseException {
        return WeChatUtil.deleteUser(getMobileAccessToken(new Date()),userId);
    }

    @Override
    public JSONObject batchDeleteUser(List<String> userIds) throws IOException, ParseException {
        return WeChatUtil.batchDeleteUser(getMobileAccessToken(new Date()),userIds);
    }

    @Override
    public JSONObject getUserSimpleList(String deptId, Integer fetchChild) throws IOException, ParseException {
        return WeChatUtil.getUserSimpleList(getMobileAccessToken(new Date()),deptId,fetchChild);
    }

    @Override
    public JSONObject getUserList(String deptId, Integer fetchChild) throws IOException, ParseException {
        return WeChatUtil.getUserList(getMobileAccessToken(new Date()),deptId,fetchChild);
    }

    @Override
    public JSONObject createDepartment(DeptDO deptDO) throws IOException, ParseException {
        return WeChatUtil.createDepartment(getMobileAccessToken(new Date()),deptDO);
    }

    @Override
    public JSONObject updateDeptment(DeptDO deptDO) throws IOException, ParseException {
        return WeChatUtil.updateDeptment(getMobileAccessToken(new Date()),deptDO);
    }

    @Override
    public JSONObject deleteDepartment(String deptId) throws IOException, ParseException {
        return WeChatUtil.deleteDepartment(getMobileAccessToken(new Date()),deptId);
    }

    @Override
    public JSONObject getDepartmentList(String deptId) throws IOException, ParseException {
        return WeChatUtil.getDepartmentList(getMobileAccessToken(new Date()),deptId);
    }

    @Override
    public JSONObject sendTextCardMessage(NoticeDO noticeDO,List<Long> userIds) throws IOException, ParseException {
        /**
         * 获取企业微信配置信息
         */
        WeChatSettingEntity setting = JSON.parseObject(settingService.getSettingValue(ConstantForSYS.QIYE_WECHAT_SETTING),WeChatSettingEntity.class);

        for(Long userId: userIds){
            UserDO userDO = userService.get(userId);
            WeChatUtil.sendTextCardMessage(setting.getAppId(),setting.getAppUrl(), getMobileAccessToken(new Date()),noticeDO,userDO.getUsername());
        }
        return null;
    }

    private String getMobileAccessToken(Date now) throws IOException, ParseException {
        /**
         * 获取企业微信配置信息
         */
        WeChatSettingEntity setting = JSON.parseObject(settingService.getSettingValue(ConstantForSYS.QIYE_WECHAT_SETTING),WeChatSettingEntity.class);
        JSONObject jsAccessToken = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_MOBILE_ACCESS_TOKEN));
        /**
         * 首先校验mobileAccessToken是否过期
         */
        if(jsAccessToken.size()==0){
            jsAccessToken = WeChatUtil.getAccessToken(setting.getCorpId(),setting.getMobileSecret());
            jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
            redisTemplate.opsForValue().set(Constant.WECHAT_MOBILE_ACCESS_TOKEN,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsAccessToken.get(EXPIRE_DATE).toString(), DateFormatUtil.DATE_PATTERN).compareTo(now)<0){
                jsAccessToken = WeChatUtil.getAccessToken(setting.getCorpId(),setting.getMobileSecret());
                jsAccessToken.put(EXPIRE_DATE, com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),DateFormatUtil.DATE_PATTERN));
                redisTemplate.opsForValue().set(Constant.WECHAT_MOBILE_ACCESS_TOKEN,jsAccessToken.toString());
            }
        }
        return jsAccessToken.optString("access_token");
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
