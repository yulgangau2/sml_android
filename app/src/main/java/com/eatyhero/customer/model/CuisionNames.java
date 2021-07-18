package com.eatyhero.customer.model;

/**
 * Created by admin on 09-01-2017.
 */
public class CuisionNames {


    String Name;
    private boolean selected;

    public CuisionNames(String name, boolean selected) {
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
