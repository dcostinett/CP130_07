package edu.uw.danco.remote;

import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Author: dcostinett
 * Date: 06/2013
 *
 * The remote broker session will talk to a Broker
 */
public interface RemoteBrokerSession extends Remote {

    /**
     * Returns the balance of session's account
     * @return - account balance
     */
    int getBalance() throws RemoteException;

    /**
     * Deletes the session's account
     */
    void deleteAccount() throws RemoteException;

    /**
     * Obtains the current price of a stock and returns a StockQuote object
     * @param ticker - the stock symbol for which to get the quote
     * @return - a stock quote for the requested stock
     */
    StockQuote requestQuote(final String ticker) throws RemoteException;

    /**
     * Places a market buy order with the broker
     * @param order - the market order
     */
    void placeMarketBuyOrder(final MarketBuyOrder order) throws RemoteException;

    /**
     * Places a market sell order with the broker
     * @param order - the market order
     */
    void placeMarketSellOrder(final MarketSellOrder order) throws RemoteException;

    /**
     * Places a stop buy order with the broker
     * @param order - the stop buy order
     */
    void placeStopBuyOrder(final StopBuyOrder order) throws RemoteException;

    /**
     * Places a stop sell order with the broker
     * @param order - the stop sell order
     */
    void placeStopSellOrder(final StopSellOrder order) throws RemoteException;

    /**
     * CLoses the session and releases any resources
     */
    void close() throws RemoteException;
}
