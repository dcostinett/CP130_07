package edu.uw.danco.remote;

import app.ExchangeFactory;
import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.account.AccountManager;
import edu.uw.ext.framework.account.AccountManagerFactory;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerException;
import edu.uw.ext.framework.broker.BrokerFactory;
import edu.uw.ext.framework.dao.AccountDao;
import edu.uw.ext.framework.dao.DaoFactory;
import edu.uw.ext.framework.dao.DaoFactoryException;
import edu.uw.ext.framework.exchange.StockExchange;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: dcostinett
 * Date: 06/2013
 *
 * This interface is used to obtain a session with the broker. The interface must have just two operations:
 *
 * createAccount this operation is used to create an account, it should return a reference to a remote session
 * login this operation is used to login to an existing account, it should return a reference to a remote session.
 */
public class RemoteBrokerGatewayImpl extends UnicastRemoteObject implements RemoteBrokerGateway {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(RemoteBrokerGatewayImpl.class.getName());

    /** The broker that executes the trades */
    private Broker broker;


    /**
     * Constructor
     * @param exchange - the exchange where stocks are traded
     * @throws RemoteException
     */
    public RemoteBrokerGatewayImpl(final StockExchange exchange) throws RemoteException {
        final BeanFactory beanfactory = new FileSystemXmlApplicationContext("context.xml");

        final AccountManagerFactory accountManagerFactory =
                beanfactory.getBean("AccountManagerFactory", AccountManagerFactory.class);

        final BrokerFactory brokerFactory = beanfactory.getBean("BrokerFactory", BrokerFactory.class);
        final DaoFactory daoFact;
        daoFact = beanfactory.getBean("DaoFactory", DaoFactory.class);

        AccountDao dao = null;
        try {
            dao = daoFact.getAccountDao();
            dao.reset();
        } catch (DaoFactoryException e) {
            LOGGER.log(Level.SEVERE, "Exception creating AccountDao: ", e);
            throw new RemoteException(e.getMessage(), e);
        } catch (AccountException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RemoteException(e.getMessage(), e);
        }
        final AccountManager accountManager = accountManagerFactory.newAccountManager(dao);

        broker = brokerFactory.newBroker("Some Broker", accountManager, exchange);
    }

    /**
     *
     * @param accountName - name of the account
     * @param password - password to be hashed for the account
     * @param balance - opening balance
     * @return - reference to a remote broker session
     * @throws RemoteException
     */
    @Override
    public RemoteBrokerSession createAccount(final String accountName,
                                             final String password,
                                             final int balance) throws RemoteException {
        Account account = null;
        try {
            account = broker.createAccount(accountName, password, balance);
        } catch (BrokerException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RemoteException(e.getMessage(), e);
        }

        return new RemoteBrokerSessionImpl(broker, account);
    }


    /**
     * Login to an existing account
     * @return - reference to a remote broker session
     * @throws RemoteException
     */
    @Override
    public RemoteBrokerSession login(final String username, final String password) throws RemoteException {
        Account account = null;
        try {
            account = broker.getAccount(username, password);
        } catch (BrokerException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RemoteException(e.getMessage(), e);
        }

        return new RemoteBrokerSessionImpl(broker, account);
    }
}
