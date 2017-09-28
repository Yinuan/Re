package com.klcxkj.rs.bo;

public class Message extends BaseBo{
	private String RecID;
	private String RepDatetime;
	private String RepTitle;
	private String RepContent;
	private String RepTypeID;

	public Message(String recID, String repDatetime, String repTitle, String repContent, String repTypeID) {
		RecID = recID;
		RepDatetime = repDatetime;
		RepTitle = repTitle;
		RepContent = repContent;
		RepTypeID = repTypeID;
	}

	public String getRepContent() {
		return RepContent;
	}
	public void setRepContent(String repContent) {
		RepContent = repContent;
	}
	public String getRecID() {
		return RecID;
	}
	public void setRecID(String recID) {
		RecID = recID;
	}
	public String getRepDatetime() {
		return RepDatetime;
	}
	public void setRepDatetime(String repDatetime) {
		RepDatetime = repDatetime;
	}
	public String getRepTitle() {
		return RepTitle;
	}
	public void setRepTitle(String repTitle) {
		RepTitle = repTitle;
	}
	public String getRepTypeID() {
		return RepTypeID;
	}
	public void setRepTypeID(String repTypeID) {
		RepTypeID = repTypeID;
	}

	
}
