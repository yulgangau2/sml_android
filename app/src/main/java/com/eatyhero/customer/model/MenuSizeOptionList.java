package com.eatyhero.customer.model;

/**
 * Created by user on 10/1/2015.
 */
public class MenuSizeOptionList {


    String menuSizeName;
    String menuSizePrice;
    String menuSizeId;
    boolean checked;

    public MenuSizeOptionList(String menusizeName, String menusizePrice,String menuSizeId, boolean checked) {

        this.menuSizeName=menusizeName;
        this.menuSizePrice=menusizePrice;
        this.menuSizeId=menuSizeId;
        this.checked=checked;
    }

    public String getMenuSizeName() {
        return menuSizeName;
    }

    public void setMenuSizeName(String menuSizeName) {
        this.menuSizeName = menuSizeName;
    }

    public String getMenuSizePrice() {
        return menuSizePrice;
    }

    public void setMenuSizePrice(String menuSizePrice) {
        this.menuSizePrice = menuSizePrice;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getMenuSizeId() {
        return menuSizeId;
    }

    public void setMenuSizeId(String menuSizeId) {
        this.menuSizeId = menuSizeId;
    }
}
