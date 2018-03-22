package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class FtpServerInfo {

	private String ftp_ip;
	private String ftp_path;
	private String ftp_account;
	private String ftp_password;
	
	public String getFtp_path() {
		return ftp_path;
	}
	public void setFtp_path(String ftp_path) {
		this.ftp_path = ftp_path;
	}
	public String getFtp_account() {
		return ftp_account;
	}
	public void setFtp_account(String ftp_account) {
		this.ftp_account = ftp_account;
	}
	public String getFtp_password() {
		return ftp_password;
	}
	public void setFtp_password(String ftp_password) {
		this.ftp_password = ftp_password;
	}
	public String getFtp_ip() {
		return ftp_ip;
	}
	public void setFtp_ip(String ftp_ip) {
		this.ftp_ip = ftp_ip;
	}
	
}
