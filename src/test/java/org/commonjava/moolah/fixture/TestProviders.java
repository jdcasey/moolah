package org.commonjava.moolah.fixture;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.commonjava.couch.conf.CouchDBConfiguration;
import org.commonjava.couch.conf.DefaultCouchDBConfiguration;
import org.commonjava.couch.test.fixture.TestData;
import org.commonjava.moolah.inject.Moolah;

@Singleton
public class TestProviders
{

    private CouchDBConfiguration couchConfig;

    @Produces
    @TestData
    @Moolah
    @Default
    public synchronized CouchDBConfiguration getCouchConfig()
    {
        if ( couchConfig == null )
        {
            couchConfig = new DefaultCouchDBConfiguration( "http://localhost:5984/test-moolah" );
        }

        return couchConfig;
    }

}
