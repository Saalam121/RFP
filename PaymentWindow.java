import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class PaymentWindow extends JFrame implements ActionListener {
    private String username;
    private float totalAmount;
    private JRadioButton cashOption, cardOption, upiOption;
    private JButton payButton;
    private Connection connection;

    public PaymentWindow(Connection connection, String username, float totalAmount) {
        this.connection = connection;
        this.username = username;
        this.totalAmount = totalAmount;

        setTitle("Payment Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 500)); // Set default panel size
        setResizable(false); // Make frame non-resizable
        setLayout(new GridBagLayout());

        // Create GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create radio buttons
        cashOption = new JRadioButton("Cash");
        cardOption = new JRadioButton("Card");
        upiOption = new JRadioButton("UPI");

        // Group radio buttons
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cashOption);
        paymentGroup.add(cardOption);
        paymentGroup.add(upiOption);

        // Set font and size for radio buttons
        Font radioButtonFont = new Font("Arial", Font.PLAIN, 20);
        cashOption.setFont(radioButtonFont);
        cardOption.setFont(radioButtonFont);
        upiOption.setFont(radioButtonFont);

        // Add radio buttons to layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(cashOption, gbc);

        gbc.gridy = 1;
        add(cardOption, gbc);

        gbc.gridy = 2;
        add(upiOption, gbc);

        // Create Pay button
        payButton = new JButton("Pay");
        payButton.addActionListener(this);
        payButton.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(payButton, gbc);

        // Pack and set location
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == payButton) {
            String paymentMethod = "";
            if (cashOption.isSelected()) {
                paymentMethod = "Cash";
                // Prompt user to pay to the delivery partner
                JOptionPane.showMessageDialog(this, "Please pay $" + totalAmount + " to the delivery partner.",
                        "Payment Information", JOptionPane.INFORMATION_MESSAGE);
            } else if (cardOption.isSelected()) {
                paymentMethod = "Card";
                // Prompt user to enter credit card number
                String cardNumber = JOptionPane.showInputDialog(this, "Enter your credit card number:");
                if (cardNumber != null && !cardNumber.isEmpty()) {
                    // Card number entered, proceed with payment
                    savePaymentInfoToDatabase(username, totalAmount, paymentMethod);
                    JOptionPane.showMessageDialog(this, "Payment Successful! Thank you.",
                            "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the payment window after successful payment
                    new LoginRegistrationSystem(); // Redirect back to login screen
                } else {
                    // User canceled or left input empty
                    JOptionPane.showMessageDialog(this, "Payment canceled or card number not entered.",
                            "Payment Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (upiOption.isSelected()) {
                paymentMethod = "UPI";
                // Generate and show a random QR code
                String qrCode = generateRandomQRCode();
                JOptionPane.showMessageDialog(this, "Scan the QR code below to complete the payment:\n\n" + qrCode,
                        "UPI QR Code", JOptionPane.PLAIN_MESSAGE);
            }

            if (!paymentMethod.isEmpty() && !paymentMethod.equals("Card")) {
                savePaymentInfoToDatabase(username, totalAmount, paymentMethod);
                JOptionPane.showMessageDialog(this, "Payment Successful! Thank you.",
                        "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the payment window after successful payment
                new LoginRegistrationSystem(); // Redirect back to login screen
            }
        }
    }

    // Method to save payment information to the database
    private void savePaymentInfoToDatabase(String username, float totalAmount, String paymentMethod) {
        try {
            String query = "INSERT INTO PaymentInfo (username, total_amount, payment_method) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setFloat(2, totalAmount);
            statement.setString(3, paymentMethod);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to generate a random QR code
    private String generateRandomQRCode() {
        // Generate a random alphanumeric string as a simulated QR code
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder qrCode = new StringBuilder();
        Random rnd = new Random();
        while (qrCode.length() < 20) {
            int index = (int) (rnd.nextFloat() * characters.length());
            qrCode.append(characters.charAt(index));
        }
        return qrCode.toString();
    }
}
