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
* Date: 02 Nov 2015
* ResponsiveAppLayoutRenderer.java
*/
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.component.layout.UIApplicationLayout;
import com.ibm.xsp.extlib.component.layout.impl.BasicApplicationConfigurationImpl;
import com.ibm.xsp.extlib.component.layout.impl.SearchBar;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.AbstractTreeRenderer;
import com.ibm.xsp.extlib.tree.ITree;
import com.ibm.xsp.extlib.tree.impl.TreeImpl;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout.tree.ApplicationLinksRenderer;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout.tree.FooterLinksRenderer;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout.tree.PlaceBarActionsRenderer;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout.tree.TitleBarTabsRenderer;
import com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout.tree.UtilityLinksRenderer;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;
import com.ibm.xsp.theme.bootstrap.components.layout.ResponsiveApplicationConfiguration;
import com.ibm.xsp.theme.bootstrap.resources.Resources;
import com.ibm.xsp.util.FacesUtil;

public class ResponsiveAppLayoutRenderer extends  com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.layout.ResponsiveAppLayoutRenderer {
    
    public static final int PROP_COLUMN_EXTRALARGE = 5;
    
    @Override
    protected Object getProperty(int prop) {
        switch (prop) {
            // Grid sizes
            case PROP_COLUMN_EXTRALARGE:             return "col-xl-"; // $NON-NLS-1$
            //Fixed banner padding
            case PROP_BANNER_FIXEDTOP_PADDING:       return "body {padding-top:3.375rem;} @media (min-width: 48em) {.applayout-main .sidebar{top:3.375rem;bottom:0rem;}}"; // $NON-NLS-1$
            case PROP_BANNER_FIXEDBOTTOM_PADDING:    return "body {padding-bottom:3.375rem;}  @media (min-width: 48em) {.applayout-main .sidebar{top:0rem;bottom:3.375rem;}}"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
    
    // ================================================================
    // Banner
    // ================================================================
    @Override
    protected void writeBanner(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration, 
            String pageWidthClass, boolean navbarInverted, String navbarFixed) throws IOException {
        
        String navbarFixedClass = "";
        if(!StringUtil.isEmpty(navbarFixed)){
            if(navbarFixed.equals(ResponsiveApplicationConfiguration.NAVBAR_FIXED_TOP)) {
                navbarFixedClass = "navbar-fixed-top"; // $NON-NLS-1$
            }else if(navbarFixed.equals(ResponsiveApplicationConfiguration.NAVBAR_FIXED_BOTTOM)) {
                navbarFixedClass = "navbar-fixed-bottom"; // $NON-NLS-1$
            }else if(navbarFixed.equals(ResponsiveApplicationConfiguration.NAVBAR_UNFIXED_TOP)) {
                navbarFixedClass = "navbar-static-top"; // $NON-NLS-1$
            }
        }
        
        w.startElement("div", c); // $NON-NLS-1$
        String navClass = ExtLibUtil.concatStyleClasses("navbar", navbarFixedClass); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
        String invertedClass = navbarInverted ? "navbar-dark bg-inverse" : "navbar-default"; // $NON-NLS-1$ $NON-NLS-2$
        navClass = ExtLibUtil.concatStyleClasses(navClass, invertedClass); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
        navClass = ExtLibUtil.concatStyleClasses(navClass, "applayout-banner"); // $NON-NLS-1$
        
        if(StringUtil.isNotEmpty(navClass)) {
            w.writeAttribute("class", navClass, null); // $NON-NLS-1$
        }
        
        //container div
        w.startElement("div",c); // $NON-NLS-1$
        String navbarClass = ExtLibUtil.concatStyleClasses(pageWidthClass, "applayout-banner-container"); // $NON-NLS-1$
        w.writeAttribute("class", navbarClass, null); // $NON-NLS-1$

        writeBannerContent(context, w, c, configuration);

        w.endElement("div"); // $NON-NLS-1$
        w.endElement("div"); // $NON-NLS-1$
    }
    
    @Override
    protected void writeBannerContent(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        boolean hasChildren = c.getChildCount() > 0;
        ITree appLinks      = TreeImpl.get(configuration.getBannerApplicationLinks());
        ITree utilityLinks  = TreeImpl.get(configuration.getBannerUtilityLinks());
        String productLogo  = configuration.getProductLogo();
        SearchBar searchBar = configuration.getSearchBar();
        boolean bannerHasContent = hasChildren || appLinks != null || utilityLinks != null || productLogo != null || searchBar != null;
        
        if(bannerHasContent) {
            writeBannerLink(context, w, c, configuration);
        }
        
        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", ExtLibUtil.concatStyleClasses((String)getProperty(PROP_BANNER_COLLAPSE_CLASS), "collapse navbar-toggleable-sm"), null); // $NON-NLS-1$ $NON-NLS-2$
        
        writeBannerProductlogo(context, w, c, configuration);
        writeBannerApplicationLinks(context, w, c, configuration);
        writeBannerUtilityLinks(context, w, c, configuration);
        w.endElement("div"); // $NON-NLS-1$
    }
    
    @Override
    protected void writeBannerLink(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        w.startElement("button", c); // $NON-NLS-1$
        w.writeAttribute("type", "button", null); // $NON-NLS-1$ $NON-NLS-2$
        
        //Add the necessary attributes to the button to make it a Bootstrap dropdown menu button
        w.writeAttribute("class", "navbar-toggler hidden-md-up", null); // $NON-NLS-1$ $NON-NLS-2$
        w.writeAttribute("data-toggle", "collapse", null); // $NON-NLS-1$ $NON-NLS-2$
        w.writeAttribute("data-target", "." + getProperty(PROP_BANNER_COLLAPSE_CLASS), null); // $NON-NLS-1$ $NON-NLS-2$
        
        //Add the unicode character for the 3 horizontal lines that make the button
        w.writeText('\u2630', null); // $NON-NLS-1$
       w.endElement("button"); // $NON-NLS-1$
    }
    
    @Override
    protected void writeBannerProductlogo(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        String logoImg = configuration.getProductLogo();
        String logoAlt = configuration.getProductLogoAlt();
        
        //start brand div
        w.startElement("div",c); // $NON-NLS-1$

        String style = configuration.getProductLogoStyle();
        if(StringUtil.isNotEmpty(style)) {
            w.writeAttribute("style",style,null); // $NON-NLS-1$
        }

        String clazz = ExtLibUtil.concatStyleClasses("navbar-brand", configuration.getProductLogoClass()); // $NON-NLS-1$
        w.writeAttribute("class", clazz, null); // $NON-NLS-1$
        
        if(StringUtil.isNotEmpty(logoImg)) {
            String imgSrc = HtmlRendererUtil.getImageURL(context, logoImg);
            w.startElement("img",c); // $NON-NLS-1$
            w.writeURIAttribute("src",imgSrc,null); // $NON-NLS-1$
   
            if(!isAltNotEmpty(logoAlt)) {
                logoAlt = "Banner product logo"; // $NLS-AbstractApplicationLayoutRenderer.BannerProductLogo-1$
            }
            w.writeAttribute("alt",logoAlt,null); // $NON-NLS-1$
            String width = configuration.getProductLogoWidth();
            if(StringUtil.isNotEmpty(width)) {
                w.writeAttribute("width",width,null); // $NON-NLS-1$
            }
            String height = configuration.getProductLogoHeight();
            if(StringUtil.isNotEmpty(height)) {
                w.writeAttribute("height",height,null); // $NON-NLS-1$
            }
            w.endElement("img"); // $NON-NLS-1$
        } else if ( StringUtil.isNotEmpty( logoAlt) ) {
            w.writeText(logoAlt, null); // $NON-NLS-1$
        }
        w.endElement("div"); // $NON-NLS-1$
    }

    @Override
    protected void writeBannerApplicationLinks(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getBannerApplicationLinks());
        if (tree != null) {
            AbstractTreeRenderer renderer = new ApplicationLinksRenderer();
            if (renderer != null) {
                renderer.render(context, c, "al", tree, w); // $NON-NLS-1$
            }
        }
    }

    @Override
    protected void writeBannerUtilityLinks(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getBannerUtilityLinks());
        if (tree != null) {
            AbstractTreeRenderer renderer = new UtilityLinksRenderer();
            if (renderer != null) {
                renderer.render(context, c, "ul", tree, w); // $NON-NLS-1$
            }
        }
    }

