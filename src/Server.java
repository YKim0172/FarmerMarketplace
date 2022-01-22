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




        /*
         * Creates the PurchaseHistory.txt file and stores the ArrayList of Purchase objects if
         * the text file wasn't already created.
         *
         * By storing Purchase objects in the file, we can ensure all data is saved regarding farmers'
         * purchasing history.
         */
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("PurchaseHistory.txt"))) {
        } catch (Exception a) {
            ArrayList<Purchase> purchaseArrayList = new ArrayList<>();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("PurchaseHistory.txt"))) {
                oos.writeObject(purchaseArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
         * Creates the SellingHistory.txt file and stores the ArrayList of Selling objects if
         * the text file isn't already created.
         *
         * By storing Selling objects in the file, we can ensure all data is saved regarding farmers'
         * selling history
         */
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("SellingHistory.txt"))) {
        } catch (Exception a) {
            ArrayList<Selling> sellingArrayList = new ArrayList<>();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("SellingHistory.txt"))) {
                oos.writeObject(sellingArrayList);
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
