package org.commonjava.moolah.model;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.commonjava.couch.util.IdUtils.namespaceId;

import java.util.HashMap;
import java.util.Map;

import org.commonjava.couch.model.AbstractCouchDocument;
import org.commonjava.couch.model.DenormalizedCouchDoc;

import com.google.gson.annotations.Expose;

public class Account
    extends AbstractCouchDocument
    implements DenormalizedCouchDoc
{

    public static final String NAMESPACE = "account";

    @Expose( deserialize = false )
    private final String doctype = NAMESPACE;

    private AccountId id;

    private String name;

    private String description;

    private Map<String, String> metadata;

    private Account( final AccountId id, final String name, final String description, final Map<String, String> metadata )
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.metadata = metadata;
    }

    Account()
    {
    }

    void setId( final AccountId id )
    {
        this.id = id;
    }

    void setName( final String name )
    {
        this.name = name;
    }

    void setDescription( final String description )
    {
        this.description = description;
    }

    void setMetadata( final Map<String, String> metadata )
    {
        this.metadata = metadata;
    }

    public String setMetadata( final String key, final String value )
    {
        return metadata.put( key, value );
    }

    public String removeMetadata( final String key )
    {
        return metadata.remove( key );
    }

    public String getMetadata( final String key )
    {
        return metadata.get( key );
    }

    public static final class Builder
    {
        private AccountId id;

        private final String name;

        private String description;

        private final Map<String, String> metadata = new HashMap<String, String>();

        public Builder( final String id, final String name )
            throws MoolahDataException
        {
            if ( id == null || isEmpty( name ) )
            {
                throw new MoolahDataException(
                                               "You must specify a valid ID and name for a new account. (ID: '%s', name: '%s')",
                                               id, name );
            }

            if ( !isEmpty( id ) )
            {
                this.id = new AccountId( id );
            }

            this.name = name;
        }

        public Builder withDescription( final String description )
        {
            this.description = description;
            return this;
        }

        public Builder withMetadata( final Map<String, String> metadata )
        {
            this.metadata.putAll( metadata );
            return this;
        }

        public Builder withMetadata( final String key, final String value )
        {
            this.metadata.put( key, value );
            return this;
        }

        public Account build()
        {
            return new Account( id, name, description, metadata );
        }
    }

    public String getDoctype()
    {
        return doctype;
    }

    public AccountId getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final Account other = (Account) obj;
        if ( id == null )
        {
            if ( other.id != null )
            {
                return false;
            }
        }
        else if ( !id.equals( other.id ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format( "Account [%s]", id );
    }

    @Override
    public void calculateDenormalizedFields()
    {
        setCouchDocId( documentId( id ) );
    }

    public static String documentId( final AccountId id )
    {
        return namespaceId( NAMESPACE, id.getRawId() );
    }

}