    // ================================================================
    // Title Bar
    // ================================================================
    @Override
    protected void writeTitleBar(FacesContext context, ResponseWriter w, UIApplicationLayout c, 
            BasicApplicationConfigurationImpl configuration, String pageWidthClass) throws IOException {
        ITree tree = TreeImpl.get(configuration.getTitleBarTabs());
        SearchBar searchBar = configuration.getSearchBar();
        String titleBarName = configuration.getTitleBarName();
        
        //If there is no titleBarName, seachbar or tabs to be displayed, dont render the titleBar
        if (StringUtil.isNotEmpty(titleBarName) || tree != null || (searchBar != null && searchBar.isRendered())) {
            w.startElement("div", c); // $NON-NLS-1$
            
            //Check if the titlebar has tabs. If none, add bottom border
            if (tree != null) {
                w.writeAttribute("class", "navbar navbar-static-top applayout-titlebar", null); // $NON-NLS-1$ $NON-NLS-2$
            }else{
                w.writeAttribute("class", "navbar navbar-static-top applayout-titlebar applayout-titlebar-border", null); // $NON-NLS-1$ $NON-NLS-2$
            }
            newLine(w);
            
            //container div
            w.startElement("div", c); // $NON-NLS-1$
            String titleClass = ExtLibUtil.concatStyleClasses(pageWidthClass, "applayout-titlebar-inner"); // $NON-NLS-1$
            w.writeAttribute("class", titleClass , null); // $NON-NLS-1$
            
            writeSearchBar(context, w, c, configuration);
            
            if( StringUtil.isNotEmpty(titleBarName)) {
                // #Bootstrap4 Add navbar-brand div around TitleBarName
                w.startElement("div", c); // $NON-NLS-1$
                w.writeAttribute("class","navbar-brand",null); // $NON-NLS-1$ $NON-NLS-2$
                
                w.startElement("h4",c); //$NON-NLS-1$
                if (tree != null) {
                    w.writeAttribute("class","applayout-titlebar-name",null); // $NON-NLS-1$ $NON-NLS-2$
                }else{
                    w.writeAttribute("class","applayout-titlebar-name applayout-titlebar-name-padding",null); // $NON-NLS-1$ $NON-NLS-2$
                }
                
                w.writeAttribute("title",titleBarName,null); // $NON-NLS-1$
                w.write(titleBarName);
                w.endElement("h4"); //$NON-NLS-1$
                w.endElement("div"); // $NON-NLS-1$
            }
    
            writeTitleBarTabsArea(context, w, c, configuration);
    
            // Close the banner
            w.endElement("div"); // $NON-NLS-1$
            w.endElement("div"); // $NON-NLS-1$
        }
    }
    
