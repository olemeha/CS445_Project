package org.BuyNothingProject.web.models;

import java.util.List;

public class AskModel {
	private int uid;
	private int aid;
	private String type;
	private String description;
	private String start_date;
	private String end_date;
	private List<String> extra_zip;
	private boolean is_active;
	private String date_created;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) throws Exception {
		String[] hai = { "borrow", "gift", "help" };
		List<String> allowedValues = java.util.Arrays.asList(hai);
		if (!allowedValues.contains(type)) {
			throw new Exception("Type must be borrow, gift, help");
		}
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public List<String> getExtra_zip() {
		return extra_zip;
	}

	public void setExtra_zip(List<String> extra_zip) {
		this.extra_zip = extra_zip;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}
}
