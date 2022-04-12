package org.BuyNothingProject.messages;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ConstraintViolationMessage {
	private static String type = "http://localhost:8080/bn/api/problems/data-validation";
	private static String title = "Your request data didn't pass validation";
	private String detail;
	private int status;
	private String instance;

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static ConstraintViolationMessage getViolationBody(Exception e) {
		ConstraintViolationException c = (ConstraintViolationException) e.getCause().getCause();
		ConstraintViolation voilation = (ConstraintViolation) c.getConstraintViolations().toArray()[0];
		ConstraintViolationMessage msg = new ConstraintViolationMessage();
		msg.setDetail(voilation.getMessage());
		msg.setTitle(title);
		msg.setType(type);
		msg.setStatus(400);
		return msg;
	}
}
