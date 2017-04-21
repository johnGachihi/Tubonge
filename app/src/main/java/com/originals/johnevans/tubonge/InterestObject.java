package com.originals.johnevans.tubonge;

/**
 * Created by John on 4/20/2017.
 */

public class InterestObject {

    String iconPath, interest;
    int interestId;

    public InterestObject(int interestId, String iconPath, String interest) {
        this.interestId = interestId;
        this.iconPath = iconPath;
        this.interest = interest;
    }

    public InterestObject(String iconPath, String interest) {
        this.iconPath = iconPath;
        this.interest = interest;
    }

    public int getInterestId() {
        return interestId;
    }

    public void setInterestId(int interestId) {
        this.interestId = interestId;
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
