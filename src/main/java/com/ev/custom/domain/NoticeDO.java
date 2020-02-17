package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  * 通知单
 *  *
 *  * @author ev-monitor
 *  * @email 286600136@qq.com
 *  * @date 2020-02-11 17:00:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通知单")
public class NoticeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	//主键
    @ApiModelProperty(value = "主键")
	private Long id;
	//消息类型
    @ApiModelProperty(value = "消息类型")
	private Long type;
	//标题
    @ApiModelProperty(value = "标题")
	private String title;
	//消息内容
    @ApiModelProperty(value = "消息内容")
	private String content;
	//消息详情详
    @ApiModelProperty(value = "消息情")
	private String contentDetail;
	//发送人
    @ApiModelProperty(value = "发送人")
	private Long fromUserId;
	//接收人
    @ApiModelProperty(value = "接收人")
	private Long toUserId;
	//过期时间
    @ApiModelProperty(value = "过期时间")
	private Date expireDate;
	//1:已读；2:未读
    @ApiModelProperty(value = "1:已读；2:未读")
	private Integer signStatus;
	//创建人
    @ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人")
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态")
	private Integer delFlag;

	public NoticeDO(Long type, String title, String content,String contentDetail, Long fromUserId, Long toUserId) {
		this.type = type;
		this.title = title;
		this.content = content;
		this.contentDetail = contentDetail;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
	}
}
