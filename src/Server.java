import java.lang.*;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4567);


        while (true) {
            Socket socket = ss.accept();
            SecondaryServer server = new SecondaryServer(socket);
            new Thread(server).start();
        }
    }
}
