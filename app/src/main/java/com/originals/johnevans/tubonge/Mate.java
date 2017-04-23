package com.originals.johnevans.tubonge;

/**
 * Created by John on 4/23/2017.
 */

public class Mate {

    private String username;
    private String icon_path;
    private int similar_interest;

    public Mate(String username, String icon_path, int similar_interest) {
        this.username = username;
        this.icon_path = icon_path;
        this.similar_interest = similar_interest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public int getSimilar_interest() {
        return similar_interest;
    }

    public void setSimilar_interest(int similar_interest) {
        this.similar_interest = similar_interest;
    }
}
