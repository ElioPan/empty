package com.ev.common.dao;

import com.ev.common.domain.DictDO;
import com.ev.system.domain.DeptDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 导入用户
 */
@Mapper
public interface ImportUserDao {

	List<DeptDO> getDeptList();
}
