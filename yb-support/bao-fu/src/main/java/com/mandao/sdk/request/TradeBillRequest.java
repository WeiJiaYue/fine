package com.mandao.sdk.request;


import com.mandao.sdk.Dictionary;
import com.mandao.sdk.RequiredParam;

/**
 * 对账文件下载请求
 */
public class TradeBillRequest extends Request {


    @RequiredParam
    private String fileDate;

    @RequiredParam
    private Dictionary.TradeBillType fileType;

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public Dictionary.TradeBillType getFileType() {
        return fileType;
    }

    public void setFileType(Dictionary.TradeBillType fileType) {
        this.fileType = fileType;
    }
}
