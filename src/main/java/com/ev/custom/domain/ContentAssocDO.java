package com.ev.custom.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 附件关联表
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-09-02 17:17:17
 */
@Data
@ApiModel(value = "附件关联表")
public class ContentAssocDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	@ApiModelProperty(value = "主键")
	private Long id;
	//关联属性ID
	@ApiModelProperty(value = "关联属性ID")
	private Integer assocId;
	//关联类型
	@ApiModelProperty(value = "关联类型")
	private String assocType;
	//名称
	@ApiModelProperty(value = "名称")
	private String fileName;
	//路径
	@ApiModelProperty(value = "路径")
	private String filePath;
	//租户ID
	@ApiModelProperty(value = "租户ID")
	private Integer providerId;
	//创建人
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	//创建人
	@ApiModelProperty(value = "修改人")
	private Long updateBy;
	//创建时间
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;
	//删除标志
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;

	public ContentAssocDO(Integer assocId, String assocType, String filePath) {
		this.assocId = assocId;
		this.assocType = assocType;
		this.filePath = filePath;
	}

	public ContentAssocDO(Integer assocId, String assocType, String fileName, String filePath) {
		this.assocId = assocId;
		this.assocType = assocType;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public ContentAssocDO() {
	}
}
