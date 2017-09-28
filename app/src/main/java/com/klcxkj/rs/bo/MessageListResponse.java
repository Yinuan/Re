package com.klcxkj.rs.bo;

import java.util.List;

public class MessageListResponse extends BaseBo{
	private List<Message> obj;

	public List<Message> getObj() {
		return obj;
	}

	public void setObj(List<Message> obj) {
		this.obj = obj;
	}
}
