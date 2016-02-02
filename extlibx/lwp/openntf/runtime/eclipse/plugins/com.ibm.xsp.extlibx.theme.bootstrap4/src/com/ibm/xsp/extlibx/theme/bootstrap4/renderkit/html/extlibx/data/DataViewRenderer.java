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
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.data;

/**
 *
 * @author Brian Gleeson (brian.gleeson@ie.ibm.com)
 */
public class DataViewRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.data.DataViewRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_SUMMARYTITLETAG:          return "h6"; // $NON-NLS-1$
            
            case PROP_HEADERLEFTCLASS:          return "pull-xs-left"; // $NON-NLS-1$
            case PROP_HEADERRIGHTCLASS:         return "pull-xs-right"; // $NON-NLS-1$

            case PROP_FOOTERLEFTCLASS:          return "pull-xs-left"; // $NON-NLS-1$
            case PROP_FOOTERRIGHTCLASS:         return "pull-xs-right"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}