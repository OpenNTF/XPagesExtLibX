 /*
 * © Copyright IBM Corp. 2011, 2012
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

package com.ibm.xsp.extlibx.library;

import java.util.ArrayList;
import java.util.List;

import com.ibm.xsp.extlib.config.ExtlibPluginConfig;
import com.ibm.xsp.extlibx.controls.config.ControlsXConfig;
import com.ibm.xsp.library.AbstractXspLibrary;
import com.ibm.xsp.extlibx.theme.bootstrap4.config.Bootstrap4Config;

/**
 *
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 */
public class ExtlibXLibrary extends AbstractXspLibrary {

	private List<ExtlibPluginConfig> plugins;

    public String getLibraryId() {
        return "com.ibm.xsp.extlibx.library"; //$NON-NLS-1$
    }

    @Override
    public String[] getDependencies() {
        return new String[]{
                "com.ibm.xsp.core.library", // $NON-NLS-1$
                "com.ibm.xsp.extsn.library", // $NON-NLS-1$
                "com.ibm.xsp.domino.library", // $NON-NLS-1$
                "com.ibm.xsp.designer.library", // $NON-NLS-1$
                "com.ibm.xsp.extlib.library", //$NON-NLS-1$
        };
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.library.AbstractXspLibrary#getFacesConfigFiles()
     */
    @Override
    public String[] getFacesConfigFiles() {
        String[] files = new String[] {};
        List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
        for( ExtlibPluginConfig plugin: plugins) {
        	files = plugin.getFacesConfigFiles(files);
        }
        return files;
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.library.AbstractXspLibrary#getPluginId()
     */
    @Override
    public String getPluginId() {
        return "com.ibm.xsp.extlibx"; //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.library.AbstractXspLibrary#getXspConfigFiles()
     */
    @Override
    public String[] getXspConfigFiles() {
        String[] files = new String[] {};
        List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
        for( ExtlibPluginConfig plugin: plugins) {
        	files = plugin.getXspConfigFiles(files);
        }
        return files;
    }

    private List<ExtlibPluginConfig> getExtlibPluginConfigs() {
    	if(plugins==null) {
    		List<ExtlibPluginConfig> _plugins = new ArrayList<ExtlibPluginConfig>();
    		_plugins.add(new ControlsXConfig());
            _plugins.add(new Bootstrap4Config());
            plugins = _plugins;
    	}
		return plugins;
	}
    
    public void installResources() {
        List<ExtlibPluginConfig> plugins = getExtlibPluginConfigs();
        for( ExtlibPluginConfig plugin: plugins) {
        	plugin.installResources();
        }
    }
}
