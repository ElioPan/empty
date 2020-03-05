package com.ev.scm.vo;

import lombok.Data;

@Data
public class ContractVO {
    private Long id;

    private String type;

    private String discountRateBefore;

    private String discountRateAfter;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"discountRateBefore\":\"")
                .append(discountRateBefore).append('\"');
        sb.append(",\"discountRateAfter\":\"")
                .append(discountRateAfter).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
