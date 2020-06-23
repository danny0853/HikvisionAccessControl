package com.dezon.hikvision.bean;

import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_COND;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_SETUPALARM_PARAM;

public class HikvisionRealTimeConfigBean {

	public NET_DVR_SETUPALARM_PARAM getAlarmConfig(Object cardConfig, Object hikVisionUserBean) {
		NET_DVR_SETUPALARM_PARAM tmp = (NET_DVR_SETUPALARM_PARAM) cardConfig;
		tmp.byDeployType = 1;
		tmp.byRetAlarmTypeV40 =1;
		tmp.byFaceAlarmDetection=1;
		tmp.byAlarmTypeURL= 0 ;
		tmp.byLevel =0;
		tmp.dwSize = tmp.size();
		return tmp;
	}

}
