package com.ibm.xsp.extlibx.relational.jdbc.jndi;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.resources.ResourceFactoriesException;
import com.ibm.xsp.extlib.log.ExtlibCoreLogger;
import com.ibm.xsp.extlib.relational.resources.provider.IJdbcResourceFactoryProvider;

public class ExtLibXJndiRegistry {

	private static final LogMgr logger = ExtlibCoreLogger.RELATIONAL;

	public static final String JNDI_PREFIX = "java:comp/env/jdbc/"; // $NON-NLS-1$

	public static String getJNDIBindName(String connectionName) {
		String jndiName = JNDI_PREFIX + connectionName;
		return jndiName;
	}

	private static Map<String, Integer> connections = new HashMap<String, Integer>();

	public static synchronized void registerConnections(IJdbcResourceFactoryProvider provider)
			throws ResourceFactoriesException {
		// Look at the local providers
		String[] names = provider.getConnectionNames();
		if (names != null) {
			for (int j = 0; j < names.length; j++) {
				String name = names[j];
				Integer n = connections.get(name);
				if (n == null) {
					n = 1;
					// Register the dataSourceName in JNDI
					try {
						Context ctx = new InitialContext();
						String jndiName = ExtLibXJndiRegistry.getJNDIBindName(name);
						ctx.bind(jndiName, new ExtLibXJndiDataSourceProxy(name));
					} catch (NamingException ex) {
						throw new ResourceFactoriesException(ex,
								StringUtil.format("Error while binding JNDI name {0}", name)); // $NLX-JndiRegistry.Errorwhilebinding0name1-1$
																								// $NON-NLS-2$
					}
				} else {
					n = n + 1;
				}
				connections.put(name, n);
			}
		}
	}

	public static synchronized void unregisterConnections(IJdbcResourceFactoryProvider provider) {
		// Nothing, we left them registered...
	}

	public static void debugNames() {

		try {

			if (logger.isTraceDebugEnabled()) {

				InitialContext ctx = new InitialContext();

				NamingEnumeration<NameClassPair> list = ctx.list("java:comp/env/jdbc");

				logger.traceDebug("Showing Name List for JDBC");

				while (list.hasMore()) {
					Object name = list.next();
					logger.traceDebug("name: " + name.toString());
				}

				logger.traceDebug("Finished Name List");

			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

}