package org.commonjava.moolah.data;

import org.commonjava.moolah.model.AccountId;

public class MoolahBalanceRequest
    extends MoolahViewRequest
{

    public MoolahBalanceRequest( final AccountId account )
    {
        super( MoolahView.balance );
        setParameter( KEY, account.toKeyPart() );
    }

}
