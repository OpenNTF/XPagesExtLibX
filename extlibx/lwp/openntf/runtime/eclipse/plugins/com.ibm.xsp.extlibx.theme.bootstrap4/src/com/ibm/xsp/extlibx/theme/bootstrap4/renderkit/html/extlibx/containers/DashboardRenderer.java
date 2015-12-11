/*
 * © Copyright IBM Corp. 2015
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
* Author: Brian Gleeson (brian.gleeson@ie.ibm.com)
* Date: 29 Oct 2015
* DashboardRenderer.java
*/
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.containers;


/**
 *
 * @author Brian Gleeson (brian.gleeson@ie.ibm.com)
 */
public class DashboardRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.containers.DashboardRenderer {
    
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_NODE_TITLE_TAG:                return "h4"; // $NON-NLS-1$
            case PROP_NODE_DEFAULT_BADGE_CLASS:      return "label dashBadge"; //$NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}