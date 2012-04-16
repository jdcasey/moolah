package org.commonjava.moolah.data.view;

import java.math.BigDecimal;

public class AccountBalance
{
    private BigDecimal balance;

    AccountBalance()
    {
    }

    void setBalance( final BigDecimal balance )
    {
        this.balance = balance;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

}
