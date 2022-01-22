import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Thread.sleep(500);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Thread.sleep(500);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Logging in
            String[] loginOptions = {"Create Account", "Login to Existing Account", "Terminate Program"};
            int selection;
            Farmer user = null;
            while (true) {
                selection = JOptionPane.showOptionDialog(null, "Welcome! Please select one of " +
                                "the following options", "Farmer Marketplace", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, loginOptions, null);
                String username;
                String password;
                if (selection == 0) {
                    String name = JOptionPane.showInputDialog(null, "Please enter your name",
                            "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);
                    username = JOptionPane.showInputDialog(null, "Please enter your desired " +
                            "username", "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);
                    password = JOptionPane.showInputDialog(null, "Please enter a password for " +
                            "your account", "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);

                    // Send to Server
                    pw.write("createAccount");
                    pw.println();
                    pw.flush();
                    pw.write(name);
                    pw.println();
                    pw.write(username);
                    pw.println();
                    pw.write(password);
                    pw.println();
                    pw.flush();

                    // Receive From Server
                    String message = br.readLine();
                    System.out.println(message);
                    if (message.equals("accountMade")) {
                        user = new Farmer(name, username, password);
                        JOptionPane.showMessageDialog(null, "Successfully Logged In",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "This username is already " +
                                "in use. Please try again", "Create Account", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (selection == 1) {
                    username = JOptionPane.showInputDialog(null, "Please enter your username",
                            "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);
                    password = JOptionPane.showInputDialog(null, "Please enter your password",
                            "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);
                    // Send to server
                    pw.write("login");
                    pw.println();
                    pw.flush();
                    pw.write(username);
                    pw.println();
                    pw.write(password);
                    pw.println();
                    pw.flush();

                    // Receive From Server
                    String message = br.readLine();
                    System.out.println("Message received");
                    if (message.equals("loggedIn")) {
                        user = (Farmer) ois.readObject();
                        JOptionPane.showMessageDialog(null, "Successfully Logged In",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "An account with the given " +
                                "credentials was not found.\nPlease try again.", "Login",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    break;
                }
            }
            if (selection == 0 || selection == 1) {
                // TODO Run GUI
                SwingUtilities.invokeLater(new FarmerGUI(user));
            } else {
                JOptionPane.showMessageDialog(null, "Program is now terminating. " +
                                "Thank you for using Farmer Marketplace.",
                        "Farmer Marketplace", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "There was an issue connecting to the server",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
