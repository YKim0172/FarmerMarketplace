import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FarmerGUI implements Runnable{
    Farmer user;

    JLabel dash = new JLabel("-");
    JFrame mainMenu;
    JButton logout;
    JButton purchaseItem;
    JButton listItem;
    JButton removeItem;
    JButton viewPurchaseHistory;
    JButton viewSalesHistory;

    JFrame salesFilters;
    JTextField priceLow;
    JTextField priceHigh;
    JLabel type;
    JComboBox<SaleType> types;
    JButton confirmFilters;

    JFrame salesBrowsing;
    JComboBox<Sales> items;

    JFrame listForPurchase;
    JTextField name;
    JTextField description;
    JTextField price;
    JButton listItemForSale;

    public FarmerGUI(Farmer user) {
        this.user = user;
    }

    public void run() {
        types = new JComboBox<>();
        types.addItem(null);
        types.addItem(SaleType.CROP);
        types.addItem(SaleType.LAND);
        types.addItem(SaleType.TOOL);
        types.addItem(SaleType.LIVESTOCK);

        mainMenu = new JFrame(user.getName() + ": Main Menu");
        Container mainContent = mainMenu.getContentPane();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainMenu.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainMenu.setSize(450, 200);
        mainMenu.setLocationRelativeTo(null);

        JPanel mainPanelTop = new JPanel();
        JPanel mainPanelMiddle = new JPanel();
        JPanel mainPanelBottom = new JPanel();

        purchaseItem = new JButton("Browse Items");
        listItem = new JButton("List Item to Sell");
        removeItem = new JButton("Remove Listed Item");
        logout = new JButton("Logout");
        viewPurchaseHistory = new JButton("View Your Purchase History");
        viewSalesHistory = new JButton("View Your Sales History");

        mainPanelTop.add(purchaseItem);
        mainPanelTop.add(listItem);
        mainPanelTop.add(removeItem);

        mainPanelBottom.add(logout);
        mainPanelMiddle.add(viewPurchaseHistory);
        mainPanelMiddle.add(viewSalesHistory);

        mainContent.add(mainPanelTop);
        mainContent.add(mainPanelMiddle);
        mainContent.add(mainPanelBottom);
        mainMenu.setVisible(true);

        // Going to buy an item
        purchaseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);

                salesFilters = new JFrame("Filter Your Results");
                Container filtersContent = salesFilters.getContentPane();
                filtersContent.setLayout(new BoxLayout(filtersContent, BoxLayout.Y_AXIS));
                salesFilters.setSize(400, 200);
                salesFilters.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                salesFilters.setLocationRelativeTo(mainContent);

                JPanel price = new JPanel();
                JPanel typePanel = new JPanel();

                JLabel priceLabel = new JLabel("Enter your price range here");
                priceLow = new JTextField("", 8);
                priceHigh = new JTextField("", 8);
                price.add(priceLabel);
                price.add(priceLow);
                price.add(dash);
                price.add(priceHigh);
                filtersContent.add(price);

                type = new JLabel("Select the sale type you would like to view");
                typePanel.add(type);
                typePanel.add(types);
                filtersContent.add(typePanel);

                JPanel confirmFiltersPanel = new JPanel();
                confirmFilters = new JButton("Select Filters");
                confirmFiltersPanel.add(confirmFilters);
                filtersContent.add(confirmFiltersPanel);
                salesFilters.setVisible(true);

                confirmFilters.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String low = priceLow.getText();
                        String high = priceHigh.getText();
                        double lowRange = -1;
                        double highRange = Double.MAX_VALUE;
                        if (!low.isBlank()) {
                            try {
                                lowRange = Double.parseDouble(low);
                                if (lowRange < 0) {
                                    throw new IllegalArgumentException();
                                }
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(salesFilters, "Please enter a valid number " +
                                                "for your lower bound",
                                        "Filters", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        if (!high.isBlank()) {
                            try {
                                highRange = Double.parseDouble(high);
                                if (highRange < lowRange) {
                                    throw new IllegalArgumentException();
                                }
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(salesFilters, "Please enter a valid number for" +
                                        "your upper bound.\nYour value must be at least as much as your lower bound",
                                        "Filters", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        SaleType saleTypeFilter = (SaleType) types.getSelectedItem();
                        salesFilters.dispose();
                        // TODO Communicate w/ server to get sales list
                        salesBrowsing = new JFrame("Items for Sale");
                        salesBrowsing.setSize(500, 200);
                        salesBrowsing.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        salesBrowsing.setLocationRelativeTo(mainContent);
                        Container salesBrowsingContent = salesBrowsing.getContentPane();
                        salesBrowsingContent.setLayout(new BoxLayout(salesBrowsingContent, BoxLayout.Y_AXIS));
                        JPanel itemsPanel = new JPanel();
                        items = new JComboBox<>();
                        items.setMaximumRowCount(15);
                        /* for (int i = 0; i < list.size(); i++)
                            if (get(i).price() >= lowRange && get(i).price() <= highRange) {
                                if (saleTypeFilter == null || get(i).type == saleTypeFilter) {
                                    items.addItem(get(i));
                                }
                            }
                        }
                         */
                        itemsPanel.add(items);
                        salesBrowsingContent.add(itemsPanel);
                        salesBrowsing.setVisible(true);
                        items.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                Sales selection = (Sales) items.getSelectedItem();
                                String selectionInfo = selection.toString() + "\n" + selection.getDescription()
                                        + "\nSeller: " + selection.getUsername() + "\n";
                                selectionInfo += "Would you like to purchase this item?";
                                int choice = JOptionPane.showConfirmDialog(salesBrowsingContent, selectionInfo,
                                        "Purchase", JOptionPane.YES_NO_OPTION);
                                if (choice == JOptionPane.YES_OPTION) {
                                    // TODO Server Interaction
                                    JOptionPane.showMessageDialog(mainMenu, "Purchase successful! " +
                                                    "Returning to main menu...", "Purchase",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    salesBrowsing.dispose();
                                }
                            }
                        });
                        salesBrowsing.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                mainMenu.setVisible(true);
                            }
                        });
                    }
                });
            }
        });

        // Going to list an item
        listItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);

                listForPurchase = new JFrame("List an Item");
                listForPurchase.setSize(400, 400);
                listForPurchase.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                listForPurchase.setLocationRelativeTo(mainMenu);
                Container purchaseContent = listForPurchase.getContentPane();
                purchaseContent.setLayout(new BoxLayout(purchaseContent, BoxLayout.Y_AXIS));

                JPanel namePanel = new JPanel();
                JLabel nameLabel = new JLabel("Enter name of item:");
                name = new JTextField("", 15);
                namePanel.add(nameLabel);
                namePanel.add(name);
                purchaseContent.add(namePanel);

                JLabel priceLabel = new JLabel("Enter price of item:");
                price = new JTextField("", 8);
                JPanel pricePanel = new JPanel();
                pricePanel.add(priceLabel);
                pricePanel.add(price);
                purchaseContent.add(pricePanel);

                JPanel descriptionPanel = new JPanel();
                JLabel descriptionLabel = new JLabel("Enter brief description for your item:");
                description = new JTextField("", 25);
                descriptionPanel.add(descriptionLabel);
                descriptionPanel.add(description);
                purchaseContent.add(descriptionPanel);

                JPanel typePanel = new JPanel();
                JLabel typeLabel = new JLabel("If applicable, select the appropriate type of your item:");
                typePanel.add(typeLabel);
                typePanel.add(types);
                purchaseContent.add(typePanel);

                JPanel buttonPanel = new JPanel();
                listItemForSale = new JButton("List this item for sale");
                buttonPanel.add(listItemForSale);
                purchaseContent.add(listItemForSale);

                listForPurchase.setVisible(true);

                listForPurchase.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainMenu.setVisible(true);
                    }
                });

                listItemForSale.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        double newPrice;
                        try {
                            newPrice = Double.parseDouble(price.getText());
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(listForPurchase, "Please enter a valid " +
                                    "number for your price", "Listing Item", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String newName = name.getText();
                        String newDescription = description.getText();
                        SaleType newType = (SaleType) types.getSelectedItem();
                        try {
                            Sales item = new Sales(user.getUsername(), newName, newPrice, newDescription, newType);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(listForPurchase, ex.getMessage(),
                                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        // TODO Server interaction w/ item
                        JOptionPane.showMessageDialog(listForPurchase, "Item successfully listed",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            }
        });

        // Logout
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.dispose();
            }
        });
    }

    public static void main(String[] args) {
        FarmerGUI f = new FarmerGUI(new Farmer("h", "h", "h"));
        SwingUtilities.invokeLater(f);
    }
}
