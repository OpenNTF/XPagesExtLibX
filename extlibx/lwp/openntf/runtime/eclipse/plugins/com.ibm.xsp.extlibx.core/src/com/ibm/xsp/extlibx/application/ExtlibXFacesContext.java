package com.ibm.xsp.extlibx.application;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ApplicationExImpl;
import com.ibm.xsp.component.UIViewRootEx2;
import com.ibm.xsp.context.FacesContextExImpl;
import com.ibm.xsp.stylekit.StyleKit;
import com.ibm.xsp.stylekit.StyleKitImpl;

/**
 * 
 * Work here is heavily derived from Sven Hasselbach's ThemeSwitchingContext
 * http://hasselba.ch/blog/?p=1414
 *
 */
public class ExtlibXFacesContext extends FacesContextExImpl {

	public StyleKit viewStyleKit = null;

	public ExtlibXFacesContext(FacesContext context) {
		super(context);
	}

	private ApplicationExImpl getApplicationExImpl() {
		return (ApplicationExImpl) getApplication();
	}

	@Override
	public StyleKit getStyleKit() {

		if (viewStyleKit == null) {

			UIViewRoot root = getViewRoot();

			if (root != null) {

				viewStyleKit = StyleKitImpl.emptyTheme;

				String viewTheme = getViewStyleKitId(root);

				if (StringUtil.isNotEmpty(viewTheme)) {
					viewStyleKit = getApplicationExImpl().getStyleKit(viewTheme);
				} else {

				}

			} else {
				/*
				 * It will reach here before the view Root is created, becuase
				 * within 'getProperty' it checks the current theme for a
				 * property I only saw it trying to get xsp.persistence.uniqueid
				 */
			}

		}

		if (viewStyleKit != null && !viewStyleKit.isEmpty()) {
			return viewStyleKit;
		}

		return super.getStyleKit();
	}

	private String getViewStyleKitId(UIViewRoot root) {

		if (root instanceof UIViewRootEx2) {
			UIViewRootEx2 rootex2 = ((UIViewRootEx2) root);
			return rootex2.getViewProperty("xsp.theme");
		}
		return null;

	}

}
