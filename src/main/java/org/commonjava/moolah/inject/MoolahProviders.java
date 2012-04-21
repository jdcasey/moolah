package org.commonjava.moolah.inject;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.commonjava.couch.change.CouchChangeListener;
import org.commonjava.couch.conf.CouchDBConfiguration;
import org.commonjava.couch.db.CouchFactory;
import org.commonjava.couch.db.CouchManager;
import org.commonjava.couch.io.CouchHttpClient;

@Singleton
public class MoolahProviders
{
    private CouchManager couchManager;

    private CouchHttpClient httpClient;

    private CouchChangeListener changeListener;

    @Inject
    private CouchFactory factory;

    @Inject
    @Moolah
    private CouchDBConfiguration couchConfig;

    @Produces
    @Moolah
    @Default
    public synchronized CouchChangeListener getChangeListener()
    {
        System.out.println( "Returning change listener for user DB" );
        if ( changeListener == null )
        {
            changeListener = factory.getChangeListener( couchConfig );
        }

        return changeListener;
    }

    @Produces
    @Moolah
    @Default
    public synchronized CouchManager getCouchManager()
    {
        if ( couchManager == null )
        {
            couchManager = factory.getCouchManager( couchConfig );
        }

        return couchManager;
    }

    @Produces
    @Moolah
    @Default
    public synchronized CouchHttpClient getHttpClient()
    {
        if ( httpClient == null )
        {
            httpClient = factory.getHttpClient( couchConfig );
        }

        return httpClient;
    }

}
