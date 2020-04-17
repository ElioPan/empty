package com.ev.custom.service;

import com.ev.custom.domain.NoticeDO;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 企业微信接口
 */
public interface WeChatService {
    public Boolean checkIsUse();

    public JSONObject getAccessToken(Date now) throws IOException, ParseException;

    public JSONObject getJsapiTicket(String accessToken,Date now) throws IOException, ParseException;

    public JSONObject getSignature(String url,Date now) throws IOException, ParseException;

    /**
     *创建成员
     */
    public JSONObject createUser(UserDO userDO) throws IOException, ParseException;
    /**
     *创建成员
     */
    public JSONObject saveUser(UserDO userDO) throws IOException, ParseException;

    /**
     * 邀请成员加入
     * @param userIds
     * @return
     */
    public JSONObject inviteUser(List<String> userIds) throws IOException, ParseException;

    /**
     *读取成员
     */
    public JSONObject getUser(String userId) throws IOException, ParseException;

    /**
     *更新成员
     */
    public JSONObject updateUser(UserDO userDO) throws IOException, ParseException;

    /**
     *删除成员
     */
    public JSONObject deleteUser(String userId) throws IOException, ParseException;

    /**
     *批量删除成员
     */
    public JSONObject batchDeleteUser(List<String> userIds) throws IOException, ParseException;

    /**
     *获取部门成员
     */
    public JSONObject getUserSimpleList(String deptId,Integer fetchChild) throws IOException, ParseException;

    /**
     *获取部门成员详情
     */
    public JSONObject getUserList(String deptId,Integer fetchChild) throws IOException, ParseException;

    /**
     *创建部门
     */
    public JSONObject createDepartment(DeptDO deptDO) throws IOException, ParseException;

    /**
     *更新部门
     */
    public JSONObject updateDeptment(DeptDO deptDO) throws IOException, ParseException;

    /**
     *删除部门
     */
    public JSONObject deleteDepartment(String deptId) throws IOException, ParseException;

    /**
     *获取部门列表
     */
    public JSONObject getDepartmentList(String deptId) throws IOException, ParseException;

    /**
     *发送卡片消息
     */
    public JSONObject sendTextCardMessage(NoticeDO noticeDO,List<Long> userId) throws IOException, ParseException;


}
