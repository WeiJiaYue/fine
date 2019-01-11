package com.mandao.sdk.response;

public class TradeBillResponse extends Response {


    private String fileContent;   //对账文件内容


    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }


    @Override
    public String toString() {
        return super.toString() + "TradeBillResponse{" +
                "fileContent='" + fileContent + '\'' +
                '}';
    }
}
