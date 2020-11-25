package com.pagen.entity.Driver;

public class BoolBlindDriver extends BaseDriver {

    private String title;

    private String isTime;
    private String payload;
    private String checkway;
    private String type;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsTime() {
        return isTime;
    }

    public void setIsTime(String isTime) {
        this.isTime = isTime;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getCheckway() {
        return checkway;
    }

    public void setCheckway(String checkway) {
        this.checkway = checkway;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public void SetAttr(String tag, String str) {
        if (tag != null) {
            switch (tag) {
                case "title":
                    title = str;
                    break;
                case "isTime":
                    isTime = str;
                    break;
                case "payload":
                    payload = str;
                    break;
                case "checkway":
                    checkway = str;
                    break;
                case "type":
                    type = str;
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "BoolBlindDriver{" +
                "title='" + title + '\'' +
                ", isTime='" + isTime + '\'' +
                ", payload='" + payload + '\'' +
                ", checkway='" + checkway + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public String getTypeDriver() {
        return "Bool_Blind";
    }
}
