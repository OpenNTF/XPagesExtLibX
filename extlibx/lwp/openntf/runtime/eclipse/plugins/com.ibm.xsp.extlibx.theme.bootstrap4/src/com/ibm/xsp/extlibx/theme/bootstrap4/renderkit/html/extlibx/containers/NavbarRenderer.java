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
import java.lang.reflect.Method;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.AbstractTreeRenderer;
import com.ibm.xsp.extlib.tree.ITree;
import com.ibm.xsp.extlib.tree.impl.TreeImpl;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.theme.bootstrap.BootstrapLogger;
import com.ibm.xsp.theme.bootstrap.components.responsive.UINavbar;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.containers.tree.NavbarLinksRenderer;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.HtmlUtil;
import com.ibm.xsp.util.JavaScriptUtil;

/**
 *
 * @author Brian Gleeson (brian.gleeson@ie.ibm.com)
 */
public class NavbarRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.containers.NavbarRenderer {

    //Container
    public static final int PROP_CLASSCONTAINER                 = 1;
    public static final int PROP_STYLECONTAINER                 = 2;
    
    //TODO refactor super class to make navbar padding tob/bottom overridable
    public static final int PROP_NAVBAR_FIXEDTOP_PADDING        = 10;
    public static final int PROP_NAVBAR_FIXEDBOTTOM_PADDING     = 11;
    //TODO similarly extract out to properties a number of the other strings
    //in the super class
    
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            // Container div
            case PROP_CLASSCONTAINER:                return ""; //"xspNavbar"; //$NON-NLS-1$
            case PROP_STYLECONTAINER:                return ""; //$NON-NLS-1$
            //Fixed banner padding
            case PROP_NAVBAR_FIXEDTOP_PADDING:       return "body {padding-top:3.375rem;} @media (min-width: 768px) {.applayout-main .sidebar{top:3.375rem;bottom:0px;}}"; // $NON-NLS-1$
            case PROP_NAVBAR_FIXEDBOTTOM_PADDING:    return "body {padding-bottom:3.375rem;}  @media (min-width: 768px) {.applayout-main .sidebar{top:0px;bottom:3.375rem;}}"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
    
