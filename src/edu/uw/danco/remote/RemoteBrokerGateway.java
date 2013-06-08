package edu.uw.danco.remote;

import edu.uw.ext.framework.account.AccountException;
import edu.uw.ext.framework.exchange.StockExchange;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Author: dcostinett
 * Date: 06/2013
 *
 * This interface is used to obtain a session with the broker. The interface must have just two operations:
 *
 *     createAccount this operation is used to create an account, it should return a reference to a remote session
 *     login this operation is used to login to an existing account, it should return a reference to a remote session.
 */
public interface RemoteBrokerGateway extends Remote {

    /**
     * This operation is used to create an account, it should return a reference to a remote session
     * @param accountName - name of the account
     * @param password - password to be hashed for the account
     * @param balance - opening balance
     * @return - a reference to the remote session
     * @throws AccountException
     * @throws RemoteException
     */
    RemoteBrokerSession createAccount(String accountName,
                                      String password,
                                      int balance)
            throws RemoteException;


    /**
     * This operation is used to login to an existing account, it should return a reference to a remote session
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    RemoteBrokerSession login(String username,
                              String password)
            throws RemoteException;
}
