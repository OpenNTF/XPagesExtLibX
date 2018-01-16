package com.ibm.xsp.extlibx.component.table;

import com.ibm.xsp.component.xp.XspTableCell;

public class UITableHeaderCell extends XspTableCell {

	public static final String RENDERER_TYPE = "com.ibm.xsp.extlibx.TableHeaderCell";

	public UITableHeaderCell() {
		super();
		setRendererType(RENDERER_TYPE);
	}

}
