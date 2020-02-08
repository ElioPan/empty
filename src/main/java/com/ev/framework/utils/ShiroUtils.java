package com.ev.framework.utils;

import com.ev.framework.config.Constant;
import com.ev.system.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class ShiroUtils {
    @Autowired
    private static SessionDAO sessionDAO;

    public static Subject getSubjct() {
        try{
            return SecurityUtils.getSubject();
        }catch (Exception ex){
            return null;
        }
    }
    public static UserDO getUser() {
        Object object = getSubjct()==null?null:getSubjct().getPrincipal();
        return (UserDO) object;
    }

    public static Long getUserId() {
        Long userId =  getUser()==null?null:getUser().getUserId();
        return userId;
    }

    public static UserDO getUserDataPermission() {
        UserDO user = getUser();
        if (user != null) {
            Integer dataPermission = user.getDataPermission();
            dataPermission = dataPermission ==null? 0:dataPermission;
            switch (dataPermission){
                case Constant.ALL_DATA:
                    user.setDeptDatas(null);
                    user.setDeptId(null);
                    user.setUserId(null);
                    break;
                case Constant.SUBORDINATE_DEPT_DATA :
                case Constant.THIS_DEPT_DATA:
                    user.setDeptDatas(null);
                    user.setUserId(null);
                    break;
                case Constant.CUSTOM_DATA:
                    user.setDeptId(null);
                    user.setUserId(null);
                    break;
                default:
                    user.setDeptDatas(null);
                    user.setDeptId(null);
                    break;
            }
        }
        return user;
    }

    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
    
    public static boolean isUser(Long userId) {
        return Objects.equals(getUserId(), userId);
    }
}
