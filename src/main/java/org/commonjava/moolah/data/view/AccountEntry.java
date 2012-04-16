package org.commonjava.moolah.data.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.commonjava.moolah.data.json.IndexableDateAdapter;
import org.commonjava.web.json.ser.JsonAdapters;

@JsonAdapters( { IndexableDateAdapter.class } )
public class AccountEntry
{

    private String transactionId;

    private Date timestamp;

    private BigDecimal amount;

    private Map<String, String> metadata;

    public String getTransactionId()
    {
        return transactionId;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    void setTransactionId( final String transactionId )
    {
        this.transactionId = transactionId;
    }

    void setTimestamp( final Date timestamp )
    {
        this.timestamp = timestamp;
    }

    void setAmount( final BigDecimal amount )
    {
        this.amount = amount;
    }

    void setMetadata( final Map<String, String> metadata )
    {
        this.metadata = metadata;
    }

}