    // ================================================================
    // Nav Bar
    // ================================================================
    protected void writeNavbar(FacesContext context, ResponseWriter w, UINavbar component) throws IOException{
        boolean inverted    = false;
        String fixed        = "";
        String style        = "";
        String styleClass   = "";
        String title        = "";
        String pageWidth    = null;
        boolean hasChildren = false;
        ITree beforeLinks   = null;
        ITree afterLinks    = null;

        if(component!=null) {
            inverted    = component.isInverted();
            fixed       = component.getFixed();
            styleClass  = component.getStyleClass();
            style       = component.getStyle();
            title       = component.getTitle();
            pageWidth   = component.getPageWidth();
            hasChildren = component.getChildCount() > 0;
            beforeLinks = TreeImpl.get(component.getNavbarBeforeLinks());
            afterLinks  = TreeImpl.get(component.getNavbarAfterLinks());
        }
        
        // write navbar container
        w.startElement("div", component); // $NON-NLS-1$
        
        if(HtmlUtil.isUserId(component.getId())) {
            String clientId = component.getClientId(context);
            w.writeAttribute("id", clientId, null); // $NON-NLS-1$ $NON-NLS-2$
        }

        if(StringUtil.isNotEmpty(title)) {
            w.writeAttribute("title", title, null); // $NON-NLS-1$
        }else{
            w.writeAttribute("title", "navigation bar", null); // $NON-NLS-1$ $NLS-NavbarRenderer.navigationbar-2$
        }
        
        String role = "navigation"; // $NON-NLS-1$
        w.writeAttribute("role", role, null); // $NON-NLS-1$

        String fixedClass = "";
        if(!StringUtil.isEmpty(fixed)){
            if(fixed.equals(UINavbar.NAVBAR_FIXED_TOP)) {
                fixedClass = "navbar-fixed-top"; // $NON-NLS-1$
            }else if(fixed.equals(UINavbar.NAVBAR_FIXED_BOTTOM)) {
                fixedClass = "navbar-fixed-bottom"; // $NON-NLS-1$
            }else if(fixed.equals(UINavbar.NAVBAR_UNFIXED_TOP)) {
                fixedClass = "navbar-static-top"; // $NON-NLS-1$
            }else{
                fixedClass = "";
            }
        }
        
        String navClass = ExtLibUtil.concatStyleClasses("navbar " + (inverted ? "navbar-dark bg-inverse " : "navbar-default ") + fixedClass, styleClass); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
        String containerMixinClass = ExtLibUtil.concatStyleClasses(navClass, (String)getProperty(PROP_CLASSCONTAINER));
        if(StringUtil.isNotEmpty(containerMixinClass)) {
            w.writeAttribute("class", containerMixinClass, null); // $NON-NLS-1$
        }
        String containerMixinStyle = ExtLibUtil.concatStyleClasses((String)getProperty(PROP_STYLECONTAINER), style);
        if(StringUtil.isNotEmpty(containerMixinStyle)) {
            w.writeAttribute("style", containerMixinStyle, null); // $NON-NLS-1$
        }
        
        //CSS required for fixed navbar
        if (!StringUtil.isEmpty(fixed)) {
            if(fixed.equals(UINavbar.NAVBAR_FIXED_TOP) || fixed.equals(UINavbar.NAVBAR_FIXED_BOTTOM)) {
                w.startElement("style", component); // $NON-NLS-1$
                w.writeAttribute("type", "text/css", null); // $NON-NLS-1$ $NON-NLS-2$
                if(fixed.equals(UINavbar.NAVBAR_FIXED_TOP)) {
                    w.writeText(getProperty(PROP_NAVBAR_FIXEDTOP_PADDING), null); // $NON-NLS-1$
                }else if(fixed.equals(UINavbar.NAVBAR_FIXED_BOTTOM)){ // $NON-NLS-1$
                    w.writeText(getProperty(PROP_NAVBAR_FIXEDBOTTOM_PADDING), null); // $NON-NLS-1$
                }else{
                    // don't write any styles
                }
                w.endElement("style"); // $NON-NLS-1$
            }
        }
        
        //container div
        w.startElement("div", component); // $NON-NLS-1$
        if ( pageWidth != null) {
            if (pageWidth.equals(UINavbar.WIDTH_FLUID)) {
                w.writeAttribute("class", "container-fluid", null); // $NON-NLS-1$ $NON-NLS-2$
            } else if (pageWidth.equals(UINavbar.WIDTH_FIXED)) {
                w.writeAttribute("class", "container", null); // $NON-NLS-1$ $NON-NLS-2$
            } else if ( pageWidth.equals(UINavbar.WIDTH_FULL)) {
                w.writeAttribute("class", "container-full", null); // $NON-NLS-1$ $NON-NLS-2$
            } else if ( pageWidth.equals(UINavbar.WIDTH_NONE)) {
                // don't write a container class to the div
            } else {
                // default to container-fluid
                w.writeAttribute("class", "container-fluid", null); // $NON-NLS-1$ $NON-NLS-2$
            }
        } else {
            // default to container-fluid
            w.writeAttribute("class", "container-fluid", null); // $NON-NLS-1$ $NON-NLS-2$
        }

        //Write hidden div for attaching collapsible menus
        boolean collapsible = hasChildren || beforeLinks != null || afterLinks != null;
        if(collapsible) {
            writeCollapsedLink(context, w, component);
        }
        
        // start collapse container div
        w.startElement("div", component); // $NON-NLS-1$
        // SPR #BGLN9ZCMXK Custom collapse class for each navbar needed
        w.writeAttribute("class", "collapse navbar-toggleable-sm " + JavaScriptUtil.encodeFunctionName(context, component, "collapse-target"), null); // $NON-NLS-1$ $NON-NLS-2$ //$NON-NLS-3$

        // write navbar-header
        writeBrand(context, w, component);

        // write navbar before links
        if(beforeLinks != null) {
            writeBeforeLinks(context, w, component, beforeLinks);
        }
        // write navbar children
        if(hasChildren && getRendersChildren()) {
            w.startElement("div", component); // $NON-NLS-1$
            w.writeAttribute("class", "nav navbar-nav xspNavbarContent", null); // $NON-NLS-1$ $NON-NLS-2$
            renderChildren(context, w, component);
            w.endElement("div"); // $NON-NLS-1$
        }
        // write navbar after links
        if(afterLinks != null) {
            writeAfterLinks(context, w, component, afterLinks);
        }
        
        // close collapse container div
        w.endElement("div"); // $NON-NLS-1$

        // close container div
        w.endElement("div"); // $NON-NLS-1$
        
        // Close the main frame
        w.endElement("div"); // $NON-NLS-1$
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
    
    /**
     * Render the children of the navbar
     * @designer.publicmethod
     */
    public void renderChildren(FacesContext context, ResponseWriter w, UIComponent component) throws IOException {
        // encode component and children
        // for children of the navbar we need to add a CSS class
        
        int count = component.getChildCount();
        if(count>0) {
            List<?> children = component.getChildren();
            for (int i=0; i<count; i++) {
                Object child = children.get(i);
                boolean addDiv = false;
                
                try{
                    //Determine if the child has a 'navbar-' CSS class assigned already
                    Method method = child.getClass().getMethod("getStyleClass", (Class[])null); // $NON-NLS-1$
                    Object result = method.invoke(child, (Object[])null);
                    String styleClass = (result != null) ? (String)result : null;
                    if(styleClass != null && styleClass.contains("navbar-")){ // $NON-NLS-1$
                        //navbar class already assigned, don't add anything
                        addDiv = false;
                        
                    }else{
                        //no navbar class assigned, wrap the control in a div with class 'nav-item'
                        addDiv = true;
                        w.startElement("div", component); // $NON-NLS-1$
                        w.writeAttribute("class", "nav-item xspNavItem", null); // $NON-NLS-1$ $NON-NLS-2$
                    }
                } catch (Exception e) {
                    if(BootstrapLogger.BOOTSTRAP.isErrorEnabled()) {
                        BootstrapLogger.BOOTSTRAP.errorp(this, "renderChildren", e, "Exception occured while rendering Navbar children"); // $NON-NLS-1$ $NLX-NavbarRenderer.ExceptionoccuredwhilstrenderingNa-2$
                    }
                }
                
                UIComponent compChild = (UIComponent)child;
                FacesUtil.renderComponent(context, compChild);
                if(addDiv) {
                    //close containing div
                    w.endElement("div"); // $NON-NLS-1$
                }
            }
        }
    }
}
