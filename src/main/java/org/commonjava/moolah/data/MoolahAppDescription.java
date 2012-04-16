package org.commonjava.moolah.data;

import java.util.Set;

import org.commonjava.couch.db.model.AppDescription;

public class MoolahAppDescription
    implements AppDescription
{

    public static final String APP_NAME = "moo-logic";

    @Override
    public String getAppName()
    {
        return APP_NAME;
    }

    @Override
    public String getClasspathAppResource()
    {
        return APP_NAME;
    }

    @Override
    public Set<String> getViewNames()
    {
        return MoolahView.viewNames();
    }

}
