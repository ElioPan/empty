package com.ev.system.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户主键
    private Long userId;
    // 用户名
    private String username;
    // 用户真实姓名
    private String name;
    // 密码
    private String password;
    // 部门
    private Long deptId;
    private String deptName;
    // 邮箱
    private String email;
    // 手机号
    private String mobile;
    // 状态 0:禁用，1:正常
    private Integer status;
    // 创建用户id
    private Long createBy;
    // 创建时间
    private Date createTime;
    // 修改人
    private Long updateBy;
    // 修改时间
    private Date updateTime;
    // 删除状态
    private Integer delFlag;
    //角色
    private List<Long> roleIds;
    //用户组
    private List<Long> groupIds;
    //性别
    private Long sex;
    //出身日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    //图片ID
    private Long picId;
    //现居住地
    private String liveAddress;
    //爱好
    private String hobby;
    //省份
    private String province;
    //所在城市
    private String city;
    //所在地区
    private String district;
    //用户组显示str
    private String groupstr;
    // 状态 0:否，1:是
    private Integer isapp = 0;
    // 是否首次登录 0:是，1:否
    private Integer isfirst = 0;

    private Long providerId;
    // 用户数据权限
    private Long dataPermission;
    // 角色数据权限自定义的部门ID
    private List<Long> deptDatas;

    @Override
    public String toString() {
        return "UserDO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", roleIds=" + roleIds +
                ", groupIds=" + groupIds +
                ", sex=" + sex +
                ", birth=" + birth +
                ", picId=" + picId +
                ", liveAddress='" + liveAddress + '\'' +
                ", hobby='" + hobby + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", groupstr='" + groupstr + '\'' +
                ", isapp='" + isapp + '\'' +
                ", isfirst='" + isfirst + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }

}
