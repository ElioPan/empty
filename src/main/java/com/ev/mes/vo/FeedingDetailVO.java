package com.ev.mes.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FeedingDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //主表ID
    private Long headId;
    //产品ID
    private Long materielId;
    //批号
    private String batchNo;
    //计划投料数量
    private BigDecimal planFeeding;
    //已出数量
    private BigDecimal outCount;
    //报废数量
    private BigDecimal scrapCount;
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
    //发料仓库ID
    private Integer facilityId;
    private String facilityName;
    //库位ID
    private Integer locationId;
    private String locationName;
    //需求工序ID
    private Long processId;
    private String processName;
    //需求工位ID
    private Long stationId;
    private String stationName;
    // 是否用料采集
    private Integer isCollect;
    private String isCollectName;

}
