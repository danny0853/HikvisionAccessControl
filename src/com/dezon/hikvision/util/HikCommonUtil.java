package com.dezon.hikvision.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.dezon.hikvision.HikvisionConnectionConfig;
import com.dezon.hikvision.bean.HikvisionErrorMsgBean;
import com.dezon.hikvision.sdk.HCNetSDK;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_ACS_EVENT_CFG;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_TIME;
import com.sun.javafx.collections.MappingChange.Map;
import com.sun.jna.Pointer;

public class HikCommonUtil {

	static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
	static HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
	static int lUserID = -1;

	private static String deviceIPAddress;
	private static String userName;
	private static String password;
	private static int port;
	
	public HikCommonUtil(String deviceIPAddress, String userName, String password, int port) {
		super();
		hCNetSDK.NET_DVR_Init();
		this.deviceIPAddress = deviceIPAddress.trim();
		this.userName = userName.trim();
		this.password = password.trim();
		this.port = port;
	}


	public static Object commandStartRemoteConfig(Class className, Class<?> executeClass, String methodName,
			Object param) {
		try {

			Object target = className.newInstance();
			target.getClass().getMethod("read").invoke(target);

			Method m = executeClass.getMethod(methodName, new Class[] { Object.class, Object.class });
			target = m.invoke(executeClass.newInstance(), target, param);

			target.getClass().getMethod("write").invoke(target);

			return target;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int simpleCommand(String type, int lUserID, int Command, Pointer pointer, int size) {
		int status = -1;

		if ("startRemoteConfig".equals(type)) {
			status = hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD, pointer, size, null, null);
		}

		if (status == -1) {
			System.out.println("Connect failed . Code : " + HikCommonUtil.hCNetSDK.NET_DVR_GetLastError());
			return status;
		} else {
			System.out.println("RemoteConfig Connected.");
			return status;
		}
	}


	public int Login() {
		lUserID = hCNetSDK.NET_DVR_Login_V30(deviceIPAddress, (short) port, userName, password, m_strDeviceInfo);
		
		
		
		if (lUserID == -1) {
			System.out.println("Login error  , code is : " + hCNetSDK.NET_DVR_GetLastError());
			return -1;
		} else {
			System.out.println(userName + " login succeed. id : " + lUserID);
			return lUserID;
		}
	}

	public static NET_DVR_TIME getDateToDvrTime(Calendar calendar, String type) {
		HCNetSDK.NET_DVR_TIME struStartTime = new HCNetSDK.NET_DVR_TIME();
		struStartTime.read();
		struStartTime.dwYear = calendar.get(Calendar.YEAR);
		struStartTime.dwMonth = calendar.get(Calendar.MONTH) + 1;
		struStartTime.dwDay = calendar.get(Calendar.DATE);

		if (null == type) {
			struStartTime.dwHour = calendar.get(Calendar.HOUR);
			struStartTime.dwMinute = calendar.get(Calendar.MINUTE);
			struStartTime.dwSecond = calendar.get(Calendar.SECOND);
		}

		if ("startDate".equals(type)) {
			struStartTime.dwHour = 0;
			struStartTime.dwMinute = 0;
			struStartTime.dwSecond = 0;
		}

		if ("endDate".equals(type)) {
			struStartTime.dwHour = 23;
			struStartTime.dwMinute = 59;
			struStartTime.dwSecond = 59;
		}

		struStartTime.write();
		return struStartTime;

	}

}
