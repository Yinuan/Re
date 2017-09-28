package com.klcxkj.rs.bean;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/22
 * email:yinjuan@klcxkj.com
 * description:红点点
 */

public class RedPointBean {

    /**
     * msg : 成功
     * msgType :
     * redPoint : 0
     * RepContent :
     * success : true
     * RepDatetime :
     * RepTitle :
     */
    private String msg;
    private String msgType;
    private int redPoint;
    private String RepContent;
    private String success;
    private String RepDatetime;
    private String RepTitle;
    private String HtmlPath;

    public String getHtmlPath() {
        return HtmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        HtmlPath = htmlPath;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setRedPoint(int redPoint) {
        this.redPoint = redPoint;
    }

    public void setRepContent(String RepContent) {
        this.RepContent = RepContent;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setRepDatetime(String RepDatetime) {
        this.RepDatetime = RepDatetime;
    }

    public void setRepTitle(String RepTitle) {
        this.RepTitle = RepTitle;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public int getRedPoint() {
        return redPoint;
    }

    public String getRepContent() {
        return RepContent;
    }

    public String getSuccess() {
        return success;
    }

    public String getRepDatetime() {
        return RepDatetime;
    }

    public String getRepTitle() {
        return RepTitle;
    }
}
