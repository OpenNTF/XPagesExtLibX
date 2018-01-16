package com.ibm.xsp.extlibx.relational.jdbc.jndi;

import javax.sql.DataSource;

import com.ibm.designer.runtime.Application;
import com.ibm.designer.runtime.resources.ResourceFactoriesException;
import com.ibm.designer.runtime.resources.ResourceFactoriesPool;
import com.ibm.xsp.extlib.relational.jdbc.jndi.JndiDataSourceProxy;
import com.ibm.xsp.extlib.relational.resources.IJdbcResourceFactory;
import com.ibm.xsp.extlibx.relational.jdbc.JdbcDataSourceProvider;

public class ExtLibXJndiDataSourceProxy extends JndiDataSourceProxy {

	private String name;

	public ExtLibXJndiDataSourceProxy(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public DataSource getWrappedDatasource() throws ResourceFactoriesException {

		// Look for an application+global specific data source
		Application app = Application.getRuntimeApplicationObject();
		if (app != null) {
			return (IJdbcResourceFactory) app.getResourceFactoriesPool()
					.getResourceFactory(JdbcDataSourceProvider.RESOURCETYPE, name);
		}
		// Else, look for a simply global one
		return (IJdbcResourceFactory) ResourceFactoriesPool.getGlobalPool()
				.getResourceFactory(JdbcDataSourceProvider.RESOURCETYPE, name);

	}

}
