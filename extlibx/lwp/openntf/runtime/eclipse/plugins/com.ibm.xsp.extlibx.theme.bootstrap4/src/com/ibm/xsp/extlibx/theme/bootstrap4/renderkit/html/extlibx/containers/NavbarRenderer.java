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
* Date: 08 Oct 2015
* NavbarRenderer.java
*/
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.containers;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.AbstractTreeRenderer;
import com.ibm.xsp.extlib.tree.ITree;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.containers.tree.NavbarLinksRenderer;
import com.ibm.xsp.theme.bootstrap.components.responsive.UINavbar;
import com.ibm.xsp.util.JavaScriptUtil;

/**
 *
 * @author Brian Gleeson (brian.gleeson@ie.ibm.com)
 */
public class NavbarRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.containers.NavbarRenderer {
    
    //TODO there are required changes in the super class in the core extlib to support
    //     this renderer. They make the BS3 NavbarRenderer more extensible. They need to be
    //     delivered before this version of the BS4 renderer can be released.
    
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            // Container div
            case PROP_CLASSCONTAINER:                return ""; //"xspNavbar"; //$NON-NLS-1$
            case PROP_STYLECONTAINER:                return ""; //$NON-NLS-1$

            case PROP_NAVBARDEFAULT:                 return "navbar-light bg-faded"; //$NON-NLS-1$
            case PROP_NAVBARINVERSE:                 return "navbar-dark bg-inverse"; //$NON-NLS-1$
            case PROP_NAVBARCHILDREN_WRAPPER:        return "nav navbar-nav xspNavbarContent"; //$NON-NLS-1$
            
            //Fixed navbar padding
            case PROP_NAVBARFIXEDTOP_PADDING:       return "body {padding-top:3.375rem;} @media (min-width: 768px) {.applayout-main .sidebar{top:3.375rem;bottom:0px;}}"; // $NON-NLS-1$
            case PROP_NAVBARFIXEDBOTTOM_PADDING:    return "body {padding-bottom:3.375rem;}  @media (min-width: 768px) {.applayout-main .sidebar{top:0px;bottom:3.375rem;}}"; // $NON-NLS-1$
            
            //Collapse classes
            case PROP_NAVBARCOLLAPSE_CONTAINER:      return "collapse navbar-toggleable-sm"; //$NON-NLS-1$

            case PROP_NAVBARITEM_PREFIX:             return "nav-"; //$NON-NLS-1$
            case PROP_NAVBARITEM_DEFAULTCLASS:       return "nav-item xspNavItem"; //$NON-NLS-1$
            
        }
        return super.getProperty(prop);
    }
    
    protected void writeHeading(FacesContext context, ResponseWriter w, UINavbar c, boolean linksExist) throws IOException {
        if(linksExist) {
            writeCollapsedLink(context, w, c);
        }
        
        // start collapse container div
        w.startElement("div", c); // $NON-NLS-1$
        // SPR #BGLN9ZCMXK Custom collapse class for each navbar needed
        //w.writeAttribute("class", "collapse navbar-collapse " + JavaScriptUtil.encodeFunctionName(context, component, "collapse-target"), null); // $NON-NLS-1$ $NON-NLS-2$ //$NON-NLS-3$
        String collapseClass  = (String)getProperty(PROP_NAVBARCOLLAPSE_CONTAINER);
        String collapseTarget = (Boolean)getProperty(PROP_NAVBARCOLLAPSE_TARGET) ? JavaScriptUtil.encodeFunctionName(context, c, "collapse-target") : ""; // $NON-NLS-1$ $NON-NLS-2$
        collapseClass = ExtLibUtil.concatStyleClasses(collapseClass, collapseTarget);
        
        if(StringUtil.isNotEmpty(collapseClass)) {
            w.writeAttribute("class", collapseClass, null); // $NON-NLS-1$
        }
        
        // write navbar brand
        writeBrand(context, w, c);
    }
    
    protected void writeBrand(FacesContext context, ResponseWriter w, UINavbar c) throws IOException {
        String headingText  = c.getHeadingText();
        String headingStyle = c.getHeadingStyle();
        String headingClass = c.getHeadingStyleClass();
        
        //start header div
        //TODO div or link?
        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", "navbar-brand", null); // $NON-NLS-1$ $NON-NLS-2$
        
        // start brand div
        if(StringUtil.isNotEmpty(headingText)) {
            w.startElement("div", c); // $NON-NLS-1$
            String headerClazz = ExtLibUtil.concatStyleClasses("", headingClass); // $NON-NLS-1$
            if(StringUtil.isNotEmpty(headerClazz)) {
                w.writeAttribute("class", headerClazz, null); // $NON-NLS-1$
            }
            if(StringUtil.isNotEmpty(headingStyle)) {
                w.writeAttribute("style", headingStyle, null); // $NON-NLS-1$
            }
            w.writeText(headingText, null); // $NON-NLS-1$
            
            //end brand div
            w.endElement("div"); // $NON-NLS-1$
        }
        // close header div
        w.endElement("div"); // $NON-NLS-1$
    }
    
    protected void writeBeforeLinks(FacesContext context, ResponseWriter w, UINavbar c, ITree beforeLinks) throws IOException {
        if(null != beforeLinks) {
            AbstractTreeRenderer renderer = new NavbarLinksRenderer(NavbarLinksRenderer.POSITION_LEFT);
            if (renderer != null) {
                renderer.render(context, c, "navbar_left", beforeLinks, w); // $NON-NLS-1$
            }
        }
    }
    
    protected void writeAfterLinks(FacesContext context, ResponseWriter w, UINavbar c, ITree afterLinks) throws IOException {
        if(null != afterLinks) {
            AbstractTreeRenderer renderer = new NavbarLinksRenderer(NavbarLinksRenderer.POSITION_RIGHT);
            if (renderer != null) {
                renderer.render(context, c, "navbar_right", afterLinks, w); // $NON-NLS-1$
            }
        }
    }
    
    protected void writeCollapsedLink(FacesContext context, ResponseWriter w, UINavbar c) throws IOException {
        String dataTargetClass = "." + JavaScriptUtil.encodeFunctionName(context, c, "collapse-target"); //$NON-NLS-1$ //$NON-NLS-2$
        w.startElement("button", c); // $NON-NLS-1$
        w.writeAttribute("type",  "button",  null); // $NON-NLS-1$ $NON-NLS-2$
        
        //Add the necessary attributes to the button to make it a Bootstrap dropdown menu button
        w.writeAttribute("class", "navbar-toggler hidden-md-up", null); // $NON-NLS-1$ $NON-NLS-2$
        w.writeAttribute("data-toggle", "collapse", null); // $NON-NLS-1$ $NON-NLS-2$
        w.writeAttribute("data-target", dataTargetClass, null); // $NON-NLS-1$ $NON-NLS-2$
        
        //Add the unicode character for the 3 horizontal lines that make the button
        w.writeText('\u2630', null);
        w.endElement("button"); // $NON-NLS-1$
    }
}
