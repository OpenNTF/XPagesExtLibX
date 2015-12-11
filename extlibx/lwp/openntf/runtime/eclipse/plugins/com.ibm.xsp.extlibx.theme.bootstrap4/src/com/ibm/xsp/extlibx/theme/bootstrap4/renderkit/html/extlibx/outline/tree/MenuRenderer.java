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
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.outline.tree;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.theme.bootstrap.resources.Resources;

public class MenuRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.outline.tree.MenuRenderer {

    private static final long serialVersionUID = 1L;

    public MenuRenderer() {
    }

    public MenuRenderer(UIComponent component, int type) {
        super(component, type);
    }

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_MENU_SELECTED:    return "active"; // $NON-NLS-1$
            case PROP_MENU_EXPANDED:    return Resources.get().getIconClass("minus-sign"); // $NON-NLS-1$
            case PROP_MENU_COLLAPSED:   return Resources.get().getIconClass("plus-sign"); // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
    
    @Override
    protected String getContainerStyleClass(TreeContextImpl node) {
        return "nav nav-pills nav-stacked xspNavigator"; // $NON-NLS-1$
    }
    
    @Override
    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
        String clazz=null;
        if(tree.getNode().getType()==ITreeNode.NODE_CONTAINER) {
            //clazz = "nav-header";
        } else if(tree.getNode().getType()==ITreeNode.NODE_SEPARATOR) {
            // #Bootstrap4 Using HR as separators, no class needed
            //clazz = "navbar-divider"; // $NON-NLS-1$
        }else{
            clazz = "nav-item"; // $NON-NLS-1$
        }
        
        return clazz;
    }
    
    @Override
    protected void renderEntryItemLinkAttributes(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        boolean section = tree.getNode().getType()!=ITreeNode.NODE_LEAF;
        String itemLinkClass = null;
        
        if(!enabled) {
            itemLinkClass = ExtLibUtil.concatStyleClasses(itemLinkClass, "disabled"); // $NON-NLS-1$
        }
        if(selected) {
            itemLinkClass = ExtLibUtil.concatStyleClasses(itemLinkClass, "active"); // $NON-NLS-1$
        }
        
        if(section) {
            itemLinkClass = ExtLibUtil.concatStyleClasses(itemLinkClass, "xspSection"); // $NON-NLS-1$
        } else {
            //TODO pull in styleClass/style prop of leaf node here?
            String leafLinkClass = "nav-link"; // $NON-NLS-1$
            itemLinkClass = ExtLibUtil.concatStyleClasses(itemLinkClass,leafLinkClass);
        }
        
        if(StringUtil.isNotEmpty(itemLinkClass)) {
            writer.writeAttribute("class", itemLinkClass, null); // $NON-NLS-1$
        }
    }
    
    @Override
    // #Bootstrap4 Use HR as separators
    protected void renderEntrySeparator(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        writer.startElement("hr", null); // $NON-NLS-1$
        writer.endElement("hr"); // $NON-NLS-1$
    }
    
    @Override
    protected void renderEntryItemLabel(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        String label = tree.getNode().getLabel();
        if(StringUtil.isNotEmpty(label)) {
            boolean addDiv = tree.getNode().getType()!=ITreeNode.NODE_LEAF && isExpandable() && !hasLink(tree);
            if(addDiv) {
                writer.startElement("div",  tree.getComponent()); // $NON-NLS-1$
                String divClass = "xspSection"; // $NON-NLS-1$
                writer.writeAttribute("class", divClass, null); // $NON-NLS-1$
            }
            writer.writeText(label, "label"); // $NON-NLS-1$
            if(addDiv) {
                writer.endElement("div"); // $NON-NLS-1$
            }
        }
    }
}