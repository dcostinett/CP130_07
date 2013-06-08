package app;

import edu.uw.danco.remote.RemoteBrokerGateway;
import edu.uw.danco.remote.RemoteBrokerSession;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
public class RmiBrokerClient {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(RmiBrokerClient.class.getName());

    /**  The port to bind the registry to*/
    private static final int REGISTRY_PORT = 11099;


    /**
     * Entry point for client
     * @param args
     */
    public static void main(String[] args) {
        String SERVER_NAME = "RemoteBrokerGateway";

        try {
            Registry reg = LocateRegistry.getRegistry(REGISTRY_PORT);
            RemoteBrokerGateway server = (RemoteBrokerGateway) reg.lookup(SERVER_NAME);

            RemoteBrokerSession session = server.createAccount("TestAccount", "password", 100000);
            assertNotNull(session);
            assertTrue(session.getBalance() == 100000);

            session = server.login("TestAccount", "password");

            session.deleteAccount();

        } catch (NotBoundException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (RemoteException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
