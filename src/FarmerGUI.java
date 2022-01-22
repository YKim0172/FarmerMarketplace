import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class FarmerGUI implements Runnable{
    Farmer user;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    PrintWriter pw;
    BufferedReader br;

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

    JFrame removeItems;
    JComboBox<Sales> accountItems;

    JFrame purchaseHistory;
    JLabel purchaseHistoryList;

    JFrame salesHistory;
    JLabel salesHistoryList;

    // Formatted string w/ description and other additional information
    public String detailedSalesInformation(Sales item) {
        return item.toString() + "\n" + item.getDescription()
                + "\nSeller: " + item.getUsername() + "\n";
    }

    public FarmerGUI(Farmer user, ObjectOutputStream oos, ObjectInputStream ois, PrintWriter pw, BufferedReader br) {
        this.user = user;
        this.oos = oos;
        this.ois = ois;
        this.pw = pw;
        this.br = br;
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
                salesFilters.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                salesFilters.setLocationRelativeTo(mainContent);

                salesFilters.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainMenu.setVisible(true);
                    }
                });

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

                        pw.write("requestSalesList");
                        pw.println();
                        pw.flush();

                        ArrayList<Sales> itemsList;
                        try {
                            itemsList = (ArrayList<Sales>) ois.readObject();
                        } catch (IOException | ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(salesFilters, "There was an issue connecting with" +
                                    "the server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        items = new JComboBox<>();
                        items.setMaximumRowCount(15);
                        boolean foundMatch = false;
                        for (Sales sales : itemsList) {
                            if (sales.getPrice() >= lowRange && sales.getPrice() <= highRange) {
                                if (saleTypeFilter == null || sales.getType() == saleTypeFilter) {
                                    items.addItem(sales);
                                    foundMatch = true;
                                }
                            }
                        }

                        if (!foundMatch) {
                            JOptionPane.showMessageDialog(salesFilters, "No listed items match your filters",
                                    "No Items Found", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        salesFilters.dispose();
                        salesBrowsing = new JFrame("Items for Sale");
                        salesBrowsing.setSize(500, 200);
                        salesBrowsing.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        salesBrowsing.setLocationRelativeTo(mainContent);
                        Container salesBrowsingContent = salesBrowsing.getContentPane();
                        salesBrowsingContent.setLayout(new BoxLayout(salesBrowsingContent, BoxLayout.Y_AXIS));

                        JPanel itemsPanel = new JPanel();
                        itemsPanel.add(items);
                        salesBrowsingContent.add(itemsPanel);

                        salesBrowsing.setVisible(true);

                        items.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                Sales selection = (Sales) items.getSelectedItem();
                                String selectionInfo = detailedSalesInformation(selection);
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
                        Sales item;
                        try {
                            item = new Sales(user.getUsername(), newName, newPrice, newDescription, newType);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(listForPurchase, ex.getMessage(),
                                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        pw.write("postingSellOffer");
                        pw.println();
                        pw.flush();

                        try {
                            oos.writeObject(item);
                            oos.flush();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "There was an issue communicating" +
                                    "with the server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        JOptionPane.showMessageDialog(listForPurchase, "Item successfully listed",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                        listForPurchase.dispose();
                    }
                });
            }
        });

        // Remove Selected Item
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);

                removeItems = new JFrame("Remove Listed Item");
                removeItems.setSize(400, 400);
                removeItems.setLocationRelativeTo(mainContent);
                removeItems.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                Container removeItemsContent = removeItems.getContentPane();
                removeItemsContent.setLayout(new BoxLayout(removeItemsContent, BoxLayout.Y_AXIS));

                JPanel items = new JPanel();
                accountItems = new JComboBox<>();
                accountItems.setMaximumRowCount(15);
                // TODO Communicate w/ server, loop to add to combobox
                items.add(accountItems);
                removeItemsContent.add(items);
                removeItems.setVisible(true);

                accountItems.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        Sales selection = (Sales) accountItems.getSelectedItem();
                        String selectioninfo = detailedSalesInformation(selection);
                        selectioninfo += "Are you sure you would like to remove this item from the shopping list?";
                        int choice = JOptionPane.showConfirmDialog(removeItemsContent, selectioninfo, "Remove Item",
                                JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            // TODO Server Interaction
                            JOptionPane.showMessageDialog(removeItemsContent, "Item has successfully " +
                                    "been removed", "Success", JOptionPane.INFORMATION_MESSAGE);
                            removeItems.dispose();
                        }
                    }
                });
                removeItems.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainMenu.setVisible(true);
                    }
                });
            }
        });

        // Purchase History
        viewPurchaseHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);

                purchaseHistory = new JFrame(user.getUsername() + ": Purchase History");
                Container purchaseHistoryContent = purchaseHistory.getContentPane();
                purchaseHistory.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                purchaseHistory.setLocationRelativeTo(mainContent);
                purchaseHistoryContent.setLayout(new BoxLayout(purchaseHistoryContent, BoxLayout.Y_AXIS));

                JPanel purchaseHistoryPanel = new JPanel();
                String history = "<html>";
                // TODO Communicate w/ server, loop to get all info
                history += "</html>";
                purchaseHistoryList = new JLabel(history);
                purchaseHistoryPanel.add(purchaseHistoryList);
                purchaseHistoryContent.add(purchaseHistoryPanel);

                purchaseHistory.pack();
                purchaseHistory.setVisible(true);

                purchaseHistory.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainMenu.setVisible(true);
                    }
                });
            }
        });

        // Selling History
        viewSalesHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.setVisible(false);

                salesHistory = new JFrame(user.getUsername() + ": Purchase History");
                Container salesHistoryContent = salesHistory.getContentPane();
                salesHistory.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                salesHistory.setLocationRelativeTo(mainContent);
                salesHistoryContent.setLayout(new BoxLayout(salesHistoryContent, BoxLayout.Y_AXIS));

                JPanel salesHistoryPanel = new JPanel();
                String history = "<html>";
                // TODO Communicate w/ server, loop to get all info
                history += "</html>";
                salesHistoryList = new JLabel(history);
                salesHistoryPanel.add(purchaseHistoryList);
                salesHistoryContent.add(salesHistoryPanel);

                salesHistory.pack();
                salesHistory.setVisible(true);

                salesHistory.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        mainMenu.setVisible(true);
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
        //FarmerGUI f = new FarmerGUI(new Farmer("h", "h", "h"));
        //SwingUtilities.invokeLater(f);
    }
}
