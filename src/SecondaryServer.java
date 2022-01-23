import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class SecondaryServer implements Runnable {
    Socket socket;

    /*
     * sending and receiving strings
     */
    BufferedReader bfr;
    PrintWriter pw;

    /*
     * sending and receiving objects
     */

    ObjectInputStream ois;
    ObjectOutputStream oos;

    static ArrayList<Socket> allUsers;  //list of all clients that are connected to the server
    public SecondaryServer(Socket socket) {
        /*
         * We made the socket object so the client is connected to the server
         */
        this.socket = socket;
        if (allUsers == null) {
            allUsers = new ArrayList<>();
        }
        allUsers.add(this.socket);

        try {
            this.bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream());
            //Thread.sleep(500);

            this.oos = new ObjectOutputStream(socket.getOutputStream());
            //Thread.sleep(500);
            this.ois = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        while (true) {
            try {
                String action = bfr.readLine();

                if (action.equals("createAccount")) {
                    String name = bfr.readLine();
                    String username = bfr.readLine();
                    String password = bfr.readLine();

                    if (accountValid(username)) {  //make account
                        Farmer newFarmer = new Farmer(name, username, password);  //make new farmer object
                        ArrayList<Farmer> farmerList = getFarmerList();
                        farmerList.add(newFarmer);
                        updateFarmerList(farmerList);

                        pw.write("accountMade");
                        pw.println();
                        pw.flush();

                        oos.writeObject(newFarmer);
                        oos.flush();
                    } else {  //username already taken
                        pw.write("accountNotMade");
                        pw.println();
                        pw.flush();
                    }

                } else if (action.equals("login")) {
                    String username = bfr.readLine();
                    String password = bfr.readLine();

                    if (loggedIn(username, password) != null) {
                        pw.write("loggedIn");
                        pw.println();
                        pw.flush();

                        oos.writeObject(loggedIn(username, password));
                        oos.flush();
                    } else {
                        pw.write("cantLogin");
                        pw.println();
                        pw.flush();
                    }
                } else if (action.equals("postingSellOffer")) {
                    Sales theSale = (Sales) ois.readObject();
                    ArrayList<Sales> theSalesList = getSalesList();
                    theSalesList.add(theSale);
                    updateSalesList(theSalesList);

                } else if (action.equals("requestSalesList")) {
                    ArrayList<Sales> theSalesList = getSalesList();
                    oos.writeObject(theSalesList);
                    oos.flush();

                } else if (action.equals("requestUserSalesList")) {
                    String username = bfr.readLine();
                    ArrayList<Sales> specificUserSales = new ArrayList<>();
                    for (Sales s: getSalesList()) {
                        if (s.getUsername().equals(username)) {
                            specificUserSales.add(s);
                        }
                    }
                    oos.writeObject(specificUserSales);
                    oos.flush();
                } else if (action.equals("removeSale")) {
                    Sales saleToRemove = (Sales) ois.readObject();
                    ArrayList<Sales> salesList = getSalesList();
                    for (int i = 0; i < salesList.size(); i++) {
                        Sales current = salesList.get(i);
                        if (current.equals(saleToRemove)) {
                            salesList.remove(i);
                            updateSalesList(salesList);
                            break;
                        }
                    }
                } else if (action.equals("buySale")) {
                    Sales soldObject = (Sales) ois.readObject();
                    ArrayList<Sales> saleList = getSalesList();
                    for (int i = 0; i < saleList.size(); i++) {
                        if (soldObject.equals(saleList)) {
                            saleList.remove(i);
                            updateSalesList(saleList);
                            break;
                        }
                    }
                }

            } catch (Exception e) {

            }

        }
    }

    public static ArrayList<Farmer> getFarmerList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Farmers.txt"))) {
            Object o = ois.readObject();
            return (ArrayList<Farmer>) o;
        } catch (Exception e) {
            return null;
        }
    }

    public static void updateFarmerList(ArrayList<Farmer> farmerList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Farmers.txt"))) {
            oos.writeObject(farmerList);
        } catch (Exception e) {

        }
    }

    public boolean accountValid(String username) {
        for(Farmer f: getFarmerList()) {
            if (f.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public Farmer loggedIn(String username, String password) {

        for (Farmer f : getFarmerList()) {
            if (f.getUsername().equals(username) && f.getPassword().equals(password)) {
                return f;
            }
        }
        return null;
    }

    public static ArrayList<Sales> getSalesList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sales.txt"))) {
            Object o = ois.readObject();
            return (ArrayList<Sales>) o;
        } catch (Exception e) {
            return null;
        }
    }

    public static void updateSalesList(ArrayList<Sales> salesList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Sales.txt"))) {
            oos.writeObject(salesList);
        } catch (Exception e) {

        }
    }
}
