/*
 * © Copyright IBM Corp. 2013
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
* Author: Maire Kehoe (mkehoe@ie.ibm.com)
* Date: 28 Sep 2011
* ExtlibXViewSerializeTest.java
*/

package xsp.extlibx.test.serialize;

import com.ibm.xsp.test.framework.XspTestUtil;
import com.ibm.xsp.test.framework.serialize.BaseViewSerializeTest;

/**
 *
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 */
public class ExtlibXViewSerializeTest extends BaseViewSerializeTest {

    private String[] _skipFails = new String[]{
    };

    @Override
    protected Object[][] getCompareSkips() {
        Object[][] skips = super.getCompareSkips();
//        skips = XspTestUtil.concat(skips, getCompareSkips_MethodBindingEx());
        skips = XspTestUtil.concat(skips, getCompareSkips_UIViewRootEx());
//        skips = XspTestUtil.concat(skips, getCompareSkips_UIRepeat());
//        skips = XspTestUtil.concat(skips, getCompareSkips_UIInputEx());
        return skips;
    }
    
    @Override
    protected String[] getSkipFails() {
        String[] arr = _skipFails;
        return arr ;
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.test.framework.serialize.ViewSerializeTest#normalizeMsg(java.lang.String)
     */
    @Override
    protected String normalizeExMsg(String msg) {
        msg = super.normalizeExMsg(msg);
        
        if( msg.contains("Could not set the property value on the object") ){
            msg = msg.replaceAll("@[0-9a-f]+>", "@????>");
        }
        
        return msg;
    }
    
}
