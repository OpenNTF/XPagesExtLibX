package com.ibm.xsp.extlibx.relational.jdbc;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.extlib.log.ExtlibCoreLogger;

public class JdbcProviderPhaseListener implements PhaseListener {

	private static final LogMgr logger = ExtlibCoreLogger.RELATIONAL;

	private static final long serialVersionUID = 1L;

	private Boolean uselocal = null;
	private Boolean useglobal = null;
	private boolean registered = false;

	public JdbcProviderPhaseListener() {

	}

	@Override
	public void afterPhase(PhaseEvent event) {

	}

	private Boolean isUseLocal() {

		if (uselocal == null) {
			ApplicationEx app = ApplicationEx.getInstance();
			String local = app.getApplicationProperty(NSFDocumentJdbcProvider.XSPPROP_LOCALPROVIDER, "");
			uselocal = StringUtil.isTrueValue(local);
		}
		return uselocal;

	}

	private Boolean isUseGlobal() {

		if (useglobal == null) {
			ApplicationEx app = ApplicationEx.getInstance();
			String globalFilePath = app.getApplicationProperty(NSFDocumentJdbcProvider.XSPPROP_GLOBALPROVIDERPATH, "");
			useglobal = StringUtil.isNotEmpty(globalFilePath);
		}
		return useglobal;

	}

	@Override
	public void beforePhase(PhaseEvent event) {

		if ((isUseLocal() || isUseGlobal()) && !registered) {
			logger.traceDebug("Initialising JDBCNotesDocumentProvider");
			try {
				JdbcDataSourceProvider.resetProvider();
				registered = true;
			} catch (Exception e) {

			}
		}

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
