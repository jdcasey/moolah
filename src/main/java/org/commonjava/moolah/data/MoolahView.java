package org.commonjava.moolah.data;

import java.util.HashSet;
import java.util.Set;

public enum MoolahView
{
    account_list, balance, transactions, account_entries;

    private String viewName;

    private MoolahView()
    {
        this.viewName = null;
    }

    private MoolahView( final String viewName )
    {
        this.viewName = viewName;
    }

    public String viewName()
    {
        return viewName == null ? name() : viewName;
    }

    public static Set<String> viewNames()
    {
        final Set<String> names = new HashSet<String>();
        for ( final MoolahView view : values() )
        {
            names.add( view.viewName() );
        }

        return names;
    }

}
