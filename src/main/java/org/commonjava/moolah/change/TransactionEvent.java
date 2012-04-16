package org.commonjava.moolah.change;

import org.commonjava.moolah.model.Transaction;

public class TransactionEvent
{

    private final Transaction transaction;

    public TransactionEvent( final Transaction transaction )
    {
        this.transaction = transaction;
    }

    public Transaction getTransaction()
    {
        return transaction;
    }

}
