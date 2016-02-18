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
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.component.layout.UIApplicationLayout;
import com.ibm.xsp.extlib.component.layout.impl.SearchBar;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.AbstractTreeRenderer;
import com.ibm.xsp.extlib.tree.ITree;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.tree.impl.TreeImpl;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.containers.tree.NavbarLinksRenderer;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;
import com.ibm.xsp.theme.bootstrap.components.layout.SimpleResponsiveApplicationConfiguration;
import com.ibm.xsp.theme.bootstrap.resources.Resources;
import com.ibm.xsp.util.FacesUtil;

public class SimpleResponsiveLayoutRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.layout.SimpleResponsiveLayoutRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            //Fixed banner padding
            case PROP_BANNER_FIXEDTOP_PADDING:       return "body {padding-top:3.375rem;} @media (min-width: 48em) {.applayout-main .sidebar{top:3.375rem;bottom:0rem;}}"; // $NON-NLS-1$
            case PROP_BANNER_FIXEDBOTTOM_PADDING:    return "body {padding-bottom:3.375rem;}  @media (min-width: 48em) {.applayout-main .sidebar{top:0rem;bottom:3.375rem;}}"; // $NON-NLS-1$
            case PROP_BANNER_COLLAPSE_CLASS:         return "navbar-collapse-target"; // $NON-NLS-1$
        
        }
        return super.getProperty(prop);
    }
    
    protected void writeLeftColumn(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration, 
            boolean collapseLeftColumn, String collapseLeftTarget, String collapseLeftButtonLabel) throws IOException {
        
        UIComponent left = c.getLeftColumn();
        if (!isEmptyComponent(left)) {          
            // Write the medium/ large screen css classes
            // if the collapseLeftColumn option is set, the large screen component is hidden on smaller screens
            w.startElement("div", c); // $NON-NLS-1$
            if (collapseLeftColumn) {
                w.writeAttribute("class", getLeftColumnClasses(collapseLeftColumn) + " hidden-sm-down applayout-column-left sidebar", null); // $NON-NLS-1$ $NON-NLS-2$
            } else {
                w.writeAttribute("class", getLeftColumnClasses(collapseLeftColumn) + " applayout-column-left sidebar", null); // $NON-NLS-1$ $NON-NLS-2$
            }
            
            FacesUtil.renderComponent(context, left);
            w.endElement("div"); // $NON-NLS-1$
            
            if (collapseLeftColumn) {
                // Write the small screen component (collapsed menu)
                w.startElement("script", c); // $NON-NLS-1$
                w.writeText("dojo.addOnLoad( function() { XTB.initCollapsibleMenu('" + collapseLeftButtonLabel + "', '" + collapseLeftTarget + "'); } );", null); // $NON-NLS-1$
                w.endElement("script"); // $NON-NLS-1$
            }
        }
    }
    
    @Override
    protected void writeNavbar(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration, 
            boolean navbarInverted, String navbarFixed, String pageWidthClass) throws IOException {
        
        String navbarFixedClass = "";
        if(StringUtil.isNotEmpty(navbarFixed)){
            if(navbarFixed.equals(SimpleResponsiveApplicationConfiguration.NAVBAR_FIXED_TOP)) {
                navbarFixedClass = "navbar-fixed-top"; // $NON-NLS-1$
            }else if(navbarFixed.equals(SimpleResponsiveApplicationConfiguration.NAVBAR_FIXED_BOTTOM)) {
                navbarFixedClass = "navbar-fixed-bottom"; // $NON-NLS-1$
            }else if(navbarFixed.equals(SimpleResponsiveApplicationConfiguration.NAVBAR_UNFIXED_TOP)) {
                navbarFixedClass = "navbar-static-top"; // $NON-NLS-1$
            }
        }
        
        w.startElement("div", c); // $NON-NLS-1$
        String navClass = ExtLibUtil.concatStyleClasses("navbar " + (navbarInverted ? "navbar-dark bg-inverse" : "navbar-default"), navbarFixedClass); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
        navClass = ExtLibUtil.concatStyleClasses(navClass, "applayout-banner"); // $NON-NLS-1$
        
        if(StringUtil.isNotEmpty(navClass)) {
            w.writeAttribute("class", navClass, null); // $NON-NLS-1$
        }
        
        //container div
        w.startElement("div",c); // $NON-NLS-1$
        String navbarClass = ExtLibUtil.concatStyleClasses(pageWidthClass, "applayout-banner-container"); // $NON-NLS-1$
        w.writeAttribute("class", navbarClass, null); // $NON-NLS-1$

        writeNavbarContent(context, w, c, configuration, navbarInverted);

        w.endElement("div"); // $NON-NLS-1$
        w.endElement("div"); // $NON-NLS-1$
    }

    @Override
    protected void writeNavbarContent(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration, boolean navbarInverted) throws IOException {
        //Write hidden div for attaching collapsible menus
        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", "applayout-banner-collapse", null); // $NON-NLS-1$ $NON-NLS-2$
        w.writeAttribute("style", "display:none", null); // $NON-NLS-1$ $NON-NLS-2$
        w.endElement("div"); // $NON-NLS-1$
        
        writeNavbarLink(context, w, c, configuration);
        
        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", ExtLibUtil.concatStyleClasses((String)getProperty(PROP_BANNER_COLLAPSE_CLASS), "collapse navbar-toggleable-sm"), null); // $NON-NLS-1$ $NON-NLS-2$
        
        writeNavbarBrand(context, w, c, configuration);
        writeNavbarApplicationLinks(context, w, c, configuration);
        writeNavbarUtilityLinks(context, w, c, configuration);
        writeSearchBar(context, w, c, configuration);
        w.endElement("div"); // $NON-NLS-1$
    }

    @Override
    protected void writeNavbarLink(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration) throws IOException {
        List<ITreeNode> utilLinks = configuration.getNavbarUtilityLinks();
        List<ITreeNode> appLinks  = configuration.getNavbarAppLinks();
        SearchBar searchBar       = configuration.getSearchBar();
        String navbarLogo         = configuration.getNavbarLogo();
        String navbarText         = configuration.getNavbarText();
        
        if( (navbarLogo != null && navbarLogo.length() > 0) ||
            (navbarText != null && navbarText.length() > 0) ||
            (utilLinks != null && utilLinks.size() > 0)     || 
            (appLinks != null && appLinks.size() > 0)       || 
            (searchBar != null && searchBar.isRendered())) {
            
            w.startElement("button", c); // $NON-NLS-1$
            w.writeAttribute("type",  "button",  null); // $NON-NLS-1$ $NON-NLS-2$
            
            //Add the necessary attributes to the button to make it a Bootstrap dropdown menu button
            w.writeAttribute("class", "navbar-toggler hidden-md-up", null); // $NON-NLS-1$ $NON-NLS-2$
            w.writeAttribute("data-toggle", "collapse", null); // $NON-NLS-1$ $NON-NLS-2$
            w.writeAttribute("data-target", "." + getProperty(PROP_BANNER_COLLAPSE_CLASS), null); // $NON-NLS-1$ $NON-NLS-2$

            //Add the unicode character for the 3 horizontal lines that make the button
            w.writeText('\u2630', null); // $NON-NLS-1$
            w.endElement("button"); // $NON-NLS-1$
        }
    }
    
    protected void writeNavbarBrand(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration) throws IOException {
        String logoImg  = configuration.getNavbarLogo();
        String navbarText = configuration.getNavbarText();
        
        if(StringUtil.isNotEmpty(logoImg) || StringUtil.isNotEmpty(navbarText)) {
            w.startElement("div",c); // $NON-NLS-1$
            
            String style = configuration.getNavbarLogoStyle();
            if(StringUtil.isNotEmpty(style)) {
                w.writeAttribute("style",style,null); // $NON-NLS-1$
            }
            
            //start brand div
            w.writeAttribute("class", "navbar-brand", null); // $NON-NLS-1$ $NON-NLS-2$
            
            if(StringUtil.isNotEmpty(logoImg)) {
                writeNavbarProductlogo(context, w, c, configuration);
            }
    
            if(StringUtil.isNotEmpty(navbarText)) {
                writeNavbarText(context, w, c, configuration, configuration.isInvertedNavbar());
            }
            
            //close brand div
            w.endElement("div"); // $NON-NLS-1$
        }
    }
    
    @Override
    protected void writeNavbarProductlogo(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration) throws IOException {
        String logoImg  = configuration.getNavbarLogo();
        String logoAlt  = configuration.getNavbarLogoAlt();
        
        w.startElement("div",c); // $NON-NLS-1$
        String style = configuration.getNavbarLogoStyle();
        if(StringUtil.isNotEmpty(style)) {
            w.writeAttribute("style",style,null); // $NON-NLS-1$
        }
        
        String clazz = ExtLibUtil.concatStyleClasses("navbar-brand-img", configuration.getNavbarLogoStyleClass()); // $NON-NLS-1$
        w.writeAttribute("class", clazz, null); // $NON-NLS-1$
        
        String imgSrc = HtmlRendererUtil.getImageURL(context, logoImg);
        w.startElement("img",c); // $NON-NLS-1$
        w.writeURIAttribute("src",imgSrc,null); // $NON-NLS-1$
   
            if(isAltNotEmpty(logoAlt)) {
                w.writeAttribute("alt",logoAlt,null); // $NON-NLS-1$
        }
        w.endElement("img"); // $NON-NLS-1$
        w.endElement("div"); // $NON-NLS-1$
    }

    @Override
    protected void writeNavbarText(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration, boolean navbarInverted) throws IOException {
        String navbarText = configuration.getNavbarText();
        
        w.startElement("div",c); // $NON-NLS-1$
                
        String clazz = ExtLibUtil.concatStyleClasses("navbar-brand-txt xspSimpleNavbarText", configuration.getNavbarTextStyleClass()); // $NON-NLS-1$
        if(StringUtil.isNotEmpty(clazz)) {
            w.writeAttribute("class", clazz, null); // $NON-NLS-1$
        }
        
        String titleStyle = configuration.getNavbarTextStyle();
        if (StringUtil.isNotEmpty(titleStyle)) {
            w.writeAttribute("style", titleStyle, null); // $NON-NLS-1$
        }
        
        w.writeText(navbarText, null);
        w.endElement("div"); // $NON-NLS-1$
    }

    @Override
    protected void writeNavbarApplicationLinks(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getNavbarAppLinks());
        if (tree != null) {
            AbstractTreeRenderer renderer = new NavbarLinksRenderer(NavbarLinksRenderer.POSITION_LEFT);
            if (renderer != null) {
                renderer.render(context, c, "al", tree, w); // $NON-NLS-1$
            }
        }
    }

    @Override
    protected void writeNavbarUtilityLinks(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getNavbarUtilityLinks());
        if (tree != null) {
            AbstractTreeRenderer renderer = new NavbarLinksRenderer(NavbarLinksRenderer.POSITION_RIGHT);
            if (renderer != null) {
                renderer.render(context, c, "ul", tree, w); // $NON-NLS-1$
            }
        }
    }
    
    private boolean isAltNotEmpty(String alt) {
        // Note, do not use StringUtil.isNotEmpty for alt text
        // because for accessibility reasons there's a difference
        // between alt="" and no alt attribute set,
        // so we treat null and "" as different for alt.
        return null != alt;
    }
    
    // ================================================================
    // Search Bar (normally part of the title bar)
    // ================================================================

    @Override
    protected void writeSearchBar(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration) throws IOException {
        UIComponent cSearchBar = c.getSearchBar();
        if (!isEmptyComponent(cSearchBar)) {
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","form-inline navbar-form pull-xs-right applayout-searchbar",null); // $NON-NLS-1$ $NON-NLS-2$
            w.writeAttribute("role", "search", null); // $NON-NLS-1$ $NON-NLS-2$
            FacesUtil.renderComponent(context, cSearchBar);
            w.endElement("div"); // $NON-NLS-1$
            return;
        }

        SearchBar searchBar = configuration.getSearchBar();
        if (searchBar != null && searchBar.isRendered()) {
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","form-inline navbar-form pull-xs-right applayout-searchbar",null); // $NON-NLS-1$ $NON-NLS-2$
            w.writeAttribute("role", "search", null); // $NON-NLS-1$ $NON-NLS-2$
            
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","input-group",null); // $NON-NLS-1$ $NON-NLS-2$
            
            boolean searchOptions = false;
            ITree tree = TreeImpl.get(searchBar.getOptions());
            if (tree != null) {
                searchOptions = true;
            }

            // Write the search options
            if (searchOptions) {
                writeSearchOptions(context, w, c, configuration, searchBar, tree);
            }
            
            // Write the search box
            writeSearchBox(context, w, c, configuration, searchBar, tree, searchOptions);
            writeSearchButton(context, w, c, configuration, searchBar, tree, searchOptions);

            w.endElement("div"); // $NON-NLS-1$
            w.endElement("div"); // $NON-NLS-1$
        }
    }

    @Override
    protected void writeSearchOptions(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration, SearchBar searchBar, ITree tree) throws IOException {
        AbstractTreeRenderer renderer = getSearchOptionsRenderer(context, w, c, configuration, searchBar);
        
        if (renderer != null) {
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","input-group-btn",null); // $NON-NLS-1$ $NON-NLS-2$
//            // Feels like a hack...
//            w.writeAttribute("style","width: 30%",null); // $NON-NLS-1$ $NON-NLS-2$
            renderer.render(context, c, "so", tree, w); // $NON-NLS-1$
            w.endElement("div"); // $NON-NLS-1$
        }
    }

    @Override
    protected void writeSearchButton(FacesContext context, ResponseWriter w, UIApplicationLayout c, SimpleResponsiveApplicationConfiguration configuration, SearchBar searchBar, ITree tree, boolean searchOptions) throws IOException {
         String submitSearch = "_xspAppSearchSubmit"; // $NON-NLS-1$
         
         w.startElement("div", c); // $NON-NLS-1$
         w.writeAttribute("class","input-group-btn",null); // $NON-NLS-1$ $NON-NLS-2$
         newLine(w);
         
         // Write the required script (done here because of Bootstrap 3 last-child selector on the input-group-btn)
         writeSearchScript(context, w, c, configuration, searchBar, tree, searchOptions);
        
         w.startElement("button",c); // $NON-NLS-1$
         w.writeAttribute("class","btn btn-secondary-outline applayout-searchbtn",null); // $NON-NLS-1$ $NON-NLS-2$
         w.writeAttribute("onclick","javascript:"+submitSearch+"(); return false;",null); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
         w.startElement("span",c); // $NON-NLS-1$
         w.writeAttribute("class", Resources.get().getIconClass("search"),null); // $NON-NLS-1$ $NON-NLS-2$
         w.endElement("span"); // $NON-NLS-1$
         w.endElement("button"); // $NON-NLS-1$
         
         w.endElement("div"); // $NON-NLS-1$
    }
    
//    @Override
//    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
//        ResponseWriter w = context.getResponseWriter();
//        UIApplicationLayout c = (UIApplicationLayout) component;
//        if (!c.isRendered()) {
//            return;
//        }
//        
//        ApplicationConfiguration _conf = c.findConfiguration();
//        if (!(_conf instanceof com.ibm.xsp.extlibx.theme.bootstrap4.components.layout.SimpleResponsiveApplicationConfiguration)) {
//            return;
//        }
//
//        com.ibm.xsp.extlibx.theme.bootstrap4.components.layout.SimpleResponsiveApplicationConfiguration configuration = (com.ibm.xsp.extlibx.theme.bootstrap4.components.layout.SimpleResponsiveApplicationConfiguration) _conf;
//        writeMainFrame(context, w, c, configuration);
//    }
}