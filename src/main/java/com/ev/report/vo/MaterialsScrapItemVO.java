package com.ev.report.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 用料报废明细
 *
 * @author ev-monitor
 * @email 286600136@qq.com
 * @date 2020-03-19 14:36:27
 */
@Data
public class MaterialsScrapItemVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//用料报废单主键
	private Long scrapId;
	//物料id
	private Long materialId;
	//批号
	private String batch;
	//报废原因
	private String scrapReason;
	//报废数量
	private BigDecimal scrapCount;
	//备注
	private String remark;
	//创建人 
	private Long createBy;
	//创建时间
	private Date createTime;
	//修改人
	private Long updateBy;
	//修改时间
	private Date updateTime;
	//删除状态
	private Integer delFlag;
	//工序计划单明细ID
	private Long processPlanItemId;
}
