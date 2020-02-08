package com.ev.custom.service.impl;

import com.ev.custom.service.WeChatService;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.WeChatUtil;
import com.google.common.collect.Maps;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Formatter;
import java.util.Map;
import java.util.UUID;
@Service
public class WeChatServiceImpl implements WeChatService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Map<String,Object> getSignature(String url) throws IOException, ParseException {
        JSONObject jsAccessToken = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_ACCESS_TOKEN));
        Date now = new Date();
        /**
         * 首先校验accessToken是否过期
         */
        if(jsAccessToken.size()==0){
            jsAccessToken = WeChatUtil.getAccessToken("wx6188bd4995b3f83b","-VXQbPLuS0FAPem5O5n_fcuLipHQP7fJjOmvxPVt8YU");
            jsAccessToken.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
            redisTemplate.opsForValue().set(Constant.WECHAT_ACCESS_TOKEN,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsAccessToken.get("expireDate").toString(),"yyyy-MM-dd HH:mm:ss").compareTo(now)<0){
                jsAccessToken = WeChatUtil.getAccessToken("wx6188bd4995b3f83b","-VXQbPLuS0FAPem5O5n_fcuLipHQP7fJjOmvxPVt8YU");
                jsAccessToken.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
                redisTemplate.opsForValue().set(Constant.WECHAT_ACCESS_TOKEN,jsAccessToken.toString());
            }
        }
        /**
         * 判断JSAPITICKET是否过期
         */
        String jsAccessTokenStr = jsAccessToken.optString("access_token");
        JSONObject jsApiTicket = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_JSAPI_TICKET));
        if(jsApiTicket.size()==0){
            jsApiTicket = WeChatUtil.getJsapiTicket(jsAccessTokenStr);
            jsApiTicket.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
            redisTemplate.opsForValue().set(Constant.WECHAT_JSAPI_TICKET,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsApiTicket.get("expireDate").toString(),"yyyy-MM-dd HH:mm:ss").compareTo(now)<0){
                jsApiTicket = WeChatUtil.getJsapiTicket(jsAccessTokenStr);
                jsApiTicket.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
                redisTemplate.opsForValue().set(Constant.WECHAT_JSAPI_TICKET,jsApiTicket.toString());
            }
        }
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
        Map<String,Object> results = Maps.newHashMap();
        results.put("appId","wx6188bd4995b3f83b");
        results.put("timestamp",now.getTime());
        results.put("nonceStr",noncestr);
        results.put("signature",signature);
        return results;
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
