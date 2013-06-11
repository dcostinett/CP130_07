package app;

import edu.uw.danco.remote.RemoteBrokerGateway;
import edu.uw.danco.remote.RemoteBrokerGatewayImpl;
import edu.uw.danco.remote.RemoteBrokerSession;
import edu.uw.ext.framework.exchange.StockExchangeSpi;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * User: dcostinett
 * Date: 6/5/13
 */
public class RmiBrokerServer {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(RmiBrokerServer.class.getName());

    /**  The port to bind the registry to*/
    private static final int REGISTRY_PORT = 11099;


    /**
     * Entry point for server test
     * @param args
     */
    public static void main(String[] args) {
        String SERVER_NAME = "RemoteBrokerGateway";
        //System.setSecurityManager(new RMISecurityManager());
        try {
            Registry reg = LocateRegistry.createRegistry(REGISTRY_PORT);
            // try to create, if it fails, use LocateRegistry.getRegistry
            StockExchangeSpi exchange = ExchangeFactory.newTestStockExchange();
            exchange.open();

            RemoteBrokerGateway server = new RemoteBrokerGatewayImpl(exchange);
            reg.rebind(SERVER_NAME, server);

            // a real app should call unbind and unexport the unicast remote object upon completion.
        }
        catch(Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
