import java.net.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class SecondaryServer implements Runnable {
    Socket socket;
    BufferedReader bfr;
    PrintWriter pw;
    static ArrayList<Socket> allUsers;  //list of all clients that are connected to the server
    public SecondaryServer(Socket socket) {
        /*
         * We made the socket object so the client is connected to the server
         */
        this.socket = socket;
        if (allUsers.size() == 0) {
            allUsers = new ArrayList<>();
        }
        allUsers.add(this.socket);


    }

    public void run() {
        while (true) {

        }
    }
}
