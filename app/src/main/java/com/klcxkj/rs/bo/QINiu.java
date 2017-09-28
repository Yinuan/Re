package com.klcxkj.rs.bo;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/5
 * email:yinjuan@klcxkj.com
 * description:骑牛云获取token
 */

public class QINiu {

    /**
     * msg : 获取token成功
     * success : true
     * domainName : outgf8oyn.bkt.clouddn.com/
     * token : epE7lnGAh9r_9U5KLIv0flP91SVdsS2HUr7DyOJr:F4HpPdZT0FFGkKgtHSH9h82WtWI=:eyJzY29wZSI6Im15LWZpbGUtc3BhY2UiLCJkZWFkbGluZSI6MTUwNDYwMTY5OX0=
     */
    private String msg;
    private String success;
    private String domainName;
    private String token;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public String getSuccess() {
        return success;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getToken() {
        return token;
    }
}
