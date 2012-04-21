package org.commonjava.moolah.data;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.commonjava.couch.db.model.ViewRequest;
import org.commonjava.moolah.data.query.AccountQuery;
import org.commonjava.moolah.model.AccountId;

public class MoolahViewRequest
    extends ViewRequest
{

    public MoolahViewRequest( final AccountQuery query, final MoolahView view )
    {
        super( MoolahAppDescription.APP_NAME, view.viewName() );

        final AccountId account = query.getAccountId();

        if ( query.getStart() != null )
        {
            setParameterArray( START_KEY,
                               transactionListKey( account, query.getStartingTransactionId(), query.getStart() ) );
        }

        if ( query.getEnd() != null )
        {
            setParameterArray( END_KEY, transactionListKey( account, WILDCARD, query.getEnd() ) );
        }
    }

    public MoolahViewRequest( final AccountId account, final MoolahView view )
    {
        super( MoolahAppDescription.APP_NAME, view.viewName() );
        setParameter( KEY, account );
    }

    public MoolahViewRequest( final MoolahView view )
    {
        super( MoolahAppDescription.APP_NAME, view.viewName() );
    }

    protected Object[] transactionListKey( final AccountId account, final String startTransactionId, final Date date )
    {
        final Object[] parts = new Object[5];
        int idx = 0;
        parts[idx++] = account.toKeyPart();
        parts[idx++] = startTransactionId;

        final Calendar cal = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
        cal.setTime( date );

        parts[idx++] = cal.get( Calendar.YEAR );
        parts[idx++] = cal.get( Calendar.MONTH );
        parts[idx++] = cal.get( Calendar.DAY_OF_MONTH );

        return parts;
    }
}