    @Override
    protected void writeTitleBarTabsArea(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getTitleBarTabs());
        if (tree != null) {
            AbstractTreeRenderer renderer = new TitleBarTabsRenderer();
            if (renderer != null) {
                //Write containing div
                w.startElement("div", c); // $NON-NLS-1$
                w.writeAttribute("class", "applayout-titlebar-tabsarea", null); // $NON-NLS-1$ $NON-NLS-2$
                // Write the tabs
                writeTitleBarTabs(context, w, c, configuration, tree, renderer);
                w.endElement("div"); // $NON-NLS-1$
            }
        }
    }
    
    @Override
    protected void writeTitleBarTabs(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration, ITree tree, AbstractTreeRenderer renderer) throws IOException {
        renderer.render(context, c, "tb", tree, w); // $NON-NLS-1$
    }

    // ================================================================
    // Search Bar (normally part of the title bar)
    // ================================================================
    @Override
    protected void writeSearchBar(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        UIComponent cSearchBar = c.getSearchBar();
        if (!isEmptyComponent(cSearchBar)) {
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","form-inline navbar-form pull-right applayout-searchbar",null); // $NON-NLS-1$ $NON-NLS-2$
            w.writeAttribute("role", "search", null); // $NON-NLS-1$ $NON-NLS-2$
            FacesUtil.renderComponent(context, cSearchBar);
            w.endElement("div"); // $NON-NLS-1$
            return;
        }

        SearchBar searchBar = configuration.getSearchBar();
        if (searchBar != null && searchBar.isRendered()) {
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","form-inline navbar-form pull-right applayout-searchbar",null); // $NON-NLS-1$ $NON-NLS-2$
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
    protected void writeSearchOptions(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration, SearchBar searchBar, ITree tree) throws IOException {
        AbstractTreeRenderer renderer = getSearchOptionsRenderer(context, w, c, configuration, searchBar);
        if (renderer != null) {
            w.startElement("div", c); // $NON-NLS-1$
            w.writeAttribute("class","input-group-btn",null); // $NON-NLS-1$ $NON-NLS-2$
            
            renderer.render(context, c, "so", tree, w); // $NON-NLS-1$
            w.endElement("div"); // $NON-NLS-1$
        }
    }
    
    @Override
    protected void writeSearchButton(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration, SearchBar searchBar, ITree tree, boolean searchOptions) throws IOException {
         String submitSearch = "_xspAppSearchSubmit"; // $NON-NLS-1$
         
         w.startElement("div", c); // $NON-NLS-1$
         w.writeAttribute("class","input-group-btn",null); // $NON-NLS-1$ $NON-NLS-2$
         newLine(w);
         
         // Write the required script (done here because of Bootstrap 3 last-child selector on the input-group-btn)
         writeSearchScript(context, w, c, configuration, searchBar, tree, searchOptions);
         newLine(w);
        
         w.startElement("button",c); // $NON-NLS-1$
         w.writeAttribute("class","btn btn-secondary-outline applayout-searchbtn",null); // $NON-NLS-1$ $NON-NLS-2$
         w.writeAttribute("onclick","javascript:"+submitSearch+"(); return false;",null); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
         w.startElement("span",c); // $NON-NLS-1$
         w.writeAttribute("class", Resources.get().getIconClass("search"),null); // $NON-NLS-1$ $NON-NLS-2$
         w.endElement("span"); // $NON-NLS-1$
         w.endElement("button"); // $NON-NLS-1$
         
         w.endElement("div"); // $NON-NLS-1$
    }
    
    // ================================================================
    // Place Bar
    // ================================================================
    @Override
    protected void writePlaceBar(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration, String pageWidthClass) throws IOException {
        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", "navbar navbar-static-top applayout-placebar", null); // $NON-NLS-1$ $NON-NLS-2$

        //container div
        w.startElement("div", c); // $NON-NLS-1$
        if(StringUtil.isNotEmpty(pageWidthClass)) {
            w.writeAttribute("class", pageWidthClass, null); // $NON-NLS-1$
        }

        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", "navbar-brand applayout-placebar-title", null); // $NON-NLS-1$ $NON-NLS-2$
        writePlaceBarName(context, w, c, configuration);
        UIComponent cPlaceBarName = c.getPlaceBarName();
        if (!isEmptyComponent(cPlaceBarName)) {
            FacesUtil.renderComponent(context, cPlaceBarName);
        }
        w.endElement("div"); // $NON-NLS-1$

        w.startElement("div", c); // $NON-NLS-1$
        w.writeAttribute("class", "nav navbar-nav pull-right applayout-placebar-actions", null); // $NON-NLS-1$ $NON-NLS-2$
        writePlaceBarActions(context, w, c, configuration);
        UIComponent cPlaceBarActions = c.getPlaceBarActions();
        if (!isEmptyComponent(cPlaceBarActions)) {
            w.startElement("div", c); // $NON-NLS-1$
            FacesUtil.renderComponent(context, cPlaceBarActions);
            w.endElement("div"); // $NON-NLS-1$
        }
        w.endElement("div"); // $NON-NLS-1$

        // Close the banner
        w.endElement("div"); // $NON-NLS-1$
        w.endElement("div"); // $NON-NLS-1$
    }
    
    @Override
    protected void writePlaceBarName(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        String placeName = configuration.getPlaceBarName();
        if (StringUtil.isNotEmpty(placeName)) {
            String placeBarNameTag = "h4"; // $NON-NLS-1$
            w.startElement(placeBarNameTag, c);
            w.writeText(placeName, null);
            w.endElement(placeBarNameTag);
            newLine(w);
        }
    }
    
    @Override
    protected void writePlaceBarActions(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getPlaceBarActions());
        if (tree != null) {
            AbstractTreeRenderer renderer = new PlaceBarActionsRenderer();
            if (renderer != null) {
                renderer.render(context, c, "pb", tree, w); // $NON-NLS-1$
            }
        }
    }

    @Override
    protected void writeLeftColumn(FacesContext context, ResponseWriter w, UIApplicationLayout c, int size, BasicApplicationConfigurationImpl configuration, 
            boolean collapseLeftColumn, String collapseLeftTarget, String collapseLeftButtonLabel) throws IOException {
        
        UIComponent left = c.getLeftColumn();
        if (!isEmptyComponent(left)) {
            // Write the medium/ large screen component
            // if the collapseLeftColumn option is set, the large screen component is hidden on smaller screens
            w.startElement("div", c); // $NON-NLS-1$
            String mdCol = (String)getProperty(PROP_COLUMN_MEDIUM);
            String smCol = (String)getProperty(PROP_COLUMN_SMALL);
            
            String columnClass = ExtLibUtil.concatStyleClasses(mdCol + size, "applayout-column-left"); // $NON-NLS-1$
            if (collapseLeftColumn) {
                // #Bootstrap4 - fix hidden class
                columnClass = ExtLibUtil.concatStyleClasses(columnClass, "hidden-sm-down"); // $NON-NLS-1$
            } else {
                columnClass = ExtLibUtil.concatStyleClasses(columnClass, smCol + (size+1));
            }
            w.writeAttribute("class", columnClass, null); // $NON-NLS-1$
            
            FacesUtil.renderComponent(context, left);
            w.endElement("div"); // $NON-NLS-1$
            
            if (collapseLeftColumn) {
                // Write the small screen component (collapsed menu)
                w.startElement("script", c); // $NON-NLS-1$
                w.writeText("dojo.addOnLoad( function() { XTB.initCollapsibleMenu('" + collapseLeftButtonLabel + "', '" + collapseLeftTarget + "'); } );", null); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
                w.endElement("script"); // $NON-NLS-1$
                
            }
        }
    }
    
    @Override
    //TODO the small size fix has been applied to super class
    // if that is delivered, we can delete this method
    protected void writeRightColumn(FacesContext context, ResponseWriter w, UIApplicationLayout c, int size, BasicApplicationConfigurationImpl configuration) throws IOException {
        UIComponent right = c.getRightColumn();
        if (!isEmptyComponent(right)) {
            w.startElement("div", c); // $NON-NLS-1$
            String mdCol = (String)getProperty(PROP_COLUMN_MEDIUM);
            String smCol = (String)getProperty(PROP_COLUMN_SMALL);
            
            String columnClass = ExtLibUtil.concatStyleClasses(mdCol + size, smCol + (size+1));
            w.writeAttribute("class", ExtLibUtil.concatStyleClasses(columnClass, "applayout-column-right"), null); // $NON-NLS-1$ $NON-NLS-2$
            
            FacesUtil.renderComponent(context, right);
            w.endElement("div"); // $NON-NLS-1$
        }
    }
    
    @Override
    //TODO the small size fix has been applied to super class
    // if that is delivered, we can delete this method
    protected void writeContentColumn(FacesContext context, ResponseWriter w, UIApplicationLayout c, int size, BasicApplicationConfigurationImpl configuration) throws IOException {
        if (!isEmptyChildren(c)) {
            w.startElement("div", c); // $NON-NLS-1$
            
            String mdCol = (String)getProperty(PROP_COLUMN_MEDIUM);
            String smCol = (String)getProperty(PROP_COLUMN_SMALL);
            int smallSize = isEmptyComponent(c.getLeftColumn()) ? size : size-1;
            smallSize = isEmptyComponent(c.getRightColumn()) ? smallSize : smallSize-1;
            
            String contentClass = ExtLibUtil.concatStyleClasses(mdCol + size, smCol + smallSize);
            w.writeAttribute("class", ExtLibUtil.concatStyleClasses(contentClass, "applayout-content"), null); // $NON-NLS-1$ $NON-NLS-2$
            
            renderChildren(context, c);
            w.endElement("div"); // $NON-NLS-1$
        }
    }

    // ================================================================
    // Footer
    // ================================================================
    @Override
    protected void writeFooterLinks(FacesContext context, ResponseWriter w, UIApplicationLayout c, BasicApplicationConfigurationImpl configuration) throws IOException {
        ITree tree = TreeImpl.get(configuration.getFooterLinks());
        if (tree != null) {
            AbstractTreeRenderer renderer = new FooterLinksRenderer();
            if (renderer != null) {
                renderer.render(context, c, "fl", tree, w); // $NON-NLS-1$
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
}