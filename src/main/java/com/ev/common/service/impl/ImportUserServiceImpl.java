package com.ev.common.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.ev.common.dao.ImportUserDao;
import com.ev.common.service.ImportUserService;
import com.ev.common.vo.UserEntity;
import com.ev.framework.il8n.MessageSourceHandler;
import com.ev.framework.utils.MD5Utils;
import com.ev.framework.utils.R;
import com.ev.framework.utils.StringUtils;
import com.ev.system.dao.UserDao;
import com.ev.system.domain.DeptDO;
import com.ev.system.domain.UserDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ImportUserServiceImpl implements ImportUserService {
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @Autowired
    private ImportUserDao importUserDao;
    @Autowired
    private UserDao userDao;
    @Override
    public R importExcelForUser(MultipartFile file) {
        if (file.isEmpty()) {
            return R.error(messageSourceHandler.getMessage("file.nonSelect", null));
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        String[] importFields = {"姓名"};
        params.setImportFields(importFields);
        List<UserEntity> userEntityList;
        try {
            userEntityList  =  ExcelImportUtil.importExcel(file.getInputStream(), UserEntity.class, params);
            userEntityList = userEntityList
                    .stream()
                    .filter(e-> StringUtils.isNoneEmpty(e.getName()))
                    .collect(Collectors.toList());
        }catch(Exception e) {
            return R.error(messageSourceHandler.getMessage("file.upload.error", null));
        }

        List<DeptDO> deptList = importUserDao.getDeptList();
        Map<String, Long> deptMap = deptList
                .stream()
                .collect(Collectors.toMap(DeptDO::getName, DeptDO::getDeptId));

        if (userEntityList.size() > 0) {
            UserDO userDO ;
            for (UserEntity userEntity : userEntityList) {
                userDO = new UserDO();
                BeanUtils.copyProperties(userEntity,userDO);
                // 部门
                String deptName = userEntity.getDeptName();
                userDO.setDeptId(deptMap.get(deptName));

                String sex = userEntity.getSex();
                if(sex!=null){
                    if ("男".equals(sex)){
                        userDO.setSex(1L);
                    }else{
                        userDO.setSex(0L);
                    }
                }
                String password = "111111";
                userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), password));
                userDO.setStatus(1);
                userDao.save(userDO);

            }
        }
        return R.ok();
    }
}
