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
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.util;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/**
 *
 * @author Brian Gleeson (brian.gleeson@ie.ibm.com)
 */
public class NavRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.util.NavRenderer {

    private static final long serialVersionUID = 1L;

    public NavRenderer() {
    }

    public NavRenderer(UIComponent component) {
        super(component);
    }

    @Override
    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
        String clazz=null;
        
        if(tree.getNode().getType()!=ITreeNode.NODE_CONTAINER && tree.getDepth()>2) {
            clazz = ExtLibUtil.concatStyleClasses(clazz, "dropdown-item");// $NON-NLS-1$ 
        }else{
            clazz = ExtLibUtil.concatStyleClasses(clazz, "nav-item"); // $NON-NLS-1$
        }
        
        if(tree.getNode().getType()==ITreeNode.NODE_SEPARATOR) {
            //clazz = ExtLibUtil.concatStyleClasses(clazz, "navbar-divider"); // $NON-NLS-1$
            return clazz;
        }
        if(tree.getNode().getType()==ITreeNode.NODE_CONTAINER) {
            if(tree.getDepth()>2) {
                clazz = ExtLibUtil.concatStyleClasses(clazz, "dropdown dropdown-submenu"); // $NON-NLS-1$
            } else {
                clazz = ExtLibUtil.concatStyleClasses(clazz, "dropdown"); // $NON-NLS-1$
            }
        }
        if(!enabled) {
            clazz = ExtLibUtil.concatStyleClasses(clazz, "disabled"); // $NON-NLS-1$
        }
        return clazz;
    }
    
    //TODO method from superclass, change package visibility?
    protected boolean makeSelectedActive(TreeContextImpl tree) {
        return tree.getDepth()<=2;
    }

    @Override
    protected void renderEntrySeparator(FacesContext context, ResponseWriter writer, TreeContextImpl tree) throws IOException {
        writer.startElement("hr", null); // $NON-NLS-1$
        writer.endElement("hr"); // $NON-NLS-1$
    }
    
    @Override
    protected void renderEntryItemLinkAttributes(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        String clazz = null;
        String styleClass = tree.getNode().getStyleClass();
        if(StringUtil.isNotEmpty(styleClass)) {
            clazz = ExtLibUtil.concatStyleClasses(clazz, styleClass);
        }
        
        String itemStyle = tree.getNode().getStyle();
        if(StringUtil.isNotEmpty(itemStyle)) {
            writer.writeAttribute("style",itemStyle,null); // $NON-NLS-1$
        }
        
        if(tree.getNode().getType()==ITreeNode.NODE_CONTAINER && tree.getDepth()<=2) {
            clazz = ExtLibUtil.concatStyleClasses(clazz, "nav-link dropdown-toggle");// $NON-NLS-1$ 
            writer.writeAttribute("data-toggle","dropdown",null); // $NON-NLS-1$ $NON-NLS-2$
            writer.writeAttribute("href", "#",  null); // $NON-NLS-1$ $NON-NLS-2$
        }else if(tree.getNode().getType()!=ITreeNode.NODE_CONTAINER && tree.getDepth()>2) {
            //Add no class to a link in a dropdown menu
        }else{
            clazz = ExtLibUtil.concatStyleClasses(clazz, "nav-link");// $NON-NLS-1$ 
        }
        if(selected && makeSelectedActive(tree)) {
            clazz = ExtLibUtil.concatStyleClasses(clazz, "active"); // $NON-NLS-1$
        }
        
        if(StringUtil.isNotEmpty(clazz)) {
            writer.writeAttribute("class",clazz,null); // $NON-NLS-1$
        }
    }
}
