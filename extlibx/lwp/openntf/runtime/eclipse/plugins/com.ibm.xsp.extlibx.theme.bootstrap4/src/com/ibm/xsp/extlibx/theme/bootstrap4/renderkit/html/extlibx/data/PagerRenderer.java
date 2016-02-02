package com.ibm.xsp.extlibx.theme.bootstrap4.renderkit.html.extlibx.data;

public class PagerRenderer extends com.ibm.xsp.theme.bootstrap.renderkit.html.extlib.PagerRenderer {
    
    //TODO This minimal class is reliant on changes being added to the 
    //     super class to make it more extensible
    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_LISTITEMCLASS:  return "page-item"; // $NON-NLS-1$
            case PROP_PAGERLINKCLASS: return "page-link"; // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}