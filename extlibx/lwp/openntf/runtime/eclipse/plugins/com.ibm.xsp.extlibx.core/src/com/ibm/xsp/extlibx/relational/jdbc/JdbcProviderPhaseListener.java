package com.ibm.xsp.extlibx.relational.jdbc;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ApplicationEx;

public class JdbcProviderPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private Boolean uselocal = null;
	private boolean localregistered = false;
	private Boolean useglobal=null;
	private String globalFilePath = null;
	private boolean globalregistered = false;

	public JdbcProviderPhaseListener() {

	}

	@Override
	public void afterPhase(PhaseEvent event) {

	}

	private Boolean isUseLocal() {

		if (uselocal == null) {
			ApplicationEx app = ApplicationEx.getInstance();
			String local = app.getApplicationProperty(JdbcDataSourceProvider.XSPPROP_LOCALPROVIDER, "");
			uselocal = StringUtil.isTrueValue(local);
		}
		return uselocal;

	}

	private Boolean isUseGlobal() {

		if (useglobal == null) {
			ApplicationEx app = ApplicationEx.getInstance();
			globalFilePath = app.getApplicationProperty(JdbcDataSourceProvider.XSPPROP_GLOBALPROVIDERPATH, "");
			useglobal = StringUtil.isNotEmpty(globalFilePath);
		}
		return useglobal;

	}

	@Override
	public void beforePhase(PhaseEvent event) {

		if (isUseLocal() && !localregistered) {
			System.out.println("Doing Local");
			try {
				JdbcDataSourceProvider.resetLocalProvider();
				localregistered = true;
			} catch (Exception e) {

			}
		}

		if (isUseGlobal() && !globalregistered) {
			System.out.println("Doing Global");
			try {
				JdbcDataSourceProvider.resetGlobalProvider(globalFilePath);
				globalregistered = true;
			} catch (Exception e) {

			}
		}

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
