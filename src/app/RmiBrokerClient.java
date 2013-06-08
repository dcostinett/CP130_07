package app;

import edu.uw.danco.broker.BrokerImpl;
import edu.uw.danco.remote.RemoteBrokerGateway;
import edu.uw.danco.remote.RemoteBrokerSession;
import edu.uw.ext.framework.broker.Broker;
import edu.uw.ext.framework.exchange.StockExchange;
import edu.uw.ext.framework.exchange.StockExchangeSpi;
import edu.uw.ext.framework.exchange.StockQuote;
import edu.uw.ext.framework.order.MarketBuyOrder;
import edu.uw.ext.framework.order.MarketSellOrder;
import edu.uw.ext.framework.order.StopBuyOrder;
import edu.uw.ext.framework.order.StopSellOrder;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * User: dcostinett
 * Date: 6/5/13
 */
public class RmiBrokerClient {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(RmiBrokerClient.class.getName());

    /**  The port to bind the registry to*/
    private static final int REGISTRY_PORT = 11099;

    /** The test account ID */
    private static final String accountId = "TestAccount";


    /**
     * Entry point for client
     * @param args
     */
    public static void main(String[] args) {
        String SERVER_NAME = "RemoteBrokerGateway";

        RemoteBrokerSession session = null;
        try {
            Registry reg = LocateRegistry.getRegistry(REGISTRY_PORT);
            RemoteBrokerGateway server = (RemoteBrokerGateway) reg.lookup(SERVER_NAME);

            session = server.createAccount("TestAccount", "password", 100000);
            assertNotNull(session);
            assertTrue(session.getBalance() == 100000);

            session = server.login("TestAccount", "password");

            StockExchangeSpi exchange = ExchangeFactory.newTestStockExchange();

            for (String ticker : exchange.getTickers()) {
                StockQuote quote = session.requestQuote(ticker);
                LOGGER.info(quote.getTicker() + " trading at " + quote.getPrice());
            }

            boolean isOpen = exchange.isOpen();
            exchange.open();
            assertTrue(exchange.isOpen());

            int balance = session.getBalance();
            String testTicker = "F";

            final MarketBuyOrder mbOrder = new MarketBuyOrder(accountId, 10, testTicker);
            session.placeMarketBuyOrder(accountId, 10, testTicker);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.warning("Sleep interrupted.");
            }
            assertEquals(session.getBalance(),
                                balance + mbOrder.valueOfOrder(session.requestQuote(testTicker).getPrice()));
            balance = session.getBalance();

            final MarketSellOrder msOrder = new MarketSellOrder(accountId, 10, testTicker);
            session.placeMarketSellOrder(accountId, 10, testTicker);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.warning("Sleep interrupted.");
            }
            assertEquals(session.getBalance(),
                                balance + msOrder.valueOfOrder(session.requestQuote(testTicker).getPrice()));
            balance = session.getBalance();

            final StopBuyOrder sbOrder =
                    new StopBuyOrder(accountId, 10, testTicker, session.requestQuote(testTicker).getPrice() - 1);
            session.placeStopBuyOrder(accountId, 10, testTicker, session.requestQuote(testTicker).getPrice() - 1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.warning("Sleep interrupted.");
            }
            assertEquals(session.getBalance(),
                                balance + sbOrder.valueOfOrder(session.requestQuote(testTicker).getPrice()));
            balance = session.getBalance();


            final StopSellOrder ssOrder =
                    new StopSellOrder(accountId, 10, testTicker, session.requestQuote(testTicker).getPrice() + 1);
            session.placeStopSellOrder(accountId, 10, testTicker, session.requestQuote(testTicker).getPrice() + 1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.warning("Sleep interrupted.");
            }
            assertEquals(session.getBalance(),
                                balance + ssOrder.valueOfOrder(session.requestQuote(testTicker).getPrice()));
            balance = session.getBalance();

        } catch (NotBoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                session.deleteAccount();
                session.close();
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}
