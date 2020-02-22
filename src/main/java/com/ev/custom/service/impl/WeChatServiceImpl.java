package com.ev.custom.service.impl;

import com.ev.custom.service.WeChatService;
import com.ev.framework.config.Constant;
import com.ev.framework.utils.WeChatUtil;
import com.google.common.collect.Maps;
import com.squareup.moshi.Json;
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
    public JSONObject getAccessToken(String corpid, String corpsecre,Date now) throws IOException, ParseException {
        JSONObject jsAccessToken = JSONObject.fromObject(redisTemplate.opsForValue().get(Constant.WECHAT_ACCESS_TOKEN));
        /**
         * 首先校验accessToken是否过期
         */
        if(jsAccessToken.size()==0){
            jsAccessToken = WeChatUtil.getAccessToken(corpid,corpsecre);
            jsAccessToken.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
            redisTemplate.opsForValue().set(Constant.WECHAT_ACCESS_TOKEN,jsAccessToken.toString());
        }else{
            if(DateUtils.parseDate(jsAccessToken.get("expireDate").toString(),"yyyy-MM-dd HH:mm:ss").compareTo(now)<0){
                jsAccessToken = WeChatUtil.getAccessToken(corpid,corpsecre);
                jsAccessToken.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
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
            jsApiTicket.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
            redisTemplate.opsForValue().set(Constant.WECHAT_JSAPI_TICKET,accessToken.toString());
        }else{
            if(DateUtils.parseDate(jsApiTicket.get("expireDate").toString(),"yyyy-MM-dd HH:mm:ss").compareTo(now)<0){
                jsApiTicket = WeChatUtil.getJsapiTicket(accessToken);
                jsApiTicket.put("expireDate", com.ev.framework.utils.DateUtils.format(DateUtils.addSeconds(now,4800),"yyyy-MM-dd HH:mm:ss"));
                redisTemplate.opsForValue().set(Constant.WECHAT_JSAPI_TICKET,jsApiTicket.toString());
            }
        }
        return jsApiTicket;
    }

    @Override
    public JSONObject getSignature(String corpid, String corpsecre,String url,Date now) throws IOException, ParseException {
        /**
         * 判断JSAPITICKET是否过期
         */
        String jsAccessTokenStr = getAccessToken(corpid,corpsecre,now).optString("access_token");
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
