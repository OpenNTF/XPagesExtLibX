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
package com.ibm.xsp.extlibx.theme.bootstrap4.themes;

import java.io.InputStream;

public class Bootstrap4StyleKitFactory implements com.ibm.xsp.stylekit.StyleKitFactory, com.ibm.xsp.stylekit.StyleKitListFactory {

    public Bootstrap4StyleKitFactory() {
    }

    @Override
    public InputStream getThemeAsStream(String themeId, int scope) {
        if (scope == Bootstrap4StyleKitFactory.STYLEKIT_GLOBAL) {
            String folderPath = "com/ibm/xsp/extlibx/theme/bootstrap4/themes"; //$NON-NLS-1$
            if (themeId.equalsIgnoreCase("Bootstrap4")) { //$NON-NLS-1$
                return getThemeFromBundle(folderPath + "/Bootstrap4.theme"); //$NON-NLS-1$
            }
        }
        return null;
    }

    @Override
    public InputStream getThemeFragmentAsStream(String themeId, int scope) {
        // No fragments are contributed
        return null;
    }

    private InputStream getThemeFromBundle(final String fileName) {
        ClassLoader cl = getClass().getClassLoader();
        return cl.getResourceAsStream(fileName);
    }
    
    @Override
    public String[] getThemeIds(){
        return new String[]{
            "Bootstrap4" // $NON-NLS-1$
        };
    }
}