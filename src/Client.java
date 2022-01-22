import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        // Logging in
        String[] loginOptions = {"Create Account", "Login to Existing Account", "Terminate Program"};
        int selection = JOptionPane.showOptionDialog(null, "Welcome! Please select one of " +
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
            // TODO Communicate w/ server
        } else if (selection == 1) {
            username = JOptionPane.showInputDialog(null, "Please enter your username",
                    "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);
            password = JOptionPane.showInputDialog(null, "Please enter your password",
                    "Farmer Marketplace", JOptionPane.QUESTION_MESSAGE);
            // TODO Communicate w/ server
        }
        if (selection == 0 || selection == 1) {
            // TODO Run GUI
        } else {
            JOptionPane.showMessageDialog(null, "Program is now terminating. " +
                            "Thank you for using Farmer Marketplace.",
                    "Farmer Marketplace", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
