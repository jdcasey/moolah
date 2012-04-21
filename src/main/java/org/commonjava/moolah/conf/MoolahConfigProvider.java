package org.commonjava.moolah.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.commonjava.couch.conf.CouchDBConfiguration;
import org.commonjava.couch.conf.DefaultCouchDBConfiguration;
import org.commonjava.moolah.inject.Moolah;
import org.commonjava.web.config.ConfigurationException;
import org.commonjava.web.config.DefaultConfigurationListener;
import org.commonjava.web.config.dotconf.DotConfConfigurationReader;

@Singleton
public class MoolahConfigProvider
    extends DefaultConfigurationListener
{

    private static final File DEFAULT_CONFIGFILE = new File( "/etc/moolah/main.conf" );

    private CouchDBConfiguration couchConfig;

    public MoolahConfigProvider()
        throws ConfigurationException
    {
        super( DefaultCouchDBConfiguration.class );
    }

    @Produces
    @Moolah
    @Default
    public CouchDBConfiguration getCouchConfig()
    {
        return couchConfig;
    }

    @PostConstruct
    public void loadConfig()
        throws ConfigurationException
    {
        FileInputStream in = null;
        try
        {
            in = new FileInputStream( DEFAULT_CONFIGFILE );
            final DotConfConfigurationReader configReader = new DotConfConfigurationReader( this );

            configReader.loadConfiguration( in );
        }
        catch ( final IOException e )
        {
            throw new ConfigurationException( "Failed to read config file: %s. Error: %s", e, DEFAULT_CONFIGFILE,
                                              e.getMessage() );
        }
    }

    @Override
    public void configurationComplete()
        throws ConfigurationException
    {
        couchConfig = getConfiguration( DefaultCouchDBConfiguration.class );
    }

}
