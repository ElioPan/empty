package com.ev.scm.vo;

import lombok.Data;

@Data
public class ContractPayVO {
    private Long id;

    private String type;

    private String receivableDateAfter;

    private String receivableDateBefore;

    private String receivableAmountAfter;

    private String receivableAmountBefore;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"type\":\"")
                .append(type).append('\"');
        sb.append(",\"receivableDateAfter\":\"")
                .append(receivableDateAfter).append('\"');
        sb.append(",\"receivableDateBefore\":\"")
                .append(receivableDateBefore).append('\"');
        sb.append(",\"receivableAmountAfter\":\"")
                .append(receivableAmountAfter).append('\"');
        sb.append(",\"receivableAmountBefore\":\"")
                .append(receivableAmountBefore).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
