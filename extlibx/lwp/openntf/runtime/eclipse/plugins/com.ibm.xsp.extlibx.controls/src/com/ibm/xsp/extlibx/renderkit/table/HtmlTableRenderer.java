package com.ibm.xsp.extlibx.renderkit.table;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ibm.xsp.util.FacesUtil;

public class HtmlTableRenderer extends com.ibm.xsp.renderkit.html_extended.HtmlTableRenderer {

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		super.encodeBegin(context, component);

		ResponseWriter w = context.getResponseWriter();

		UIComponent thead = component.getFacet("thead");

		if (thead != null) {
			w.startElement("thead", null);
			FacesUtil.renderComponent(context, thead);
			w.endElement("thead");
		}

		UIComponent tfoot = component.getFacet("tfoot");

		if (tfoot != null) {
			w.startElement("tfoot", null);
			FacesUtil.renderComponent(context, tfoot);
			w.endElement("tfoot");
		}

		w.startElement("tbody", null);

	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		context.getResponseWriter().endElement("tbody");

		super.encodeEnd(context, component);
	}

}
