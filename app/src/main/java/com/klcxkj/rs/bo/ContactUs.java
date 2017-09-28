package com.klcxkj.rs.bo;

/**
 * autor:OFFICE-ADMIN
 * time:2017/9/4
 * email:yinjuan@klcxkj.com
 * description:
 */

public class ContactUs {

    /**
     * msg : 查询成功
     * versionContent : http://192.168.1.250:8282/RoadStuAppS/mainNews/version.html
     * dutyContent : http://192.168.1.250:8282/RoadStuAppS/mainNews/duty.html
     * success : true
     * TelPhone : 13866666666
     */
    private String msg;
    private String versionContent;  //版权声明
    private String dutyContent;  //免责申明
    private String success;
    private String TelPhone;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }

    public void setDutyContent(String dutyContent) {
        this.dutyContent = dutyContent;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setTelPhone(String TelPhone) {
        this.TelPhone = TelPhone;
    }

    public String getMsg() {
        return msg;
    }

    public String getVersionContent() {
        return versionContent;
    }

    public String getDutyContent() {
        return dutyContent;
    }

    public String getSuccess() {
        return success;
    }

    public String getTelPhone() {
        return TelPhone;
    }
}
