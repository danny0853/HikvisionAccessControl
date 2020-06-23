package com.dezon.hikvision;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class HikvisionConnectionConfig {
	public static String sDeviceIPAddress = "";
	public static String userName = "";
	public static String password = "";
	public static int port = 8000;

	public static String dllPath = "E:\\exlipseWorkspace\\HikvisionAccessControl\\lib\\HCNetSDK.dll";
	public static String playCtrlPath = "E:\\exlipseWorkspace\\HikvisionAccessControl\\lib\\PlayCtrl.dll";

}
