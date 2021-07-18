package com.eatyhero.customer.model;

/**
 * Created by admin on 29-03-2018.
 */

public class RestaurantTypeName {
    String Name;
    private boolean selected;

    public RestaurantTypeName(String name, boolean selected) {
        Name = name;
        this.selected = selected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
