package org.commonjava.moolah.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.enterprise.util.AnnotationLiteral;

import org.apache.log4j.Level;
import org.cjtest.fixture.CouchWeldFixture;
import org.commonjava.moolah.data.view.AccountList;
import org.commonjava.moolah.inject.Moolah;
import org.commonjava.moolah.model.Account;
import org.commonjava.moolah.model.MoolahDataException;
import org.commonjava.util.logging.Log4jUtil;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MoolahDataManagerTest
{

    private final WeldContainer weld = new Weld().initialize();

    public CouchWeldFixture couchFixture = new CouchWeldFixture( weld, new AnnotationLiteral<Moolah>()
    {
        private static final long serialVersionUID = 1L;
    } );

    private MoolahDataManager dataManager;

    @BeforeClass
    public static void setupClass()
    {
        Log4jUtil.configure( Level.DEBUG );
    }

    @Before
    public void setup()
        throws MoolahDataException
    {
        dataManager = weld.instance()
                          .select( MoolahDataManager.class )
                          .get();

        dataManager.install();
    }

    @Test
    public void storeAccountAndFindInAccountList()
        throws MoolahDataException
    {
        final Account acct = new Account.Builder( "1001", "Mad Money" ).build();

        dataManager.storeAccount( acct );
        final AccountList list = dataManager.getAccountList();

        final List<Account> accounts = list.getAccounts();
        assertThat( accounts, notNullValue() );
        assertThat( accounts.size(), equalTo( 1 ) );
        assertThat( accounts.get( 0 )
                            .getId(), equalTo( acct.getId() ) );
    }

}
