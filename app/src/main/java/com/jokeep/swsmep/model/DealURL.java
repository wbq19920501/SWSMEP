package com.jokeep.swsmep.model;

import java.net.URLDecoder;


public class DealURL {
	@Override
	public String toString() {
		return "BillData [isSkip=" + isSkip + ", url=" + url + ", text=" + text
				+ "]";
	}

	private String isSkip;
	private String url;
	private String text;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsSkip() {
		return isSkip;
	}

	public void setIsSkip(String isSkip) {
		this.isSkip = isSkip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setData(String key, String value) throws Exception {
		if (key.equals("isskip")) {
			setIsSkip(value);
		} else if (key.equals("text")) {
			value = URLDecoder.decode(value, "UTF-8");
			setText(value);
		} else if (key.equals("url")) {
			setUrl(value);
		} else if (key.equals("id")) {
			setId(value);
		}

	}
}