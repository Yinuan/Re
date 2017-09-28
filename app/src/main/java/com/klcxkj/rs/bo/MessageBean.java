package com.klcxkj.rs.bo;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/8
 * email:yinjuan@klcxkj.com
 * description:消息实体类
 */

public class MessageBean {

    /**
     * RepTypeID : 1
     * msgType : 1
     * RepContent :
     * RepDatetime : 2017-09-06 20:12:51.237
     * RecID : 8
     * iconUrl :
     * RepTitle :
     * HtmlPath :
     */

    private int RepTypeID;
    private int msgType;
    private String RepContent;
    private String RepDatetime;
    private int RecID;
    private String iconUrl;
    private String RepTitle;
    private String HtmlPath;

    public void setRepTypeID(int RepTypeID) {
        this.RepTypeID = RepTypeID;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public void setRepContent(String RepContent) {
        this.RepContent = RepContent;
    }

    public void setRepDatetime(String RepDatetime) {
        this.RepDatetime = RepDatetime;
    }

    public void setRecID(int RecID) {
        this.RecID = RecID;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setRepTitle(String RepTitle) {
        this.RepTitle = RepTitle;
    }

    public void setHtmlPath(String HtmlPath) {
        this.HtmlPath = HtmlPath;
    }

    public int getRepTypeID() {
        return RepTypeID;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getRepContent() {
        return RepContent;
    }

    public String getRepDatetime() {
        return RepDatetime;
    }

    public int getRecID() {
        return RecID;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getRepTitle() {
        return RepTitle;
    }

    public String getHtmlPath() {
        return HtmlPath;
    }
}
