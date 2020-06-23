package com.dezon.hikvision.bean;

public class HikVisionUserBean extends HikvisionErrorMsgBean {
	private String userName;
	private String cardNo;

	public HikVisionUserBean(String userName, String cardNo, String dwEmployeeNo) {
		super();
		this.userName = userName;
		this.cardNo = cardNo;
		this.dwEmployeeNo = dwEmployeeNo;
	}

	// Id
	private String dwEmployeeNo;

	public HikVisionUserBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getDwEmployeeNo() {
		return dwEmployeeNo;
	}

	public void setDwEmployeeNo(String dwEmployeeNo) {
		this.dwEmployeeNo = dwEmployeeNo;
	}

}
