package com.klcxkj.rs.bo;

public class Bill extends BaseBo{
	private String FlagName;
	private String DMoney;
	private String RowID;
	private String DT;

	public Bill(String flagName, String DMoney, String rowID, String DT) {
		FlagName = flagName;
		this.DMoney = DMoney;
		RowID = rowID;
		this.DT = DT;
	}

	public String getFlagName() {
		return FlagName;
	}
	public void setFlagName(String flagName) {
		FlagName = flagName;
	}
	public String getDMoney() {
		return DMoney;
	}
	public void setDMoney(String dMoney) {
		DMoney = dMoney;
	}
	public String getRowID() {
		return RowID;
	}
	public void setRowID(String rowID) {
		RowID = rowID;
	}
	public String getDT() {
		return DT;
	}
	public void setDT(String dT) {
		DT = dT;
	}
	
}
