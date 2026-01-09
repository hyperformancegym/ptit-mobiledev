package com.ptithcm.lexigo.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReadingPassagesResponse {
    @SerializedName("items")
    private List<ReadingPassage> items;
    
    @SerializedName("total")
    private int total;
    
    @SerializedName("page")
    private int page;
    
    @SerializedName("page_size")
    private int pageSize;

    public List<ReadingPassage> getItems() {
        return items;
    }

    public void setItems(List<ReadingPassage> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
