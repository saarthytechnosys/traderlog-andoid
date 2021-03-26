package com.bibsindia.bibstraderpanel.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlCount {
    @SerializedName("catalogue_id")
    @Expose
    private Integer catalogueId;
    @SerializedName("catalogue_name")
    @Expose
    private String catalogueName;
    @SerializedName("catalogue_no")
    @Expose
    private String catalogueNo;
    @SerializedName("share_url_count")
    @Expose
    private Integer shareUrlCount;
    @SerializedName("open_url_count")
    @Expose
    private Integer openUrlCount;
    @SerializedName("pdf_url_count")
    @Expose
    private Integer pdfUrlCount;

    public Integer getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(Integer catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String getCatalogueName() {
        return catalogueName;
    }

    public void setCatalogueName(String catalogueName) {
        this.catalogueName = catalogueName;
    }

    public String getCatalogueNo() {
        return catalogueNo;
    }

    public void setCatalogueNo(String catalogueNo) {
        this.catalogueNo = catalogueNo;
    }

    public Integer getShareUrlCount() {
        return shareUrlCount;
    }

    public void setShareUrlCount(Integer shareUrlCount) {
        this.shareUrlCount = shareUrlCount;
    }

    public Integer getOpenUrlCount() {
        return openUrlCount;
    }

    public void setOpenUrlCount(Integer openUrlCount) {
        this.openUrlCount = openUrlCount;
    }

    public Integer getPdfUrlCount() {
        return pdfUrlCount;
    }

    public void setPdfUrlCount(Integer pdfUrlCount) {
        this.pdfUrlCount = pdfUrlCount;
    }

}
