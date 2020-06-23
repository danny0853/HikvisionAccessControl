package com.dezon.hikvision;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.dezon.hikvision.util.HikCommonUtil;
import com.dezon.hikvision.util.HikDeviceEventUtil;

public class HIKIntegration {

//	Calendar startDate = Calendar.getInstance();
//	Calendar endDate = Calendar.getInstance();
//	startDate.set(2020, 1, 1);
//	endDate.set(2020, 6, 30);
//	hikUtil.getRecordByRange(startDate, endDate , true);

	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

		HikCommonUtil hikUtil = new HikCommonUtil(HikvisionConnectionConfig.sDeviceIPAddress,
				HikvisionConnectionConfig.userName, HikvisionConnectionConfig.password, HikvisionConnectionConfig.port);
		int status = hikUtil.Login();

		if (status > -1) {
			HikDeviceEventUtil deviceEventUtil = new HikDeviceEventUtil();
			deviceEventUtil.setlUserID(status);
			List rs = deviceEventUtil.getRecordByRange();
			for (Object obj : rs) {
				Map tmp = (Map) obj;
				System.out.println(tmp.toString());
			}
		}

	}
}
