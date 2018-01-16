/*
 * © Copyright IBM Corp. 2018
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
/*
* Author: Cameron Gregor
* Date: 15 Jan 2018
* CoreXConfig.java
*/
package com.ibm.xsp.extlibx.config;

import com.ibm.xsp.extlib.config.ExtlibPluginConfig;

public class CoreXConfig extends ExtlibPluginConfig {

	@Override
	public String[] getXspConfigFiles(String[] files) {
		return files;
//		return concat(files, new String[] { 
//				"com/ibm/xsp/extlibx/controls/config/extlib-table.xsp-config", // $NON-NLS-1$
//		});
	}

	@Override
	public String[] getFacesConfigFiles(String[] files) {
		return concat(files, new String[] { 
				"com/ibm/xsp/extlibx/config/extlibx-core-faces-config.xml", // $NON-NLS-1$
		});
	}

}
