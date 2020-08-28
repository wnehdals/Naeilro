package com.koreatech.naeilro.network.entity.myplan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyPlanNode {
    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("content_id")
    @Expose
    private String contendID;
    @SerializedName("node_no")
    @Expose
    private String nodeNumber;


    public MyPlanNode(String contentType, String contendID) {
        this.contentType = contentType;
        this.contendID = contendID;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContendID() {
        return contendID;
    }

    public String getNodeNumber() {
        return nodeNumber;
    }
}