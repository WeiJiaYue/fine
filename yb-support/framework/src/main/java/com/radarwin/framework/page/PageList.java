package com.radarwin.framework.page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 15/6/20.
 */
public class PageList<T> {
    private Page page;
    private List<T> resultList = new ArrayList<>();
    private boolean hasNext = false;
    private int totalCount;
    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return (totalCount + page.getPageSize() - 1) / page.getPageSize();
    }
}
