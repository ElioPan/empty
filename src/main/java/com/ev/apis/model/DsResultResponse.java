package com.ev.apis.model;

import lombok.Data;

import java.util.List;

@Data
public class DsResultResponse {
    private int pageno;
    private int pagesize;
    private int totalPages;
    private int totalRows;
    private String searchKey;
    private String keyword;
    private List<?> datas;

    public DsResultResponse() {
    }

    public DsResultResponse(int pageno, int pagesize, int totalRows, List<?> datas) {
        this.pageno = pageno;
        this.pagesize = pagesize;
        this.totalPages = (totalRows + pagesize - 1) / pagesize;
        this.totalRows = totalRows;
        this.datas = datas;
    }
}
