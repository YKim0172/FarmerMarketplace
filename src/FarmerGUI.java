import javax.swing.*;
import java.awt.*;

public class FarmerGUI implements Runnable{
    Farmer user;

    JFrame mainMenu;
    JButton logout;
    JButton purchaseItem;
    JButton listItem;
    JButton viewPurchaseHistory;
    JButton viewSalesHistory;

    JButton returnToMain;

    JFrame salesFilters;
    JTextField priceLow;
    JTextField priceHigh;
    JLabel priceRange;
    JLabel type;
    JComboBox<SaleType> types;

    JFrame salesBrowsing;
    JComboBox<Sales> items;

    JFrame listForPurchase;
    JTextField name;
    JTextField description;
    JTextField price;
    JComboBox<SaleType> itemTypes;

    public FarmerGUI(Farmer user) {
        this.user = user;
    }

    public void run() {
        mainMenu = new JFrame(user.getName() + ": Main Menu");
        Container mainContent = mainMenu.getContentPane();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainMenu.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainMenu.setSize(400, 200);
        mainMenu.setLocationRelativeTo(null);
        JPanel mainPanelTop = new JPanel();
        JPanel mainPanelMiddle = new JPanel();
        JPanel mainPanelBottom = new JPanel();
        purchaseItem = new JButton("Browse Items");
        listItem = new JButton("List Item to Sell");
        logout = new JButton("Logout");
        viewPurchaseHistory = new JButton("View Your Purchase History");
        viewSalesHistory = new JButton("View Your Sales History");
        mainPanelTop.add(purchaseItem);
        mainPanelTop.add(listItem);
        mainPanelBottom.add(logout);
        mainPanelMiddle.add(viewPurchaseHistory);
        mainPanelMiddle.add(viewSalesHistory);
        mainContent.add(mainPanelTop);
        mainContent.add(mainPanelMiddle);
        mainContent.add(mainPanelBottom);
        mainMenu.setVisible(true);
    }

    public static void main(String[] args) {
        FarmerGUI f = new FarmerGUI(new Farmer("h", "h", "h"));
        SwingUtilities.invokeLater(f);
    }
}
