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

	public static final String RESOURCETYPE = "NSFJDBC";

	private static IJdbcResourceFactoryProvider provider = new NSFDocumentJdbcProvider();

	public JdbcDataSourceProvider() {

		logger.traceDebug("Created a JdbcDataSourceProvider");

	}

	public static void resetProvider() throws ResourceFactoriesException {
		ExtLibXJndiRegistry.registerConnections(provider);
	}

	public static void unregisterLocalProvider() throws ResourceFactoriesException {
		ExtLibXJndiRegistry.unregisterConnections(provider);
	}

	public IJdbcResourceFactoryProvider getProvider() {
		return provider;
	}

	@Override
	public IResourceFactory loadResource(ResourceFactoriesPool pool, String type, String name, int scope)
			throws ResourceFactoriesException {

		if (StringUtil.equals(type, RESOURCETYPE)) {
			logger.traceDebug("Being asked for Jdbc Resource '{0}'", name);
			switch (scope) {
			case IResourceFactory.SCOPE_APPLICATION: {

				// Look at the local providers
				IJdbcResourceFactory f = getProvider().loadResourceFactory(name);
				if (f != null) {
					logger.traceDebug("Found Jdbc Resource '{0}'", name);
					return f;
				}

				logger.traceDebug("Could not find Jdbc Resource '{0}'", name);
				return null;
			}
			}
		}
		return null;
	}

}
