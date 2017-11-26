package com.ibm.xsp.extlibx.renderkit.table;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.ibm.xsp.renderkit.html_extended.HtmlTableCellRenderer;

public class HtmlTableHeaderCellRenderer extends HtmlTableCellRenderer {

	public static final String TAG = "th";
	public static final String[] ATTRS = { "rowspan", "colspan", "align", "valign", "title", "role" };

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		super.encodeBegin(context, component, TAG, ATTRS);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		super.encodeEnd(context, component, TAG);
	}

}
