package org.commonjava.moolah.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionSplit
{

    private final AccountId to;

    private final BigDecimal amount;

    private final Map<String, String> metadata;

    private TransactionSplit( final String transactionId, final AccountId to, final BigDecimal amount,
                              final Map<String, String> metadata )
    {
        this.to = to;
        this.amount = amount;
        this.metadata = metadata;
    }

    public static final class Builder
    {
        private final AccountId to;

        private final BigDecimal amount;

        private final Map<String, String> metadata;

        private Transaction.Builder builder;

        private final String transactionId;

        public Builder( final String transactionId, final AccountId to, final BigDecimal amount )
        {
            this.transactionId = transactionId;
            this.to = to;
            this.amount = amount;
            this.metadata = new HashMap<String, String>();
        }

        public Builder( final Transaction.Builder builder, final AccountId to, final BigDecimal amount )
        {
            this.builder = builder;
            this.transactionId = builder.getTransactionId();
            this.to = to;
            this.amount = amount;
            this.metadata = new HashMap<String, String>();
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

        public Transaction.Builder done()
        {
            return builder.withSplit( build() );
        }

        public TransactionSplit build()
        {
            return new TransactionSplit( transactionId, to, amount, metadata );
        }

    }

    public AccountId getTo()
    {
        return to;
    }

    public BigDecimal getAmount()
    {
        return amount;
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

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( amount == null ) ? 0 : amount.hashCode() );
        result = prime * result + ( ( to == null ) ? 0 : to.hashCode() );
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
        final TransactionSplit other = (TransactionSplit) obj;
        if ( amount == null )
        {
            if ( other.amount != null )
            {
                return false;
            }
        }
        else if ( !amount.equals( other.amount ) )
        {
            return false;
        }
        if ( to == null )
        {
            if ( other.to != null )
            {
                return false;
            }
        }
        else if ( !to.equals( other.to ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format( "TransactionSplit [to=%s, amount=%s]", to, amount );
    }

}
