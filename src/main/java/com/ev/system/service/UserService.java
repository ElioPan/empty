package com.ev.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ev.system.vo.UserVO;
import org.springframework.stereotype.Service;

import com.ev.common.domain.Tree;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
	UserDO get(Long id);

	List<UserDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	List<Map<String,Object>> listForMap(Map<String, Object> map);

	int countForMap(Map<String, Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(Long userId);

	int batchremove(Long[] userIds);

	boolean exit(Map<String, Object> params);

	Set<String> listRoles(Long userId);

	int resetPwd(UserVO userVO,UserDO userDO) throws Exception;
	int adminResetPwd(UserVO userVO) throws Exception;
	Tree<DeptDO> getTree();

	/**
	 * 更新个人信息
	 * @param userDO
	 * @return
	 */
	int updatePersonal(UserDO userDO);

	/**
	 * 更新个人图片
	 * @param file 图片
	 * @param avatar_data 裁剪信息
	 * @param userId 用户ID
	 * @throws Exception
	 */
    Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;

    UserDO findByUsername(String username);

	int appResetPasswd(UserDO userDO, String oldpasswd, String newpasswd)throws Exception;

	boolean isAdmin(UserDO currUser);

    Map<String,Object> getUsers(Long deptId, Integer isContainChildren);

    Map<String,Object> getUserMap();

	boolean checkAdmin(UserDO user);

    int edit(UserDO user);
}
