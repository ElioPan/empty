package com.ev.mes.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeedingAlterationVO  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long materielId;

    private String countBefore="";

    private String countAfter="";

    private String facilityBefore="";

    private String facilityAfter="";

    private String locationBefore="";

    private String locationAfter="";

    private String processBefore="";

    private String processAfter="";

    private String stationBefore="";

    private String stationAfter="";

    private String isCollectBefore="";

    private String isCollectAfter="";


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"materielId\":")
                .append(materielId);
        sb.append(",\"countBefore\":\"")
                .append(countBefore).append('\"');
        sb.append(",\"countAfter\":\"")
                .append(countAfter).append('\"');
        sb.append(",\"facilityBefore\":\"")
                .append(facilityBefore).append('\"');
        sb.append(",\"facilityAfter\":\"")
                .append(facilityAfter).append('\"');
        sb.append(",\"locationBefore\":\"")
                .append(locationBefore).append('\"');
        sb.append(",\"locationAfter\":\"")
                .append(locationAfter).append('\"');
        sb.append(",\"processBefore\":\"")
                .append(processBefore).append('\"');
        sb.append(",\"processAfter\":\"")
                .append(processAfter).append('\"');
        sb.append(",\"stationBefore\":\"")
                .append(stationBefore).append('\"');
        sb.append(",\"stationAfter\":\"")
                .append(stationAfter).append('\"');
        sb.append(",\"isCollectBefore\":\"")
                .append(isCollectBefore).append('\"');
        sb.append(",\"isCollectAfter\":\"")
                .append(isCollectAfter).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
