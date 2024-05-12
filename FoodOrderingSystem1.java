import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FoodOrderingSystem1 extends JFrame implements ActionListener {
    private JLabel textHeader, Description, userLabel;
    private JLabel MealsLabel, chapatiLabel, teaLabel, Fried_riceLabel, DrinksLabel, mandaziLabel;
    private JSpinner MealsSpinner, chapatiSpinner, teaSpinner, Fried_riceSpinner, DrinksSpinner, mandaziSpinner;
    private JButton b;
    private Connection connection;
    private String loggedInUsername;

    public FoodOrderingSystem1(Connection connection, String loggedInUsername) {
        this.connection = connection;
        this.loggedInUsername = loggedInUsername;

        setTitle("Food Ordering System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textHeader = new JLabel("FOOD ORDERING");
        textHeader.setFont(new Font("Times New Roman", Font.PLAIN, 35));

        Description = new JLabel();
        Description.setText("Prepared by Saalam");
        Description.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        MealsLabel = new JLabel("Meals @ 60");
        chapatiLabel = new JLabel("Chapati @ 40");
        teaLabel = new JLabel("Tea @ 10");
        Fried_riceLabel = new JLabel("Fried_rice @ 70");
        DrinksLabel = new JLabel("Drinks @ 20");
        mandaziLabel = new JLabel("Mandazi @ 15");

        MealsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        chapatiSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        teaSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        Fried_riceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        DrinksSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        mandaziSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        userLabel = new JLabel("Logged in as: " + loggedInUsername);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        b = new JButton("ORDER");
        b.addActionListener(this);

        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        container.add(textHeader, gbc);

        addLabelWithSpinner(container, gbc, MealsLabel, MealsSpinner);
        addLabelWithSpinner(container, gbc, chapatiLabel, chapatiSpinner);
        addLabelWithSpinner(container, gbc, teaLabel, teaSpinner);
        addLabelWithSpinner(container, gbc, Fried_riceLabel, Fried_riceSpinner);
        addLabelWithSpinner(container, gbc, DrinksLabel, DrinksSpinner);
        addLabelWithSpinner(container, gbc, mandaziLabel, mandaziSpinner);

        container.add(b, gbc);
        container.add(userLabel, gbc);

        MealsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        chapatiLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        teaLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        Fried_riceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        DrinksLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        mandaziLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        GridBagConstraints gbcDesc = new GridBagConstraints();
        gbcDesc.anchor = GridBagConstraints.SOUTHEAST;
        gbcDesc.insets = new Insets(5, 5, 5, 5);
        container.add(Description, gbcDesc);

        setVisible(true);
    }

    public static void main(String[] args) {
        // This class is invoked from the LoginRegistrationSystem after successful login.
        // So, the main method will not be used for launching this window.
    }

    private void addLabelWithSpinner(Container container, GridBagConstraints gbc, JLabel label, JSpinner spinner) {
        container.add(label, gbc);

        GridBagConstraints spinnerGBC = (GridBagConstraints) gbc.clone();
        spinnerGBC.anchor = GridBagConstraints.EAST;
        spinnerGBC.insets = new Insets(-30, 5, 25, 0);
        spinner.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        container.add(spinner, spinnerGBC);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        float amount = 0;
        String msg = "";
        float n = 0;
        if (((int) MealsSpinner.getValue()) > 0) {
            int quantity = (int) MealsSpinner.getValue();
            n = 60 * quantity;
            amount += 60 * quantity;
            msg = "Meals        : " + quantity + " x 60 => " + n + "\n";
        }
        if (((int) chapatiSpinner.getValue()) > 0) {
            int quantity = (int) chapatiSpinner.getValue();
            n = 40 * quantity;
            amount += 40 * quantity;
            msg += "Chapati    : " + quantity + " x 40 => " + n + "\n";
        }
        if (((int) teaSpinner.getValue()) > 0) {
            int quantity = (int) teaSpinner.getValue();
            n = 10 * quantity;
            amount += 10 * quantity;
            msg += "Tea           : " + quantity + " x 10 => " + n + "\n";
        }
        if (((int) Fried_riceSpinner.getValue()) > 0) {
            int quantity = (int) Fried_riceSpinner.getValue();
            amount += 70 * quantity;
            msg += "FriedRice : " + quantity + " x 70 => " + n + "\n";
        }
        if (((int) DrinksSpinner.getValue()) > 0) {
            int quantity = (int) DrinksSpinner.getValue();
            n = 20 * quantity;
            amount += 20 * quantity;
            msg += "Drinks      : " + quantity + " x 20 => " + n + "\n";
        }
        if (((int) mandaziSpinner.getValue()) > 0) {
            int quantity = (int) mandaziSpinner.getValue();
            n = 15 * quantity;
            amount += 15 * quantity;
            msg += "Mandazi   : " + quantity + " x 15 => " + n + "\n";
        }
        msg += "_____________\n";
        if (amount != 0) {


            saveReceiptToDatabase(loggedInUsername, msg, amount);

            showReceipt(msg, amount);
        } else {
            String[] buttons = {"Go back"};
            JOptionPane.showOptionDialog(null, "Please select any item", "Error",
                    JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[0]);
        }
    }

    private void saveReceiptToDatabase(String username, String items, float totalAmount) {
        try {
            String query = "INSERT INTO receipts (username, items, total_amount) VALUES (?,  ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, items);
            statement.setFloat(3, totalAmount);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showReceipt(String items, float totalAmount) {
        JFrame frame = new JFrame();
        Font font = new Font("Arial", Font.PLAIN, 20);

        JOptionPane optionPane = new JOptionPane(
                "RECEIPT\n\n" + "Token Number: " + "\n\n" + items + "Total = " + totalAmount +
                        "\n\n CONTACT: saalam121@gmail.com", JOptionPane.PLAIN_MESSAGE);
        optionPane.setFont(font);

        JButton proceedButton = new JButton("Proceed to pay " + totalAmount);
        proceedButton.setFont(font);
        proceedButton.addActionListener(e -> {
            optionPane.setValue(JOptionPane.OK_OPTION);
            new PaymentWindow(connection, loggedInUsername, totalAmount);
            dispose();
        });

        optionPane.setOptions(new Object[]{proceedButton});

        JDialog dialog = optionPane.createDialog(frame, "Receipt Dialog");
        dialog.setVisible(true);
    }
}
