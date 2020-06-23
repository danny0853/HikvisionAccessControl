package com.dezon.hikvision.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.dezon.hikvision.HikvisionConnectionConfig;
import com.dezon.hikvision.bean.HikVisionUserBean;
import com.dezon.hikvision.bean.HikvisionCardConfigBean;
import com.dezon.hikvision.bean.HikvisionMessageCallBackBean;
import com.dezon.hikvision.bean.HikvisionRealTimeConfigBean;
import com.dezon.hikvision.sdk.HCNetSDK;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_COND;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_SETUPALARM_PARAM;

public class HikRealTimeDataUtil {
	private int lUserID;

	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

		HikCommonUtil hikUtil = new HikCommonUtil(HikvisionConnectionConfig.sDeviceIPAddress,
				HikvisionConnectionConfig.userName, HikvisionConnectionConfig.password, HikvisionConnectionConfig.port);

		HikRealTimeDataUtil hikRealTimeDataUtil = new HikRealTimeDataUtil();
		int status = hikUtil.Login();

		HikvisionMessageCallBackBean hikvisionMessageCallBackBean = new HikvisionMessageCallBackBean();

		boolean isCallBack = HikCommonUtil.hCNetSDK.NET_DVR_SetDVRMessageCallBack(hikvisionMessageCallBackBean, status);

		if (isCallBack) {
			System.out.println("setup isCallBack");
			int alarmHandler = hikRealTimeDataUtil.setupAlarm(new HikVisionUserBean());
		
			if(alarmHandler >= 0 ) {
				System.out.println("NET_DVR_SetupAlarmChan" );
				HikDeviceEventUtil deviceEventUtil = new HikDeviceEventUtil();
				deviceEventUtil.setlUserID(alarmHandler);
				List rs = deviceEventUtil.getRecordByRange();
				System.out.println(rs.size());
				
			} else {
				System.out.println( "NET_DVR_SetupAlarmChan Failed");
			}
			
			
		} else {
			System.out.println("setup isCallBack failed. ErrorCode : " + HikCommonUtil.hCNetSDK.NET_DVR_GetLastError());
		}

	}

	public int setupAlarm(HikVisionUserBean hikVisionUserBean) {
		NET_DVR_SETUPALARM_PARAM lpSetupParam = (NET_DVR_SETUPALARM_PARAM) HikCommonUtil.commandStartRemoteConfig(
				NET_DVR_SETUPALARM_PARAM.class, HikvisionRealTimeConfigBean.class, "getAlarmConfig", hikVisionUserBean);

		
		System.out.println(lpSetupParam);
		
		int dwStatus = HikCommonUtil.hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, lpSetupParam);
		
		
	

		System.out.println("hikVisionUserBean  : " + dwStatus);
		return dwStatus;
	}

	public int getlUserID() {
		return lUserID;
	}

	public void setlUserID(int lUserID) {
		this.lUserID = lUserID;
	}

}
