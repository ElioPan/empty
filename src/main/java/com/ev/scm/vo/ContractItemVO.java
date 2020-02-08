package com.ev.scm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ContractItemVO {
    private Long id;

    private Long materielId;

    private String type;

    private String countBefore;

    private String countAfter;

    private String taxAmountBefore;

    private String taxAmountAfter;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"materielId\":")
                .append(materielId);
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"countBefore\":\"")
                .append(countBefore).append('\"');
        sb.append(",\"countAfter\":\"")
                .append(countAfter).append('\"');
        sb.append(",\"taxAmountBefore\":\"")
                .append(taxAmountBefore).append('\"');
        sb.append(",\"taxAmountAfter\":\"")
                .append(taxAmountAfter).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
