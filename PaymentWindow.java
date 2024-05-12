import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PaymentWindow extends JFrame implements ActionListener {
    private JRadioButton cashOption, cardOption, upiOption;
    private JButton payButton;

    public PaymentWindow() {
        setTitle("Payment Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        cashOption = new JRadioButton("Cash");
        cardOption = new JRadioButton("Card");
        upiOption = new JRadioButton("UPI");

        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cashOption);
        paymentGroup.add(cardOption);
        paymentGroup.add(upiOption);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(cashOption, gbc);

        gbc.gridy = 1;
        add(cardOption, gbc);

        gbc.gridy = 2;
        add(upiOption, gbc);

        payButton = new JButton("Pay");
        payButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(payButton, gbc);

        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == payButton) {
            String selectedOption = "";
            if (cashOption.isSelected()) {
                selectedOption = "Cash";
            } else if (cardOption.isSelected()) {
                selectedOption = "Card";
            } else if (upiOption.isSelected()) {
                selectedOption = "UPI";
            }

            if (!selectedOption.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Payment method selected: " + selectedOption,
                        "Payment Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a payment method!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PaymentWindow();
            }
        });
    }
}
