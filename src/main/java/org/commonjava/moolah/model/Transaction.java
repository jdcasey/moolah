package org.commonjava.moolah.model;

import static org.commonjava.couch.util.IdUtils.namespaceId;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.commonjava.couch.model.AbstractCouchDocument;
import org.commonjava.couch.model.DenormalizedCouchDoc;
import org.commonjava.moolah.data.json.IndexableDateAdapter;
import org.commonjava.web.json.ser.JsonAdapters;

import com.google.gson.annotations.Expose;

@JsonAdapters( { IndexableDateAdapter.class } )
public class Transaction
    extends AbstractCouchDocument
    implements DenormalizedCouchDoc, Iterable<TransactionSplit>
{

    public static final String NAMESPACE = "transaction";

    @Expose( deserialize = false )
    private final String doctype = NAMESPACE;

    private String transactionId;

    private Date timestamp;

    private AccountId from;

    private Set<TransactionSplit> splits;

    private Map<String, String> metadata;

    private Transaction( final String transactionId, final AccountId from, final Date timestamp,
                         final Set<TransactionSplit> splits, final Map<String, String> transactionMetadata )
    {
        this.from = from;
        this.timestamp = timestamp;
        this.splits = Collections.unmodifiableSet( splits );
        metadata = transactionMetadata;
        this.transactionId = transactionId;
    }

    Transaction()
    {
    }

    public BigDecimal getAmount()
    {
        BigDecimal total = null;
        for ( final TransactionSplit out : splits )
        {
            final BigDecimal amt = out.getAmount();

            total = total == null ? amt : total.add( amt );
        }

        return total;
    }

    public String getTransactionId()
    {
        return transactionId;
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

    public AccountId getFrom()
    {
        return from;
    }

    public String getDoctype()
    {
        return doctype;
    }

    public Set<TransactionSplit> getSplits()
    {
        return splits;
    }

    @Override
    public Iterator<TransactionSplit> iterator()
    {
        return splits == null ? Collections.<TransactionSplit> emptySet()
                                           .iterator() : splits.iterator();
    }

    void setTransactionId( final String transactionId )
    {
        this.transactionId = transactionId;
    }

    void setSplits( final Set<TransactionSplit> splits )
    {
        this.splits = splits;
    }

    void setFrom( final AccountId from )
    {
        this.from = from;
    }

    void setMetadata( final Map<String, String> metadata )
    {
        this.metadata = metadata;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    void setTimestamp( final Date timestamp )
    {
        this.timestamp = timestamp;
    }

    public static String documentId( final String transactionId )
    {
        return namespaceId( NAMESPACE, transactionId );
    }

    @Override
    public void calculateDenormalizedFields()
    {
        setCouchDocId( documentId( transactionId ) );
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( transactionId == null ) ? 0 : transactionId.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( !super.equals( obj ) )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final Transaction other = (Transaction) obj;
        if ( transactionId == null )
        {
            if ( other.transactionId != null )
            {
                return false;
            }
        }
        else if ( !transactionId.equals( other.transactionId ) )
        {
            return false;
        }
        return true;
    }

    public static final class Builder
    {
        private Date timestamp;

        private final AccountId from;

        private final Set<TransactionSplit> splits;

        private Map<String, String> metadata;

        private final String transactionId;

        public Builder( final AccountId from )
        {
            this.from = from;
            this.splits = new HashSet<TransactionSplit>();
            this.transactionId = UUID.randomUUID()
                                     .toString();

        }

        public Builder withTimestamp( final Date timestamp )
        {
            this.timestamp = timestamp;
            return this;
        }

        public TransactionSplit.Builder withNewSplit( final AccountId to, final BigDecimal amount )
        {
            return new TransactionSplit.Builder( transactionId, to, amount );
        }

        public Builder withSplit( final TransactionSplit split )
        {
            splits.add( split );
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

        public String getTransactionId()
        {
            return transactionId;
        }

        public Transaction build()
            throws MoolahDataException
        {
            if ( splits.isEmpty() )
            {
                throw new MoolahDataException( "Invalid transaction: You must specify at least one destination." );
            }

            if ( timestamp == null )
            {
                timestamp = new Date();
            }

            return new Transaction( transactionId, from, timestamp, splits, metadata );
        }
    }

}
