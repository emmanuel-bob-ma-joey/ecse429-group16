
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.Assert.assertTrue;

@TestMethodOrder(MethodOrderer.Random.class)
public class StandaloneTest {

	
    @Test
	public void testServerStatus() {
        String serverAddress = Constants.server_address; 
        int serverPort = Constants.port;

        try {
            // Attempt to connect to the server
            Socket socket = new Socket(serverAddress, serverPort);
            assertTrue(!socket.isConnected());

            socket.close();
        } catch (IOException e) {
            // If an exception is thrown, the server is not running
            assertTrue(true);
        }
    }

}
