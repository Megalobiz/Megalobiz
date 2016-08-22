package com.megalobiz.megalobiz.utils;

/**
 * Created by KeitelRobespierre on 8/22/2016.
 */
public class HamburgerItem {
    private String title;
    private int icon;

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public HamburgerItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
}
