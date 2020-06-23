package com.dezon.hikvision.bean;

import com.dezon.hikvision.sdk.HCNetSDK;

public class HikvisionErrorMsgBean {
	private String waitMsg = "please wait..............";
	private String succeedMsg = "connected succeed. ";
	private String finishMsg = "All data have been received.";
	private String failMsg = "Connect Failed.";
	private String exceptionMsg = "Exception error.";

	public void printMsg(int statusCode, String description) {
		if (statusCode == -1) {
			System.out.println(failMsg);
		} else {
			if (statusCode == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
				System.out.println(succeedMsg);
			} else if (statusCode == HCNetSDK.NET_SDK_CONFIG_STATUS_NEEDWAIT) {
				System.out.println(waitMsg);
			} else if (statusCode == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
				System.out.println(failMsg);
			} else if (statusCode == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
				System.out.println(exceptionMsg);
			}
		}
	}

	public String getWaitMsg() {
		return waitMsg;
	}

	public void setWaitMsg(String waitMsg) {
		this.waitMsg = waitMsg;
	}

	public String getSucceedMsg() {
		return succeedMsg;
	}

	public void setSucceedMsg(String succeedMsg) {
		this.succeedMsg = succeedMsg;
	}

	public String getFinishMsg() {
		return finishMsg;
	}

	public void setFinishMsg(String finishMsg) {
		this.finishMsg = finishMsg;
	}

	public String getFailMsg() {
		return failMsg;
	}

	public void setFailMsg(String failMsg) {
		this.failMsg = failMsg;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

}
