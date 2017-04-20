package com.originals.johnevans.tubonge;

/**
 * Created by John on 4/20/2017.
 */

public class InterestObject {

    String iconPath, interest;

    public InterestObject(String iconPath, String interest) {
        this.iconPath = iconPath;
        this.interest = interest;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getInterest() {
        return interest;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
