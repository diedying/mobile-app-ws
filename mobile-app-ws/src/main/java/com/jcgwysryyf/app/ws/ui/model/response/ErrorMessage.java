package com.jcgwysryyf.app.ws.ui.model.response;

import java.util.Date;

public class ErrorMessage {
	private Date timestamp;
	private String message;
	
	public ErrorMessage() {}
	
	public ErrorMessage(Date date, String message)
	{
		this.timestamp = date;
		this.message = message;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
