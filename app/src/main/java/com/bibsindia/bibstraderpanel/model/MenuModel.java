package com.bibsindia.bibstraderpanel.model;

public class MenuModel {

    public String menuName;
    public int menuIcon;
    public boolean hasChildren = false;
    public boolean isGroup;
    public int manufacturerId;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, int menuIcon) {
        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.menuIcon = menuIcon;
    }

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren) {
        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.menuIcon = 0;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }
}
