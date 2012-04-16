package org.commonjava.moolah.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.commonjava.moolah.model.AccountId;

public class MoolahTransactionRequest
    extends MoolahViewRequest
{

    public MoolahTransactionRequest( final AccountId account, final Date start, final Date end )
    {
        super( MoolahView.transactions );
        setParameterArray( START_KEY, transactionListKey( account, start ) );
        setParameterArray( END_KEY, transactionListKey( account, end ) );
    }

    private Object[] transactionListKey( final AccountId account, final Date date )
    {
        final Object[] parts = new Object[4];
        parts[0] = account.toKeyPart();
        final Calendar cal = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        cal.setTime( date );

        parts[1] = cal.get( Calendar.YEAR );
        parts[2] = cal.get( Calendar.MONTH );
        parts[3] = cal.get( Calendar.DAY_OF_MONTH );

        return parts;
    }

}
