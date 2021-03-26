package com.bibsindia.bibstraderpanel.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("acknowledge")
    @Expose
    private String acknowledge;
    @SerializedName("order_date")
    @Expose
    private String orderDate;

    @SerializedName("manufacturer_id")
    @Expose
    private int manufacturer_id;

    @SerializedName("manufacturer_name")
    @Expose
    private String manufacturer_name;

    @SerializedName("manufacturer_company_name")
    @Expose
    private String manufacturer_company_name;

    @SerializedName("catalogue_id")
    @Expose
    private Integer catalogueId;
    @SerializedName("catalogue_name")
    @Expose
    private String catalogueName;
    @SerializedName("catalogue_no")
    @Expose
    private String catalogueNo;

    @SerializedName("size")
    @Expose
    private String size;

    @SerializedName("catalogue_image")
    @Expose
    private String catalogueImage;
    @SerializedName("catalogue_thumb_image")
    @Expose
    private String catalogueThumbImage;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_thumb_image")
    @Expose
    private String productThumbImage;
    @SerializedName("fabric_id")
    @Expose
    private Integer fabricId;
    @SerializedName("fabric_name")
    @Expose
    private String fabricName;
    @SerializedName("fabric_image")
    @Expose
    private String fabricImage;
    @SerializedName("fabric_thumb_image")
    @Expose
    private String fabricThumbImage;
    @SerializedName("subfabric_id")
    @Expose
    private Integer subfabricId;
    @SerializedName("subfabric_name")
    @Expose
    private String subfabricName;
    @SerializedName("subfabric_image")
    @Expose
    private String subfabricImage;
    @SerializedName("subfabric_thumb_image")
    @Expose
    private String subfabricThumbImage;
    @SerializedName("total_quantity")
    @Expose
    private String totalQuantity;
    @SerializedName("shade_id")
    @Expose
    private String shadeId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(String acknowledge) {
        this.acknowledge = acknowledge;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

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

    public String getCatalogueImage() {
        return catalogueImage;
    }

    public void setCatalogueImage(String catalogueImage) {
        this.catalogueImage = catalogueImage;
    }

    public String getCatalogueThumbImage() {
        return catalogueThumbImage;
    }

    public void setCatalogueThumbImage(String catalogueThumbImage) {
        this.catalogueThumbImage = catalogueThumbImage;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductThumbImage() {
        return productThumbImage;
    }

    public void setProductThumbImage(String productThumbImage) {
        this.productThumbImage = productThumbImage;
    }

    public Integer getFabricId() {
        return fabricId;
    }

    public void setFabricId(Integer fabricId) {
        this.fabricId = fabricId;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getFabricImage() {
        return fabricImage;
    }

    public void setFabricImage(String fabricImage) {
        this.fabricImage = fabricImage;
    }

    public String getFabricThumbImage() {
        return fabricThumbImage;
    }

    public void setFabricThumbImage(String fabricThumbImage) {
        this.fabricThumbImage = fabricThumbImage;
    }

    public Integer getSubfabricId() {
        return subfabricId;
    }

    public void setSubfabricId(Integer subfabricId) {
        this.subfabricId = subfabricId;
    }

    public String getSubfabricName() {
        return subfabricName;
    }

    public void setSubfabricName(String subfabricName) {
        this.subfabricName = subfabricName;
    }

    public String getSubfabricImage() {
        return subfabricImage;
    }

    public void setSubfabricImage(String subfabricImage) {
        this.subfabricImage = subfabricImage;
    }

    public String getSubfabricThumbImage() {
        return subfabricThumbImage;
    }

    public void setSubfabricThumbImage(String subfabricThumbImage) {
        this.subfabricThumbImage = subfabricThumbImage;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getShadeId() {
        return shadeId;
    }

    public void setShadeId(String shadeId) {
        this.shadeId = shadeId;
    }

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(int manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getManufacturer_company_name() {
        return manufacturer_company_name;
    }

    public void setManufacturer_company_name(String manufacturer_company_name) {
        this.manufacturer_company_name = manufacturer_company_name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
