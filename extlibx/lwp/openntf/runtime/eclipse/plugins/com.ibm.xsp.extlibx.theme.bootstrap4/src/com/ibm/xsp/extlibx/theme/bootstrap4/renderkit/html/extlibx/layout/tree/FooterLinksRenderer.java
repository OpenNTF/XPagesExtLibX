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
package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.layout.tree;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.renderkit.html_extended.outline.tree.HtmlListRenderer;

import com.ibm.xsp.extlib.tree.ITreeNode;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.renderkit.html_basic.HtmlRendererUtil;
import com.ibm.xsp.renderkit.html_extended.RenderUtil;

public class FooterLinksRenderer extends HtmlListRenderer {
    
    private static final long serialVersionUID = 1L;

    public FooterLinksRenderer() {
    }

    @Override
    protected String getContainerStyleClass(TreeContextImpl node) {
        if(node.getDepth()==1) {
            return "nav nav-pills applayout-footerlinks"; // $NON-NLS-1$
        }
        return super.getContainerStyleClass(node);
    }

    @Override
    protected String getContainerStyle(TreeContextImpl node) {
        String style = super.getContainerStyle(node);
        //return ExtLibUtil.concatStyles(style, "margin-left:2em; margin-right:2em"); // $NON-NLS-1$
        return style;
    }
    
    @Override
    protected String getItemStyle(TreeContextImpl tree, boolean enabled, boolean selected) {
        String style = super.getItemStyle();
        return ExtLibUtil.concatStyles(style, "list-style-type: none;"); // $NON-NLS-1$
    }
    
    @Override
    protected String getItemStyleClass(TreeContextImpl tree, boolean enabled, boolean selected) {
        String styleClass = super.getItemStyleClass();
        
        if(tree.getNode().getType()==ITreeNode.NODE_LEAF) {
            styleClass = ExtLibUtil.concatStyles(styleClass, "nav-item"); // $NON-NLS-1$
        }
        return styleClass;
    }
    
    @Override
    protected void renderEntryItemLinkAttributes(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        writer.writeAttribute("class","nav-link",null); // $NON-NLS-1$ $NON-NLS-2$
    }
    @Override
    protected boolean alwaysRenderItemLink(TreeContextImpl tree, boolean enabled, boolean selected) {
        return false;
    }
    @Override
    protected void renderEntryItemContent(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        boolean hasLink = false;
        boolean alwaysRenderLinks = alwaysRenderItemLink(tree, enabled, selected);
        if(enabled) {
            String href = tree.getNode().getHref();
            if(StringUtil.isNotEmpty(href)) {
                writer.startElement("a",null);
                String role = getItemRole(tree, enabled, selected);
                if (StringUtil.isNotEmpty(role)) {
                    writer.writeAttribute("role", role, null); // $NON-NLS-1$
                }
                RenderUtil.writeLinkAttribute(context,writer,href);
                hasLink = true;
            } else {
                String onclick = findNodeOnClick(tree);
                if(StringUtil.isNotEmpty(onclick)) {
                    writer.startElement("a",null);
                    writer.writeAttribute("href","javascript:;",null); // $NON-NLS-1$ $NON-NLS-2$
                    writer.writeAttribute("onclick", "javascript:"+onclick, null); // $NON-NLS-1$ $NON-NLS-2$
                    hasLink = true;
                }else{
                    writer.startElement("div",null); // $NON-NLS-1$
                    hasLink = false;
                }
            }
        }
        if(!hasLink && alwaysRenderLinks) {
            // Render an empty link...
            writer.startElement("a",null);
            hasLink = true;
        }
        if(hasLink) {
            renderEntryItemLinkAttributes(context, writer, tree, enabled, selected);
        }

        String image = tree.getNode().getImage();
        boolean hasImage = StringUtil.isNotEmpty(image);
        if(hasImage) {
            writer.startElement("img",null); // $NON-NLS-1$
            image = HtmlRendererUtil.getImageURL(context, image);
            writer.writeAttribute("src",image,null); // $NON-NLS-1$
            String imageAlt = tree.getNode().getImageAlt();
            if (StringUtil.isNotEmpty(imageAlt)) {
                writer.writeAttribute("alt",imageAlt,null); // $NON-NLS-1$
            }
            String imageHeight = tree.getNode().getImageHeight();
            if (StringUtil.isNotEmpty(imageHeight)) {
                writer.writeAttribute("height",imageHeight,null); // $NON-NLS-1$
            }
            String imageWidth = tree.getNode().getImageWidth();
            if (StringUtil.isNotEmpty(imageWidth)) {
                writer.writeAttribute("width",imageWidth,null); // $NON-NLS-1$
            }
            writer.endElement("img"); // $NON-NLS-1$
        }
        
        // Generate a regular node
        renderEntryItemLabel(context, writer, tree, enabled, selected);
        
        // Render a popup image, if any
        writePopupImage(context, writer, tree);

        if(hasLink || alwaysRenderLinks) {
            writer.endElement("a");
            tree.markCurrentAsAction();
        }
        if(!hasLink) {
            writer.endElement("div"); // $NON-NLS-1$
        }
    }
    
    //Adding this method so it can be over-ridden in subclasses. See com.ibm.xsp.theme.twitter.bootstrap.MenuRenderer
    @Override
    protected void renderEntryItemLabel(FacesContext context, ResponseWriter writer, TreeContextImpl tree, boolean enabled, boolean selected) throws IOException {
        String label = tree.getNode().getLabel();
        if(StringUtil.isNotEmpty(label)) {
            writer.writeText(label, "label"); // $NON-NLS-1$
        }
    }
}