package com.nangua.xiaomanjflc.bean;

import com.nangua.xiaomanjflc.AppConstants;

public class MainAD {
	
	private String version;
	
	private String link;
	
	private String img;
	
	private int linkType = AppConstants.MainADType.GOODS.getCode();
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

}
