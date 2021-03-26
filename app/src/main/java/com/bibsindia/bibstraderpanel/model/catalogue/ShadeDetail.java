package com.bibsindia.bibstraderpanel.model.catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShadeDetail implements Serializable {


    @SerializedName("shade_id")
    @Expose
    private Integer shadeId;
    @SerializedName("shade_no")
    @Expose
    private String shadeNo="";
    @SerializedName("shade_group_id")
    @Expose
    private Integer shadeGroupId;
    @SerializedName("shade_group_color")
    @Expose
    private String shadeGroupColor;
    @SerializedName("color_code")
    @Expose
    private String colorCode;

    @SerializedName("shade_name")
    @Expose
    private String shadeName="";

    @SerializedName("fabric_id")
    @Expose
    private Integer fabricId;
    @SerializedName("fabric_name")
    @Expose
    private String fabricName;
    @SerializedName("shade_image")
    @Expose
    private String shadeImage;
    @SerializedName("shade_thumb_image")
    @Expose
    private String shadeThumbImage;

    private boolean isChecked = false;
    private int totalQty = 0;

    public Integer getShadeId() {
        return shadeId;
    }

    public void setShadeId(Integer shadeId) {
        this.shadeId = shadeId;
    }

    public String getShadeNo() {
        return shadeNo;
    }

    public void setShadeNo(String shadeNo) {
        this.shadeNo = shadeNo;
    }

    public Integer getShadeGroupId() {
        return shadeGroupId;
    }

    public void setShadeGroupId(Integer shadeGroupId) {
        this.shadeGroupId = shadeGroupId;
    }

    public String getShadeGroupColor() {
        return shadeGroupColor;
    }

    public void setShadeGroupColor(String shadeGroupColor) {
        this.shadeGroupColor = shadeGroupColor;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getShadeName() {
        return shadeName;
    }

    public void setShadeName(String shadeName) {
        this.shadeName = shadeName;
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

    public String getShadeImage() {
        return shadeImage;
    }

    public void setShadeImage(String shadeImage) {
        this.shadeImage = shadeImage;
    }

    public String getShadeThumbImage() {
        return shadeThumbImage;
    }

    public void setShadeThumbImage(String shadeThumbImage) {
        this.shadeThumbImage = shadeThumbImage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }
}
