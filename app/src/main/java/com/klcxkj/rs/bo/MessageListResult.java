package com.klcxkj.rs.bo;

import java.util.List;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/8
 * email:yinjuan@klcxkj.com
 * description:消息列表
 */

public class MessageListResult {


    /**
     * msg : 查询成功
     * obj : [{"RepTypeID":1,"msgType":1,"RepContent":"","RepDatetime":"2017-09-06 20:12:51.237","RecID":8,"iconUrl":"","RepTitle":"","HtmlPath":""}]
     * success : true
     */
    private String msg;
    private List<MessageBean> obj;
    private String success;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setObj(List<MessageBean> obj) {
        this.obj = obj;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public List<MessageBean> getObj() {
        return obj;
    }

    public String getSuccess() {
        return success;
    }


}
