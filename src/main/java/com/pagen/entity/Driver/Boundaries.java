package com.pagen.entity.Driver;

/**
 * 边界类
 * 存放
 */
public class Boundaries extends BaseDriver {

    private String id;
    private String prefix;
    private String payloadTrue;
    private String payloadFalse;
    private String suffix;
    private String falsePage;

    public String getFalsePage() {
        return falsePage;
    }

    public void setFalsePage(String falsePage) {
        this.falsePage = falsePage;
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPayloadTrue() {
        return payloadTrue;
    }

    public String getPayloadFalse() {
        return payloadFalse;
    }

    public String getSuffix() {
        return suffix;
    }

    public void SetAttr(String tag, String str) {
        if (tag != null) {
            switch (tag) {
                case "id":
                    id = str;
                    break;
                case "prefix":
                    prefix = str;
                    break;
                case "payloadTrue":
                    payloadTrue = str;
                    break;
                case "payloadFalse":
                    payloadFalse = str;
                    break;
                case "suffix":
                    suffix = str;
                    break;
            }
        }
    }

    @Override
    public String getTypeDriver() {
        return "BOUNDARY";
    }
}