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
    StockQuote requestQuote(String ticker) throws RemoteException;


    /**
     * Places a market buy order with the broker
     * @param numberOfShares
     * @param ticker
     * @throws RemoteException
     */
    void placeMarketBuyOrder(int numberOfShares, String ticker) throws RemoteException;


    /**
     * Places a market sell order with the broker
     * @param numberOfShares
     * @param ticker
     * @throws RemoteException
     */
    void placeMarketSellOrder(int numberOfShares, String ticker) throws RemoteException;


    /**
     *
     * @param numberOfShares
     * @param ticker
     * @param stopPrice
     * @throws RemoteException
     */
    void placeStopBuyOrder(int numberOfShares,
                           String ticker,
                           int stopPrice) throws RemoteException;


    /**
     *
     * @param numberOfShares
     * @param ticker
     * @param stopPrice
     * @throws RemoteException
     */
    void placeStopSellOrder(int numberOfShares,
                            String ticker,
                            int stopPrice) throws RemoteException;


    /**
     * Closes the session and releases any resources
     */
    void close() throws RemoteException;
}
