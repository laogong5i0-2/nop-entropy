package io.nop.search.api;

import io.nop.api.core.annotations.data.DataBean;

import java.util.List;

@DataBean
public class SearchResponse {
    private String query;
    private int limit;
    private long total;

    private List<SearchHit> items;

    private long processTime;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<SearchHit> getItems() {
        return items;
    }

    public void setItems(List<SearchHit> items) {
        this.items = items;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }
}
