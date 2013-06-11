package edu.uw.danco.remote;

import edu.uw.ext.framework.account.Account;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.broker.BrokerException;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

/**
 * Author: dcostinett
 * Date: 06/2013
 */
public class RemoteBrokerSessionImpl extends UnicastRemoteObject implements RemoteBrokerSession {
    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(RemoteBrokerSessionImpl.class.getName());

    /** The broker used to execute transactions */
    private Broker broker;

    /** The account */
    private Account account;


    /**
     * Instantiate a remote session for the specified broker and account
     * @param broker
     * @param account
     */
    public RemoteBrokerSessionImpl(Broker broker, Account account) throws RemoteException {
        this.broker = broker;
        this.account = account;
    }


    /**
     * Gets the balance of the account associated with the session
     * @return - the session's account balance
     * @throws RemoteException
     */
    @Override
    public int getBalance() throws RemoteException {
        checkInvariants();
        return account.getBalance();
    }


    /**
     * Delete the associated account
     * @throws RemoteException
     */
    @Override
    public void deleteAccount() throws RemoteException {
        try {
            checkInvariants();
            LOGGER.info("Deleting account: " + account.getName());
            broker.deleteAccount(account.getName());
            account = null;
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Get a quote for the specified ticker
     * @param ticker - the stock symbol for which to get the quote
     * @return - a StockQuote for the specified ticker
     * @throws RemoteException
     */
    @Override
    public StockQuote requestQuote(String ticker) throws RemoteException {
        try {
            checkInvariants();
            LOGGER.info("Quote requested for: " + ticker);
            return broker.requestQuote(ticker);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Buy the specified number of shares at market price for the specified ticker
     * @param numberOfShares
     * @param ticker
     * @throws RemoteException
     */
    @Override
    public void placeMarketBuyOrder(final int numberOfShares, final String ticker)
            throws RemoteException {
        try {
            checkInvariants();
            final MarketBuyOrder order = new MarketBuyOrder(account.getName(), numberOfShares, ticker);
            LOGGER.info("Placing MarketBuyOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Sell the specified number of shares at market price for the specified ticker
     * @param numberOfShares
     * @param ticker
     * @throws RemoteException
     */
    @Override
    public void placeMarketSellOrder(final int numberOfShares, final String ticker)
            throws RemoteException {
        try {
            checkInvariants();
            final MarketSellOrder order = new MarketSellOrder(account.getName(), numberOfShares, ticker);
            LOGGER.info("Placing MarketSellOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Place a StopBuyOrder for the specified number of shares at the specified price for the specified ticker
     * @param numberOfShares
     * @param ticker
     * @param stopPrice
     * @throws RemoteException
     */
    @Override
    public void placeStopBuyOrder(final int numberOfShares,
                                  final String ticker,
                                  final int stopPrice) throws RemoteException {
        try {
            checkInvariants();
            final StopBuyOrder order =
                    new StopBuyOrder(account.getName(), numberOfShares, ticker, stopPrice);
            LOGGER.info("Placing StopBuyOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Place a StopSellOrder for the specified number of shares at the specified price for the specified ticker
     * @param numberOfShares
     * @param ticker
     * @param stopPrice
     * @throws RemoteException
     */
    @Override
    public void placeStopSellOrder(final int numberOfShares,
                                   final String ticker,
                                   final int stopPrice) throws RemoteException {
        try {
            checkInvariants();
            final StopSellOrder order =
                    new StopSellOrder(account.getName(), numberOfShares, ticker, stopPrice);
            LOGGER.info("Placing StopSellOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Close the broker
     * @throws RemoteException
     */
    @Override
    public void close() throws RemoteException {
        try {
            LOGGER.info("Closing the broker.");
            if (broker != null) {
                broker.close();
                account = null;
                broker = null;
            }
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     * Verify object state
     */
    private void checkInvariants() {
        if (account == null ||
                broker == null ) {
            throw new IllegalStateException("Broker session is not properly initialized, or has been closed.");
        }
    }
}
