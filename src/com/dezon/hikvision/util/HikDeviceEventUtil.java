package com.dezon.hikvision.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dezon.hikvision.sdk.HCNetSDK;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_ACS_EVENT_CFG;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_ACS_EVENT_COND;

public class HikDeviceEventUtil {

	public int lUserID;

	public HikDeviceEventUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List getRecordByRange(Calendar startDate, Calendar endDate, boolean isNoTimeDetail) {

		NET_DVR_ACS_EVENT_COND getRecordConfig;
		getRecordConfig = (null != startDate || null != endDate)
				? searchDeviceEventLogDefaultValue(startDate, endDate, isNoTimeDetail)
				: searchDeviceEventLogDefaultValue();

		int lHandle = HikCommonUtil.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID,
				HikCommonUtil.hCNetSDK.NET_DVR_GET_ACS_EVENT, getRecordConfig.getPointer(), getRecordConfig.size(),
				null, null);

		if (lHandle == -1) {
			System.out.println("Connect failed . Code : " + HikCommonUtil.hCNetSDK.NET_DVR_GetLastError());
			return null;
		} else {
			System.out.println("RemoteConfig Connected.");

			NET_DVR_ACS_EVENT_CFG getData = new HCNetSDK.NET_DVR_ACS_EVENT_CFG();
			List eventList = getDeviceEventData(new ArrayList(), lHandle, getData, 1001);
			return eventList;

		}

	}

	public List getRecordByRange() {
		return getRecordByRange(null, null, true);
	}

	public NET_DVR_ACS_EVENT_COND searchDeviceEventLogDefaultValue() {
		Calendar startDate = Calendar.getInstance();
		startDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 1);
		Calendar endDate = Calendar.getInstance();
		endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));

		return searchDeviceEventLogDefaultValue(startDate, endDate, true);
	}

	public NET_DVR_ACS_EVENT_COND searchDeviceEventLogDefaultValue(Calendar startDate, Calendar endDate) {
		return searchDeviceEventLogDefaultValue(Calendar.getInstance(), Calendar.getInstance(), false);
	}

	public NET_DVR_ACS_EVENT_COND searchDeviceEventLogDefaultValue(Calendar startDate, Calendar endDate,
			boolean isDefault) {

		System.out.println(startDate.getTime());
		System.out.println(endDate.getTime());

		HCNetSDK.NET_DVR_ACS_EVENT_COND getRecordConfig;

		getRecordConfig = new HCNetSDK.NET_DVR_ACS_EVENT_COND();
		getRecordConfig.read();
		getRecordConfig.dwSize = getRecordConfig.size();
		// getRecordConfig.byName = "danny".getBytes();
		getRecordConfig.struStartTime = HikCommonUtil.getDateToDvrTime(startDate, (isDefault) ? "startDate" : null);
		getRecordConfig.struEndTime = HikCommonUtil.getDateToDvrTime(endDate, (isDefault) ? "endDate" : null);

		getRecordConfig.dwMajor = 5;// 5代表event事件
		// getRecordConfig.dwMinor = 0x4b;// 代表刷脸成功
		// getRecordConfig.byTimeType = 1;

		// getRecordConfig.dwBeginSerialNo = 0; // 起始流水号（为0时默认全部）
		// getRecordConfig.dwEndSerialNo = 0; // 结束流水号（为0时默认全部）
		// getRecordConfig.byEventAttribute = 1;
		getRecordConfig.write();

		System.out.println("start : " + getRecordConfig.struStartTime.toStringTime() + " end : "
				+ getRecordConfig.struEndTime.toStringTime());

		return getRecordConfig;
	}

	public List getDeviceEventData(ArrayList rs, int lHandle, NET_DVR_ACS_EVENT_CFG getData, int State) {
		int dwState = HikCommonUtil.hCNetSDK.NET_DVR_GetNextRemoteConfig(lHandle, getData.getPointer(), getData.size());
		getData.read();
		if (dwState == -1) {
			System.out.println("NET_DVR_GetNextRemoteConfig Connect failed. Error Code  :"
					+ HikCommonUtil.hCNetSDK.NET_DVR_GetLastError());
		}

		if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
			Map eventMap = new HashMap();
			eventMap.put("employeeNo", new String(getData.struAcsEventInfo.byEmployeeNo));
			eventMap.put("cardNo", new String(getData.struAcsEventInfo.byCardNo));
			eventMap.put("struTime", getData.struTime.toStringTime());
			
			
			System.out.println(eventMap.toString());
			
			rs.add(eventMap);

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getDeviceEventData(rs, lHandle, getData, dwState);
		}

		if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEEDWAIT) {
			HikCommonUtil.hCNetSDK.NET_DVR_GetNextRemoteConfig(lHandle, getData.getPointer(), getData.size());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getDeviceEventData(rs, lHandle, getData, dwState);
		}

		if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
			
			try {
				Thread.sleep(100);
				getDeviceEventData(rs, lHandle, getData, dwState);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		return rs;

	}

	public int getlUserID() {
		return lUserID;
	}

	public void setlUserID(int lUserID) {
		this.lUserID = lUserID;
	}

}
