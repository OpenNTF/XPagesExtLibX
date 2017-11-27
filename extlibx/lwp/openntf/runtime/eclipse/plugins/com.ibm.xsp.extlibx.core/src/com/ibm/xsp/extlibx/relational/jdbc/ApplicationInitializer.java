package com.ibm.xsp.extlibx.relational.jdbc;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.Os;
import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.application.events.ApplicationListener;
import com.ibm.xsp.extlib.log.ExtlibCoreLogger;

import lotus.domino.NotesException;
import lotus.domino.Session;

public class ApplicationInitializer implements ApplicationListener {

	private static final LogMgr logger = ExtlibCoreLogger.RELATIONAL;

	public static final String XSPPROP_USERNAME = "xsp.jdbc.nsfdocumentprovider.username";

	@Override
	public void applicationCreated(ApplicationEx application) {

		String userName = application.getApplicationProperty(XSPPROP_USERNAME, "");
		if (StringUtil.isEmpty(userName)) {
			logger.info("JDBC Connection Details Application Initializer Not Running - No Username specified");
			return;
		} else {
			logger.info("JDBC Connection Details Application Initializer Running with credentials for {0}", userName);
		}

		Session mySession = null;
		long userHandle = 0L;

		try {
			userHandle = NotesUtil.createUserNameList(userName);
			mySession = XSPNative.createXPageSessionExt(userName, userHandle, false, true, true);
		} catch (Exception e) {
			logger.error("Error creating a Notes Session in ApplicationInitializer", e);
		}

		if (mySession != null) {
			//DominoSession.registerSession(mySession);
		}

		try {
			JdbcDataSourceProvider.resetGlobalProvider();
		} catch (Throwable ex) {
			logger.error("Error Initialising Global NSF JDBC Provider", ex);
		}

		try {
			JdbcDataSourceProvider.resetLocalProvider();
		} catch (Throwable ex) {
			logger.error("Error Initialising Local NSF JDBC Provider", ex);
		}

		if (mySession != null) {
			try {
				mySession.recycle();
			} catch (NotesException e) {
				logger.warn("Error During cleanup of ApplicationInitialiser Session", e);
			}
		}

		if (userHandle != 0L) {
			try {
				Os.OSUnlock(userHandle);
			} catch (NException e) {
				logger.warn("Error During cleanup of ApplicationInitialiser Session", e);
			}
			try {
				Os.OSMemFree(userHandle);
			} catch (NException e) {
				logger.warn("Error During cleanup of ApplicationInitialiser Session", e);
			}
		}

	}

	public void applicationRefreshed(ApplicationEx application) {
		try {
			JdbcDataSourceProvider.resetLocalProvider();
		} catch (Throwable ex) {
			logger.error("Error refreshing Local Provider", ex);
		}
	}

	@Override
	public void applicationDestroyed(ApplicationEx application) {
		try {
			JdbcDataSourceProvider.unregisterLocalProvider();
		} catch (Throwable ex) {
			logger.error("Error unregistering local provider", ex);
		}
	}

}
