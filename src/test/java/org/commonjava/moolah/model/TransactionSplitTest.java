package org.commonjava.moolah.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.commonjava.web.json.ser.JsonSerializer;
import org.junit.Test;

public class TransactionSplitTest
{

    @Test
    public void roundTrip()
    {
        final TransactionSplit split =
            new TransactionSplit.Builder( "0001-0101", new AccountId( "0001" ), BigDecimal.valueOf( 25.53 ) ).build();

        final JsonSerializer serializer = new JsonSerializer();

        final String json = serializer.toString( split );

        System.out.println( json );

        final TransactionSplit result = serializer.fromString( json, TransactionSplit.class );

        assertThat( result, equalTo( split ) );
    }

}
