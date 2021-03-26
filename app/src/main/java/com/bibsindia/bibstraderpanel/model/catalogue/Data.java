package com.bibsindia.bibstraderpanel.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {

    @SerializedName("catalogue_id")
    @Expose
    private Integer catalogueId;
    @SerializedName("catalogue_name")
    @Expose
    private String catalogueName;
    @SerializedName("catelogue_no")
    @Expose
    private String catelogueNo;
    @SerializedName("shade_type")
    @Expose
    private String shadeType;
    @SerializedName("unit_type")
    @Expose
    private String unitType;
    @SerializedName("bulk_qty")
    @Expose
    private String bulkQty;
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
    @SerializedName("sub_fabric_id")
    @Expose
    private Integer subFabricId;
    @SerializedName("sub_fabric_name")
    @Expose
    private String subFabricName;
    @SerializedName("subfabric_image")
    @Expose
    private String subfabricImage;
    @SerializedName("subfabric_thumb_image")
    @Expose
    private String subfabricThumbImage;
    @SerializedName("sale_price")
    @Expose
    private String salePrice = "";
    @SerializedName("catalogue_image")
    @Expose
    private String catalogueImage;
    @SerializedName("catalogue_thumb_image")
    @Expose
    private String catalogueThumbImage;
    @SerializedName("shade_detail")
    @Expose
    private ArrayList<ShadeDetail> shadeDetail = null;

    private int manufacturerId;

    private String manufacturerName;

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

    public String getCatelogueNo() {
        return catelogueNo;
    }

    public void setCatelogueNo(String catelogueNo) {
        this.catelogueNo = catelogueNo;
    }

    public String getShadeType() {
        return shadeType;
    }

    public void setShadeType(String shadeType) {
        this.shadeType = shadeType;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getBulkQty() {
        return bulkQty;
    }

    public void setBulkQty(String bulkQty) {
        this.bulkQty = bulkQty;
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

    public Integer getSubFabricId() {
        return subFabricId;
    }

    public void setSubFabricId(Integer subFabricId) {
        this.subFabricId = subFabricId;
    }

    public String getSubFabricName() {
        return subFabricName;
    }

    public void setSubFabricName(String subFabricName) {
        this.subFabricName = subFabricName;
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

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
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

    public ArrayList<ShadeDetail> getShadeDetail() {
        return shadeDetail;
    }

    public void setShadeDetail(ArrayList<ShadeDetail> shadeDetail) {
        this.shadeDetail = shadeDetail;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
}
