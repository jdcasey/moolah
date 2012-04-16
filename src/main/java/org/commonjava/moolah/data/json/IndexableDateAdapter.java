package org.commonjava.moolah.data.json;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.commonjava.web.json.ser.WebSerializationAdapter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IndexableDateAdapter
    implements WebSerializationAdapter, JsonSerializer<Date>, JsonDeserializer<Date>
{

    private static final String YEAR = "utc_year";

    private static final String MONTH = "utc_month";

    private static final String DAY = "utc_day";

    private static final String TIMESTAMP = "utc_timestamp";

    private static final String COMPTIME_MILLIS = "comptime_millis";

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void register( final GsonBuilder gsonBuilder )
    {
        gsonBuilder.registerTypeAdapter( Date.class, this );
    }

    @Override
    public Date deserialize( final JsonElement json, final Type typeOfT, final JsonDeserializationContext context )
        throws JsonParseException
    {
        final JsonObject dateObj = json.getAsJsonObject();

        final JsonElement millisObj = dateObj.get( COMPTIME_MILLIS );
        final Date d = millisObj == null ? null : new Date( millisObj.getAsLong() );

        return d;
    }

    @Override
    public JsonElement serialize( final Date src, final Type typeOfSrc, final JsonSerializationContext context )
    {
        final JsonObject obj = new JsonObject();
        final Calendar cal = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        cal.setTime( src );

        obj.addProperty( DAY, cal.get( Calendar.DATE ) );
        obj.addProperty( MONTH, cal.get( Calendar.MONTH ) );
        obj.addProperty( YEAR, cal.get( Calendar.YEAR ) );
        obj.addProperty( TIMESTAMP, new SimpleDateFormat( TIMESTAMP_FORMAT ).format( src ) );
        obj.addProperty( COMPTIME_MILLIS, src.getTime() );

        return obj;
    }

}
