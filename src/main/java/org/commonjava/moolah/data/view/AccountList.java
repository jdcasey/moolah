package org.commonjava.moolah.data.view;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.commonjava.moolah.model.Account;

public class AccountList
    implements Iterable<Account>
{

    private List<Account> accounts;

    public AccountList( final List<Account> accounts )
    {
        this.accounts = accounts;
    }

    AccountList()
    {
    }

    void setAccounts( final List<Account> accounts )
    {
        this.accounts = accounts;
    }

    public List<Account> getAccounts()
    {
        return accounts;
    }

    @Override
    public Iterator<Account> iterator()
    {
        return accounts == null ? Collections.<Account> emptyList()
                                             .iterator() : accounts.iterator();
    }

}
