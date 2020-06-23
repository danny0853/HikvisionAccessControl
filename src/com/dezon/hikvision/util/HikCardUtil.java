package com.dezon.hikvision.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.dezon.hikvision.HikvisionConnectionConfig;
import com.dezon.hikvision.bean.HikVisionUserBean;
import com.dezon.hikvision.bean.HikvisionCardConfigBean;
import com.dezon.hikvision.sdk.HCNetSDK;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_COND;
import com.dezon.hikvision.sdk.HCNetSDK.NET_DVR_CARD_RECORD;
import com.sun.jna.ptr.IntByReference;

public class HikCardUtil {

	public int lUserID;
	private String strCardNo;

	private HikVisionUserBean hikVisionUserBean = new HikVisionUserBean();

	public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
		HikCommonUtil hikUtil = new HikCommonUtil(HikvisionConnectionConfig.sDeviceIPAddress,
				HikvisionConnectionConfig.userName, HikvisionConnectionConfig.password, HikvisionConnectionConfig.port);
		HikCardUtil hikCardUtil = new HikCardUtil();
		int status = hikUtil.Login();
		hikCardUtil.setlUserID(status);

		HikVisionUserBean hikVisionUserBean = new HikVisionUserBean("33", "3l", "625");

		hikCardUtil.SetOneCardTest(hikVisionUserBean);
	}

	public void SetOneCardTest(HikVisionUserBean hikVisionUserBean) {
		int dwStatus = -1;

		// this.strCardNo = strCardNo;

		NET_DVR_CARD_COND struCardCond = (NET_DVR_CARD_COND) HikCommonUtil.commandStartRemoteConfig(
				NET_DVR_CARD_COND.class, HikvisionCardConfigBean.class, "getCardConfig", hikVisionUserBean);
		dwStatus = HikCommonUtil.simpleCommand("startRemoteConfig", this.lUserID, HCNetSDK.NET_DVR_SET_CARD,
				struCardCond.getPointer(), struCardCond.size());
		if (dwStatus > -1) {

			NET_DVR_CARD_RECORD struCardRecord = (NET_DVR_CARD_RECORD) HikCommonUtil.commandStartRemoteConfig(
					NET_DVR_CARD_RECORD.class, HikvisionCardConfigBean.class, "getWriteCardConfig", hikVisionUserBean);

			HCNetSDK.NET_DVR_CARD_STATUS struCardStatus = new HCNetSDK.NET_DVR_CARD_STATUS();
			struCardStatus.read();
			struCardStatus.dwSize = struCardStatus.size();
			struCardStatus.write();

			IntByReference pInt = new IntByReference(0);

			dwStatus = HikCommonUtil.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(dwStatus, struCardRecord.getPointer(),
					struCardRecord.size(), struCardStatus.getPointer(), struCardStatus.size(), pInt);
			struCardStatus.read();

			if (dwStatus == -1) {
				System.out.println(
						"NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + HikCommonUtil.hCNetSDK.NET_DVR_GetLastError());

			} else if (dwStatus == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
				System.out.println(new String(struCardStatus.byCardNo).trim() + " : 配置等待 : " + struCardStatus.byStatus);
			} else if (dwStatus == HCNetSDK.NET_SDK_CONFIG_STATUS_NEEDWAIT) {
				System.out.println("配置等待 : " + struCardStatus.byStatus);

				dwStatus = HikCommonUtil.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(dwStatus,
						struCardRecord.getPointer(), struCardRecord.size(), struCardStatus.getPointer(),
						struCardStatus.size(), pInt);
			} else if (dwStatus == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
				System.out.println("下发卡失败, 卡号: ");

			} else if (dwStatus == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
				System.out.println("下发卡异常, 卡号: ");

			} else if (dwStatus == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
				HikCommonUtil.hCNetSDK.NET_DVR_StopRemoteConfig(dwStatus);
			}

		}
	}

//	public void SetOneCard(String strCardNo) throws UnsupportedEncodingException, InterruptedException {
//		HCNetSDK.NET_DVR_CARD_COND struCardCond = new HCNetSDK.NET_DVR_CARD_COND();
//		struCardCond.read();
//		struCardCond.dwSize = struCardCond.size();
//		struCardCond.dwCardNum = 1; // 下发一张
//		struCardCond.write();
//
//		m_lSetCardCfgHandle = HikCommonUtil.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD,
//				struCardCond.getPointer(), struCardCond.size(), null, null);
//		if (m_lSetCardCfgHandle == -1) {
//			System.out.println("建立下发卡长连接失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
//			return;
//		} else {
//			System.out.println("建立下发卡长连接成功！");
//		}
//
//		HCNetSDK.NET_DVR_CARD_RECORD struCardRecord = new HCNetSDK.NET_DVR_CARD_RECORD();
//		struCardRecord.read();
//		struCardRecord.dwSize = struCardRecord.size();
//
//		for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
//			struCardRecord.byCardNo[i] = 0;
//		}
//		for (int i = 0; i < strCardNo.length(); i++) {
//			struCardRecord.byCardNo[i] = strCardNo.getBytes()[i];
//		}
//
//		struCardRecord.byCardType = 1; // 普通卡
//		struCardRecord.byLeaderCard = 0; // 是否为首卡，0-否，1-是
//		struCardRecord.byUserType = 0;
//		struCardRecord.byDoorRight[0] = 1; // 门1有权限
//
//		struCardRecord.struValid.byEnable = 1; // 卡有效期使能，下面是卡有效期从2000-1-1 11:11:11到2030-1-1 11:11:11
//		struCardRecord.struValid.struBeginTime.wYear = 2000;
//		struCardRecord.struValid.struBeginTime.byMonth = 1;
//		struCardRecord.struValid.struBeginTime.byDay = 1;
//		struCardRecord.struValid.struBeginTime.byHour = 11;
//		struCardRecord.struValid.struBeginTime.byMinute = 11;
//		struCardRecord.struValid.struBeginTime.bySecond = 11;
//		struCardRecord.struValid.struEndTime.wYear = 2030;
//		struCardRecord.struValid.struEndTime.byMonth = 1;
//		struCardRecord.struValid.struEndTime.byDay = 1;
//		struCardRecord.struValid.struEndTime.byHour = 11;
//		struCardRecord.struValid.struEndTime.byMinute = 11;
//		struCardRecord.struValid.struEndTime.bySecond = 11;
//
//		struCardRecord.wCardRightPlan[0] = 1;// 卡计划模板1有效
//		struCardRecord.dwEmployeeNo = 66611; // 工号
//
//		byte[] strCardName = "测试".getBytes("GBK"); // 姓名
//		for (int i = 0; i < HCNetSDK.NAME_LEN; i++) {
//			struCardRecord.byName[i] = 0;
//		}
//		for (int i = 0; i < strCardName.length; i++) {
//			struCardRecord.byName[i] = strCardName[i];
//		}
//		struCardRecord.write();
//
//		HCNetSDK.NET_DVR_CARD_STATUS struCardStatus = new HCNetSDK.NET_DVR_CARD_STATUS();
//		struCardStatus.read();
//		struCardStatus.dwSize = struCardStatus.size();
//		struCardStatus.write();
//
//		IntByReference pInt = new IntByReference(0);
//
//		while (true) {
//			dwState = hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lSetCardCfgHandle, struCardRecord.getPointer(),
//					struCardRecord.size(), struCardStatus.getPointer(), struCardStatus.size(), pInt);
//			struCardStatus.read();
//			if (dwState == -1) {
//				System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + hCNetSDK.NET_DVR_GetLastError());
//				break;
//			} else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEEDWAIT) {
//				System.out.println("配置等待");
//				Thread.sleep(10);
//				continue;
//			} else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
//				System.out.println("下发卡失败, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码："
//						+ struCardStatus.dwErrorCode);
//				break;
//			} else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
//				System.out.println("下发卡异常, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码："
//						+ struCardStatus.dwErrorCode);
//				break;
//			} else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
//				if (struCardStatus.dwErrorCode != 0) {
//					System.out.println("下发卡成功,但是错误码" + struCardStatus.dwErrorCode + ", 卡号："
//							+ new String(struCardStatus.byCardNo).trim());
//				} else {
//					System.out.println("下发卡成功, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 状态："
//							+ struCardStatus.byStatus);
//				}
//				continue;
//			} else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
//				System.out.println("下发卡完成");
//				break;
//			}
//
//		}
//
//	}

	public int getlUserID() {
		return lUserID;
	}

	public void setlUserID(int lUserID) {
		this.lUserID = lUserID;
	}
}
