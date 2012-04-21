package org.commonjava.moolah.data;

import static org.apache.commons.lang.StringUtils.join;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.commonjava.couch.conf.CouchDBConfiguration;
import org.commonjava.couch.db.CouchDBException;
import org.commonjava.couch.db.CouchManager;
import org.commonjava.couch.model.CouchApp;
import org.commonjava.couch.model.CouchDocRef;
import org.commonjava.moolah.data.query.AccountQuery;
import org.commonjava.moolah.data.view.AccountBalance;
import org.commonjava.moolah.data.view.AccountEntry;
import org.commonjava.moolah.data.view.AccountList;
import org.commonjava.moolah.inject.Moolah;
import org.commonjava.moolah.model.Account;
import org.commonjava.moolah.model.AccountId;
import org.commonjava.moolah.model.MoolahDataException;
import org.commonjava.moolah.model.Transaction;
import org.commonjava.moolah.model.TransactionSplit;

@Singleton
public class MoolahDataManager
{

    @Inject
    @Moolah
    private CouchManager couch;

    @Inject
    @Moolah
    private CouchDBConfiguration couchConfig;

    public void install()
        throws MoolahDataException
    {
        final MoolahAppDescription description = new MoolahAppDescription();
        final CouchApp app = new CouchApp( MoolahAppDescription.APP_NAME, description );

        try
        {
            if ( couch.dbExists() )
            {
                // TODO: This may not be friendly for multiple apps using the same DB...
                // Forcibly reload views.
                couch.delete( app );
            }

            couch.initialize( description );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to initialize moolah database: %s (application: %s). Reason: %s", e,
                                           couchConfig.getDatabaseUrl(), description.getAppName(), e.getMessage() );
        }
    }

    public boolean storeAccount( final Account account )
        throws MoolahDataException
    {
        try
        {
            return couch.store( account, false );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to store account: %s. Error: %s", e, account, e.getMessage() );
        }
    }

    public void deleteAccount( final AccountId accountId )
        throws MoolahDataException
    {
        try
        {
            couch.delete( new CouchDocRef( Account.documentId( accountId ) ) );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to delete account: %s. Error: %s", e, accountId, e.getMessage() );
        }
    }

    public Account getAccount( final AccountId accountId )
        throws MoolahDataException
    {
        try
        {
            return couch.getDocument( new CouchDocRef( Account.documentId( accountId ) ), Account.class );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to retrieve account: %s. Error: %s", e, accountId, e.getMessage() );
        }
    }

    public boolean storeTransaction( final Transaction transaction )
        throws MoolahDataException
    {
        try
        {
            final List<AccountId> ids = new ArrayList<AccountId>();
            ids.add( transaction.getFrom() );
            for ( final TransactionSplit split : transaction )
            {
                ids.add( split.getTo() );
            }

            final List<AccountId> missing = findMissingAccounts( ids );
            if ( !missing.isEmpty() )
            {
                throw new MoolahDataException(
                                               "Cannot store transaction where one or more accounts are missing in the system: %s",
                                               new Object()
                                               {
                                                   @Override
                                                   public String toString()
                                                   {
                                                       return join( missing, ", " );
                                                   }
                                               } );
            }

            return couch.store( transaction, false );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to store transaction: %s. Error: %s", e, transaction, e.getMessage() );
        }
    }

    private List<AccountId> findMissingAccounts( final List<AccountId> ids )
        throws MoolahDataException
    {
        final List<AccountId> missing = new ArrayList<AccountId>();
        for ( final AccountId id : ids )
        {
            try
            {
                if ( !couch.exists( new CouchDocRef( Account.documentId( id ) ) ) )
                {
                    missing.add( id );
                }
            }
            catch ( final CouchDBException e )
            {
                throw new MoolahDataException( "Error while verifying existence of account: %s. Error was: %s", e, id,
                                               e.getMessage() );
            }
        }

        return missing;
    }

    public void deleteTransaction( final String transactionId )
        throws MoolahDataException
    {
        try
        {
            couch.delete( new CouchDocRef( Transaction.documentId( transactionId ) ) );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to delete transaction: %s. Error: %s", e, transactionId,
                                           e.getMessage() );
        }
    }

    public Transaction getTransaction( final String transactionId )
        throws MoolahDataException
    {
        try
        {
            return couch.getDocument( new CouchDocRef( Transaction.documentId( transactionId ) ), Transaction.class );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to retrieve transaction: %s. Error: %s", e, transactionId,
                                           e.getMessage() );
        }
    }

    public AccountList getAccountList()
        throws MoolahDataException
    {
        final MoolahViewRequest req = new MoolahViewRequest( MoolahView.account_list );
        try
        {
            return couch.getView( req, AccountList.class );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to retrieve account list. Error: %s", e, e.getMessage() );
        }
    }

    public AccountBalance getAccountBalance( final AccountId account )
        throws MoolahDataException
    {
        final MoolahViewRequest req = new MoolahViewRequest( account, MoolahView.balance );
        try
        {
            return couch.getView( req, AccountBalance.class );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to retrieve account balance for: %s. Error: %s", e, account,
                                           e.getMessage() );
        }
    }

    public List<AccountEntry> getAccountEntries( final AccountQuery query )
        throws MoolahDataException
    {
        try
        {
            return couch.getViewListing( new MoolahViewRequest( query, MoolahView.account_entries ), AccountEntry.class );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to entries for account query: %s. Error: %s", e, query,
                                           e.getMessage() );
        }
    }

    public List<Transaction> getTransactions( final AccountQuery query )
        throws MoolahDataException
    {
        final MoolahViewRequest req = new MoolahViewRequest( query, MoolahView.transactions );
        try
        {
            return couch.getViewListing( req, Transaction.class );
        }
        catch ( final CouchDBException e )
        {
            throw new MoolahDataException( "Failed to retrieve transaction list using account query: %s. Error: %s", e,
                                           query, e.getMessage() );
        }
    }

}
