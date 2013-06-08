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
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public int getBalance() throws RemoteException {
        return account.getBalance();
    }


    /**
     *
     * @throws RemoteException
     */
    @Override
    public void deleteAccount() throws RemoteException {
        try {
            LOGGER.info("Deleting account: " + account.getName());
            broker.deleteAccount(account.getName());
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     *
     * @param ticker - the stock symbol for which to get the quote
     * @return - a StockQuote for the specified ticker
     * @throws RemoteException
     */
    @Override
    public StockQuote requestQuote(String ticker) throws RemoteException {
        try {
            LOGGER.info("Quote requested for: " + ticker);
            return broker.requestQuote(ticker);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     *
     * @param order - the market order
     * @throws RemoteException
     */
    @Override
    public void placeMarketBuyOrder(final MarketBuyOrder order) throws RemoteException {
        try {
            LOGGER.info("Placing MarketBuyOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     *
     * @param order - the market order
     * @throws RemoteException
     */
    @Override
    public void placeMarketSellOrder(final MarketSellOrder order) throws RemoteException {
        try {
            LOGGER.info("Placing MarketSellOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     *
     * @param order - the stop buy order
     * @throws RemoteException
     */
    @Override
    public void placeStopBuyOrder(final StopBuyOrder order) throws RemoteException {
        try {
            LOGGER.info("Placing StopBuyOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     *
     * @param order - the stop sell order
     * @throws RemoteException
     */
    @Override
    public void placeStopSellOrder(final StopSellOrder order) throws RemoteException {
        try {
            LOGGER.info("Placing StopSellOrder: " + order);
            broker.placeOrder(order);
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }


    /**
     *
     * @throws RemoteException
     */
    @Override
    public void close() throws RemoteException {
        try {
            LOGGER.info("Closing the broker.");
            broker.close();
        } catch (BrokerException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }
}
