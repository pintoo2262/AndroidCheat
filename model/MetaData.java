package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 2/11/17.
 */

public class MetaData {
    @SerializedName("recordPerPage")
    String recordPerPage;
    @SerializedName("tableTotalRows")
    String tableTotalRows;
    @SerializedName("startRow")
    String startRow;
    @SerializedName("endRow")
    String endRow;
    @SerializedName("currentPage")
    String currentPage;
    @SerializedName("totalPages")
    String totalPages;

    public String getRecordPerPage() {
        return recordPerPage;
    }

    public void setRecordPerPage(String recordPerPage) {
        this.recordPerPage = recordPerPage;
    }

    public String getTableTotalRows() {
        return tableTotalRows;
    }

    public void setTableTotalRows(String tableTotalRows) {
        this.tableTotalRows = tableTotalRows;
    }

    public String getStartRow() {
        return startRow;
    }

    public void setStartRow(String startRow) {
        this.startRow = startRow;
    }

    public String getEndRow() {
        return endRow;
    }

    public void setEndRow(String endRow) {
        this.endRow = endRow;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }


}
