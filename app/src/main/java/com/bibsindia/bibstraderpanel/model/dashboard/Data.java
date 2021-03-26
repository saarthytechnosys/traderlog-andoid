package com.bibsindia.bibstraderpanel.model.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("total_catalogue_count")
    @Expose
    private Integer totalCatalogueCount;
    @SerializedName("last_sevenday_catalogue_count")
    @Expose
    private Integer lastSevendayCatalogueCount;
    @SerializedName("monthly_catalogue_count")
    @Expose
    private Integer monthlyCatalogueCount;
    @SerializedName("url_count")
    @Expose
    private List<UrlCount> urlCount = null;
    @SerializedName("total_order_count")
    @Expose
    private Integer totalOrderCount;
    @SerializedName("approved_order_count")
    @Expose
    private Integer approvedOrderCount;
    @SerializedName("pending_order_count")
    @Expose
    private Integer pendingOrderCount;
    @SerializedName("today_order_count")
    @Expose
    private Integer todayOrderCount;

    public Integer getTotalCatalogueCount() {
        return totalCatalogueCount;
    }

    public void setTotalCatalogueCount(Integer totalCatalogueCount) {
        this.totalCatalogueCount = totalCatalogueCount;
    }

    public Integer getLastSevendayCatalogueCount() {
        return lastSevendayCatalogueCount;
    }

    public void setLastSevendayCatalogueCount(Integer lastSevendayCatalogueCount) {
        this.lastSevendayCatalogueCount = lastSevendayCatalogueCount;
    }

    public Integer getMonthlyCatalogueCount() {
        return monthlyCatalogueCount;
    }

    public void setMonthlyCatalogueCount(Integer monthlyCatalogueCount) {
        this.monthlyCatalogueCount = monthlyCatalogueCount;
    }

    public List<UrlCount> getUrlCount() {
        return urlCount;
    }

    public void setUrlCount(List<UrlCount> urlCount) {
        this.urlCount = urlCount;
    }

    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public Integer getApprovedOrderCount() {
        return approvedOrderCount;
    }

    public void setApprovedOrderCount(Integer approvedOrderCount) {
        this.approvedOrderCount = approvedOrderCount;
    }

    public Integer getPendingOrderCount() {
        return pendingOrderCount;
    }

    public void setPendingOrderCount(Integer pendingOrderCount) {
        this.pendingOrderCount = pendingOrderCount;
    }

    public Integer getTodayOrderCount() {
        return todayOrderCount;
    }

    public void setTodayOrderCount(Integer todayOrderCount) {
        this.todayOrderCount = todayOrderCount;

    }
}
