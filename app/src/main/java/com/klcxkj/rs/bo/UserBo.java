package com.klcxkj.rs.bo;

import android.util.Log;

public class UserBo extends BaseBo{
//	{"CreditsAccount":"","EmployeeID":0,"LastLogInDT":"2015-05-21 16:59:14 ","PrjID":1,"PrjRecID":1,"ServerID":1,
//		"ServerIP":"192.168.1.249","ServerPort":8080,"SysName":"测试用户","SystemID":1,"TelPhone":1365565566,
//		"msg":"登录成功","success":"true"}
	private String CreditsAccount;
	private String EmployeeID;
	private String LastLogInDT;
	private String PrjID;
	private String PrjRecID;
	private String ServerID;
	private String ServerIP;
	private String ServerPort;
	private String SysName;
	private String SystemID;
	private String TelPhone;
	private String LogPwd;
	
	public String getLogPwd() {
		return LogPwd;
	}
	public void setLogPwd(String logPwd) {
		LogPwd = logPwd;
	}
	public String getCreditsAccount() {
		return CreditsAccount;
	}
	public void setCreditsAccount(String creditsAccount) {
		CreditsAccount = creditsAccount;
	}
	public String getEmployeeID() {
		Log.e("water", "getEmployeeID = " + EmployeeID);
		return EmployeeID;
	}
	public void setEmployeeID(String employeeID) {
		Log.e("water", "setEmployeeID = " + employeeID);
		EmployeeID = employeeID;
	}
	public String getLastLogInDT() {
		return LastLogInDT;
	}
	public void setLastLogInDT(String lastLogInDT) {
		LastLogInDT = lastLogInDT;
	}
	public String getPrjID() {
		return PrjID;
	}
	public void setPrjID(String prjID) {
		PrjID = prjID;
	}
	public String getPrjRecID() {
		return PrjRecID;
	}
	public void setPrjRecID(String prjRecID) {
		PrjRecID = prjRecID;
	}
	public String getServerID() {
		return ServerID;
	}
	public void setServerID(String serverID) {
		ServerID = serverID;
	}
	public String getServerIP() {
		return ServerIP;
	}
	public void setServerIP(String serverIP) {
		ServerIP = serverIP;
	}
	public String getServerPort() {
		return ServerPort;
	}
	public void setServerPort(String serverPort) {
		ServerPort = serverPort;
	}
	public String getSysName() {
		return SysName;
	}
	public void setSysName(String sysName) {
		SysName = sysName;
	}
	public String getSystemID() {
		return SystemID;
	}
	public void setSystemID(String systemID) {
		SystemID = systemID;
	}
	public String getTelPhone() {
		return TelPhone;
	}
	public void setTelPhone(String telPhone) {
		TelPhone = telPhone;
	}
	
}
