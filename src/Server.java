import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println(InetAddress.getLocalHost());
        ServerSocket ss = new ServerSocket(1234);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Farmers.txt"))) {
        } catch (Exception a) {
            ArrayList<Farmer> farmerArrayList = new ArrayList<>();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Farmers.txt"))) {
                oos.writeObject(farmerArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sales.txt"))) {
        } catch (Exception a) {
            ArrayList<Sales> salesArrayList = new ArrayList<>();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Sales.txt"))) {
                oos.writeObject(salesArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (true) {
            System.out.println("accepting input");
            Socket socket = ss.accept();
            SecondaryServer server = new SecondaryServer(socket);
            new Thread(server).start();
        }
    }
}
