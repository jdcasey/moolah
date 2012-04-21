package org.commonjava.moolah.model;

import java.lang.reflect.Type;

import org.commonjava.moolah.model.AccountId.IdAdapter;
import org.commonjava.web.json.ser.JsonAdapters;
import org.commonjava.web.json.ser.WebSerializationAdapter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@JsonAdapters( IdAdapter.class )
public class AccountId
{

    private final String id;

    public AccountId( final String id )
    {
        this.id = id;
    }

    public String getRawId()
    {
        return id;
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
        final AccountId other = (AccountId) obj;
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
        return String.format( "AccountId [%s]", id );
    }

    public static final class IdAdapter
        implements WebSerializationAdapter, JsonSerializer<AccountId>, JsonDeserializer<AccountId>
    {

        private static final String ID_PROPERTY = "id";

        @Override
        public AccountId deserialize( final JsonElement json, final Type typeOfT,
                                      final JsonDeserializationContext context )
            throws JsonParseException
        {
            final JsonElement idObj = json.getAsJsonObject()
                                          .get( ID_PROPERTY );
            return idObj == null ? null : new AccountId( idObj.getAsString() );
        }

        @Override
        public JsonElement serialize( final AccountId src, final Type typeOfSrc, final JsonSerializationContext context )
        {
            final JsonObject obj = new JsonObject();
            obj.addProperty( ID_PROPERTY, src.id );
            return obj;
        }

        @Override
        public void register( final GsonBuilder gsonBuilder )
        {
            gsonBuilder.registerTypeAdapter( AccountId.class, this );
        }

    }

    public String toKeyPart()
    {
        return getRawId();
    }

}
