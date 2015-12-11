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
* Date: 05 Nov 2015
* Bootstrap4RenderThemeControlTest.java
*/
package xsp.extlibx.test.render;

import java.util.Arrays;
import java.util.List;

import com.ibm.xsp.extlibx.theme.bootstrap4.themes.Bootstrap4StyleKitFactory;
import com.ibm.xsp.test.framework.XspTestUtil;
import com.ibm.xsp.test.framework.render.BaseRenderThemeControlTest;

public class Bootstrap4RenderThemeControlTest  extends BaseRenderThemeControlTest {
    @Override
    protected String[][] getExtraConfig() {
        String[][] extra = super.getExtraConfig();
        extra = XspTestUtil.concat(extra, new String[][]{
                // Test all controls in the registry, not just the ExtLib controls
                {"target.all", "true"},
                {"target.library", null},
                // also test the _blue, etc files. [Not tested by default]
                {"RenderThemeControl.ignoreFilesWithUnderscore", "true"},
                // also test the android, iphone, blackberry themes [Not tested by default]
                {"RenderThemeControlTest.requireMobileThemes", "false"},
                // the list of libraries whose faces-config.xml files should be loaded
                {"extra.library.depends.runtime", "com.ibm.xsp.extlib.library"},
        });
        return extra;
    }
    
    @Override
    protected List<String> computeContributedThemes(List<String> themeFileIds) {
        // call to superclass verifies mobile themes are contributed (and finds all contributed themes, unused)
        super.computeContributedThemes(themeFileIds);
        
        // only test the Bootstrap4 themes, not all the contributed themes.
        // Also testing null theme (defaults to webstandard), and oneuiv2.1.
        // Testing null so can see if the any exceptions are also present in webstandard.
        // Testing oneuiv2.1 so the system.out.printlns show it as contrast to the mobile themes.
        List<String> defaultBootstrap4Themes = Arrays.asList((new Bootstrap4StyleKitFactory()).getThemeIds());
        
        return defaultBootstrap4Themes;
    }
    
    @Override
    protected void validateContributedThemes(List<String> contributedThemes, List<String> fileSysThemeFileIds) {
        if(contributedThemes.isEmpty() ){
            throw new RuntimeException("Bad project configuration");
        }
        int minimumExpected = 0;
        if( contributedThemes.size() < minimumExpected ){
            throw new RuntimeException(
                    "Number of contributed themes is less than expected "
                            + minimumExpected + ", was " + contributedThemes.size());
        }
    }


}
