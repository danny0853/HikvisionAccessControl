package com.dezon.hikvision.bean;

import com.dezon.hikvision.sdk.HCNetSDK.FMessageCallBack;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_RECORD;

public class HikvisionMessageCallBackBean implements FMessageCallBack {

	@Override
	public boolean invoke(int lCommand, String sDVRIP, String pBuf, int dwBufLen, int dwUser) {
		System.out.println("dwUser : " + dwUser + " lCommand : " + lCommand + " sDVRIP" + sDVRIP + " pBuf" + pBuf);
		return true;
	}
}
