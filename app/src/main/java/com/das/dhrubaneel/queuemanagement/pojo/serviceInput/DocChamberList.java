package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class DocChamberList {
    private String service;
    private String method;
    private DocChamberListData data;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public DocChamberListData getData() {
        return data;
    }

    public void setData(DocChamberListData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DocChamberList{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
