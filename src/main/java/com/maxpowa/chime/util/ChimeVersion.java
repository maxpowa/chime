package com.maxpowa.chime.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChimeVersion {
	Long id;
	String url;
	String name;
	String type;
	String version;
	Long downloads;
	String created_at;
	
	public Long getId() {
		return id;
	}
	public String getVersion() {
		return name.replace("Chime-", "").replace(".jar", "");
	}
	public int getBuildNumber() {
		String[] splitVer = getVersion().split("[.]");
		String b = splitVer[splitVer.length-1];
		try {
			return Integer.parseInt(b);
		} catch (NumberFormatException ex) {
			return -1;
		}
	}
	public String getMCVersion() {
		return version;
	}
	public String getType() {
		return type;
	}
	public String getSponsoredURL() {
		return url.toString();
	}
	public String getDownloadURL() {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append("http://addons.curse.cursecdn.com/files/")
			.append(new StringBuilder(id.toString()).insert(id.toString().length()-3, '/'))
			.append('/')
			.append(name);
		return urlBuilder.toString();
	}
	public Date getCreatedAtUTC() throws ParseException {
		DateFormat dateTime = new SimpleDateFormat("yyyy-mm-ddTHH:mm:ss");
		return dateTime.parse(created_at.split("[+-]", 2)[0]);
	}
	public String getFilename() {
		return name;
	}
}
