package com.pagen.entity.Driver;

public class ErrorBaseDriver extends BaseDriver {

    private String title;
    private String payload;
    private String type;
    private String sqlversion;
    private int count;
    private int outputInPage;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOutputInPage() {
        return outputInPage;
    }

    public void setOutputInPage(int outputInPage) {
        this.outputInPage = outputInPage;
    }

    public String getTitle() {
        return title;
    }

    public String getPayload() {
        return payload;
    }

    public String getType() {
        return type;
    }

    public String getSqlversion() {
        return sqlversion;
    }

    @Override
    public void SetAttr(String tag, String str) {
        if (tag != null) {
            switch (tag) {
                case "title":
                    title = str;
                    break;
                case "payload":
                    payload = str;
                    break;
                case "type":
                    type = str;
                    break;
                case "sqlversion":
                    sqlversion = str;
                    break;
            }
        }
    }

    @Override
    public String getTypeDriver() {
        return "ERROR_BASE";
    }


}