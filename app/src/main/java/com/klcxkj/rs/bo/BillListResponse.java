package com.klcxkj.rs.bo;

import java.util.List;

public class BillListResponse extends BaseBo{
	private String AllPage;
	private List<Bill> obj;

	public List<Bill> getObj() {
		return obj;
	}

	public void setObj(List<Bill> obj) {
		this.obj = obj;
	}

	public String getAllPage() {
		return AllPage;
	}

	public void setAllPage(String allPage) {
		AllPage = allPage;
	}
	
	
}
