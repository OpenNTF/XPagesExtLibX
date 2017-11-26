package com.ibm.xsp.extlibx.relational.jdbc;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.resources.IResourceFactory;
import com.ibm.designer.runtime.resources.ResourceFactoriesException;
import com.ibm.designer.runtime.resources.ResourceFactoriesPool;
import com.ibm.designer.runtime.resources.ResourceFactoryProvider;
import com.ibm.xsp.extlib.log.ExtlibCoreLogger;
import com.ibm.xsp.extlib.relational.resources.IJdbcResourceFactory;
import com.ibm.xsp.extlib.relational.resources.provider.IJdbcResourceFactoryProvider;
import com.ibm.xsp.extlibx.relational.jdbc.jndi.ExtLibXJndiRegistry;

public class JdbcDataSourceProvider implements ResourceFactoryProvider {

	private static final LogMgr logger = ExtlibCoreLogger.RELATIONAL;
	
	private static final String GLOBAL_CONFIG_DB = "Horizon/Globals.nsf";

	public static final String RESOURCETYPE = "HorizonJDBC";

	private static Boolean globalRegistered = false;

	private static IJdbcResourceFactoryProvider globalProvider = new NSFDocumentJdbcProvider(GLOBAL_CONFIG_DB);

	private static IJdbcResourceFactoryProvider localProvider = new NSFDocumentJdbcProvider();

	public static void resetLocalProvider() throws ResourceFactoriesException {
		ExtLibXJndiRegistry.registerConnections(localProvider);
	}

	public static void resetGlobalProvider() throws ResourceFactoriesException {
		ExtLibXJndiRegistry.registerConnections(globalProvider);
	}

	public static void initGlobalProvider() throws ResourceFactoriesException {

		synchronized (globalRegistered) {
			if (!globalRegistered) {
				ExtLibXJndiRegistry.registerConnections(globalProvider);
				globalRegistered = true;
			}
		}

	}

	public static void unregisterLocalProvider() throws ResourceFactoriesException {
		ExtLibXJndiRegistry.unregisterConnections(localProvider);
	}

	public static void unregisterGlobalProvider() throws ResourceFactoriesException {
		ExtLibXJndiRegistry.unregisterConnections(globalProvider);
	}

	public JdbcDataSourceProvider() {

		logger.traceDebug("Created a JdbcDataSourceProvider");

	}

	public IJdbcResourceFactoryProvider getLocalProvider() {
		return localProvider;
	}

	public IJdbcResourceFactoryProvider getGlobalProvider() {
		return globalProvider;
	}

	@Override
	public IResourceFactory loadResource(ResourceFactoriesPool pool, String type, String name, int scope)
			throws ResourceFactoriesException {

		if (StringUtil.equals(type, RESOURCETYPE)) {
			logger.traceDebug("Being asked for Jdbc Resource '{}'", name);
			switch (scope) {
			case IResourceFactory.SCOPE_APPLICATION: {
				
				// Look at the local providers
				IJdbcResourceFactory f = getLocalProvider().loadResourceFactory(name);
				if (f != null) {
					logger.traceDebug("Found " + name + " in Local");
					return f;
				}

				f = getGlobalProvider().loadResourceFactory(name);
				if (f != null) {
					logger.traceDebug("Found JDBC Resource " + name + " in Global");
					return f;
				}

				logger.traceDebug("Could not find Jdbc Resource '{}'", name);
				return null;
			}
			case IResourceFactory.SCOPE_GLOBAL: {
				IJdbcResourceFactory f = getGlobalProvider().loadResourceFactory(name);
				if (f != null) {
					return f;
				}
				return null;
			}
			}
		}
		return null;
	}

}
