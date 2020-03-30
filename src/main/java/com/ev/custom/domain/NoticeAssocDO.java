package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 
 * 
 * @author ZhangDong
 * @email 911435330@qq.com
 * @date 2020-03-27 15:30:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "")
public class NoticeAssocDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//消息主键
    @ApiModelProperty(value = "消息主键")
	private Long noticeId;
	//用户
    @ApiModelProperty(value = "用户")
	private Long userId;
	//用户名
    @ApiModelProperty(value = "用户名")
	private String userName;
	//用户类型（0：接收人；1：抄送人）
    @ApiModelProperty(value = "用户类型（0：接收人；1：抄送人）")
	private String assocType;
	//状态（0：未读，1：已读）
    @ApiModelProperty(value = "状态（0：未读，1：已读）")
	private Integer signStatus;

	public NoticeAssocDO(Long noticeId, Long userId, String userName, String assocType, Integer signStatus) {
		this.noticeId = noticeId;
		this.userId = userId;
		this.userName = userName;
		this.assocType = assocType;
		this.signStatus = signStatus;
	}
}
