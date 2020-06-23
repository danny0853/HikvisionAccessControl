package com.dezon.hikvision.bean;

import java.io.UnsupportedEncodingException;

import com.dezon.hikvision.sdk.HCNetSDK;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_COND;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_RECORD;

public class HikvisionCardConfigBean {
	public NET_DVR_CARD_COND getCardConfig(Object cardConfig, Object hikVisionUserBean) {
		NET_DVR_CARD_COND struCardCond = (NET_DVR_CARD_COND) cardConfig;
		struCardCond.dwSize = struCardCond.size();
		struCardCond.dwCardNum = 1;
		return struCardCond;
	}

	public NET_DVR_CARD_COND getAllCardsConfig(Object cardConfig, Object hikVisionUserBean) {
		NET_DVR_CARD_COND struCardCond = (NET_DVR_CARD_COND) cardConfig;
		struCardCond.dwSize = struCardCond.size();
		struCardCond.dwCardNum = 0xffffffff;
		return struCardCond;
	}

	public NET_DVR_CARD_RECORD getWriteCardConfig(Object cardConfig, Object hikVisionUserBean)
			throws UnsupportedEncodingException {
		NET_DVR_CARD_RECORD struCardRecord = (NET_DVR_CARD_RECORD) cardConfig;
		HikVisionUserBean tmp = (HikVisionUserBean) hikVisionUserBean;
		String strCardNo = (String) tmp.getCardNo();
		String userName = (String) tmp.getUserName();
		String dwEmployeeNo = (String) tmp.getDwEmployeeNo();

		struCardRecord.dwSize = struCardRecord.size();

		struCardRecord.dwEmployeeNo = Integer.valueOf(dwEmployeeNo);
		for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
			struCardRecord.byCardNo[i] = 0;
		}

		for (int i = 0; i < strCardNo.length(); i++) {
			struCardRecord.byCardNo[i] = strCardNo.getBytes()[i];
		}

		byte[] strCardName = userName.getBytes("UTF-8"); // 姓名
		for (int i = 0; i < HCNetSDK.NAME_LEN; i++) {
			struCardRecord.byName[i] = 0;
		}
		for (int i = 0; i < strCardName.length; i++) {
			struCardRecord.byName[i] = strCardName[i];
		}

		struCardRecord.wCardRightPlan[0] = 1;// 卡计划模板1有效
		struCardRecord.byCardType = 1; // 普通卡
		struCardRecord.byLeaderCard = 0; // 是否为首卡，0-否，1-是
		struCardRecord.byUserType = 0;
		struCardRecord.byDoorRight[0] = 1; // 门1有权限

		struCardRecord.struValid.byEnable = 1; // 卡有效期使能，下面是卡有效期从2000-1-1 11:11:11到2030-1-1 11:11:11
		struCardRecord.struValid.struBeginTime.wYear = 2000;
		struCardRecord.struValid.struBeginTime.byMonth = 1;
		struCardRecord.struValid.struBeginTime.byDay = 1;
		struCardRecord.struValid.struBeginTime.byHour = 11;
		struCardRecord.struValid.struBeginTime.byMinute = 11;
		struCardRecord.struValid.struBeginTime.bySecond = 11;
		struCardRecord.struValid.struEndTime.wYear = 2030;
		struCardRecord.struValid.struEndTime.byMonth = 1;
		struCardRecord.struValid.struEndTime.byDay = 1;
		struCardRecord.struValid.struEndTime.byHour = 11;
		struCardRecord.struValid.struEndTime.byMinute = 11;
		struCardRecord.struValid.struEndTime.bySecond = 11;
		return struCardRecord;
	}

}
