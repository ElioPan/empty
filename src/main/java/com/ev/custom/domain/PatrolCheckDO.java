package com.ev.custom.domain;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * 巡检验收表
 * 
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2019-07-30 16:39:24
 */
@ApiModel(value = "巡检验收表")
public class PatrolCheckDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
    @ApiModelProperty(value = "")
	private Long id;
	//验收人
    @ApiModelProperty(value = "验收人",hidden = true)
	private Long userId;
	//收验结果
    @ApiModelProperty(value = "收验结果(115验收通过；116验收不通过)")
	private Integer result;//115验收通过；116验收不通过
	//内容
    @ApiModelProperty(value = "内容")
	private String content;
	//验收时间
    @ApiModelProperty(value = "验收时间")
	private Date checkTime;
	//记录ID
    @ApiModelProperty(value = "记录ID",hidden = true)
	private Long recordId;
	//效率评分
    @ApiModelProperty(value = "效率评分")
	private Integer xlGrade;
	//质量评分
    @ApiModelProperty(value = "质量评分")
	private Integer qaGrade;
	//态度评分
    @ApiModelProperty(value = "态度评分")
	private Integer tdGrade;
    //创建人
    @ApiModelProperty(value = "创建人",hidden = true)
	private Long createBy;
	//创建时间
    @ApiModelProperty(value = "创建时间",hidden = true)
	private Date createTime;
	//修改人
    @ApiModelProperty(value = "修改人",hidden = true)
	private Long updateBy;
	//修改时间
    @ApiModelProperty(value = "修改时间",hidden = true)
	private Date updateTime;
	//删除状态
    @ApiModelProperty(value = "删除状态",hidden = true)
	private Integer delFlag;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：验收人
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：验收人
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：收验结果
	 */
	public void setResult(Integer result) {
		this.result = result;
	}
	/**
	 * 获取：收验结果
	 */
	public Integer getResult() {
		return result;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：验收时间
	 */
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	/**
	 * 获取：验收时间
	 */
	public Date getCheckTime() {
		return checkTime;
	}
	/**
	 * 设置：记录ID
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：记录ID
	 */
	public Long getRecordId() {
		return recordId;
	}
	/**
	 * 设置：效率评分
	 */
	public void setXlGrade(Integer xlGrade) {
		this.xlGrade = xlGrade;
	}
	/**
	 * 获取：效率评分
	 */
	public Integer getXlGrade() {
		return xlGrade;
	}
	/**
	 * 设置：质量评分
	 */
	public void setQaGrade(Integer qaGrade) {
		this.qaGrade = qaGrade;
	}
	/**
	 * 获取：质量评分
	 */
	public Integer getQaGrade() {
		return qaGrade;
	}
	/**
	 * 设置：态度评分
	 */
	public void setTdGrade(Integer tdGrade) {
		this.tdGrade = tdGrade;
	}
	/**
	 * 获取：态度评分
	 */
	public Integer getTdGrade() {
		return tdGrade;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改人
	 */
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：修改人
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：删除状态
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
}
