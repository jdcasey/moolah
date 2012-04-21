package org.commonjava.moolah.data.query;

import java.util.Date;
import java.util.Formattable;
import java.util.Formatter;

import org.commonjava.moolah.model.AccountId;

public class AccountQuery
    implements Formattable
{

    private final AccountId accountId;

    private Date start;

    private Date end;

    private String startingTransactionId;

    private int limit;

    public AccountQuery( final AccountId accountId )
    {
        this.accountId = accountId;
    }

    public AccountQuery withStartDate( final Date start )
    {
        this.start = start;
        return this;
    }

    public AccountQuery withEndDate( final Date end )
    {
        this.end = end;
        return this;
    }

    public AccountQuery withStartingTransactionId( final String startingTransactionId )
    {
        this.startingTransactionId = startingTransactionId;
        return this;
    }

    public AccountQuery withLimit( final int limit )
    {
        this.limit = limit;
        return this;
    }

    public AccountId getAccountId()
    {
        return accountId;
    }

    public Date getStart()
    {
        return start;
    }

    public Date getEnd()
    {
        return end;
    }

    public String getStartingTransactionId()
    {
        return startingTransactionId;
    }

    public int getLimit()
    {
        return limit;
    }

    @Override
    public void formatTo( final Formatter formatter, final int flags, final int width, final int precision )
    {
        formatter.format( "AccountQuery:\n  account-id: %s\n  starting transaction-id: %s\n  start-date: %s\n  end-date: %s\n",
                          accountId, startingTransactionId, start, end );
    }

}
