package com.radarwin.framework.page;

/**
 * Created by josh on 15/6/20.
 */
public class Page {
    private int pageSize = 20;
    private int pageNo = 1;
    private String sortName;
    private String sortDirect;
    private int start;

    public int getStartIndex() {
        return (pageNo - 1) * pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortDirect() {
        return sortDirect;
    }

    public void setSortDirect(String sortDirect) {
        this.sortDirect = sortDirect;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
