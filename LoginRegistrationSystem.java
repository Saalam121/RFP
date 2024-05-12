import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class LoginRegistrationSystem {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Connection connection;

    public LoginRegistrationSystem() {
        frame = new JFrame();
        frame.setTitle("Login / Registration");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Initialize components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this::loginButtonClicked);
        registerButton = new JButton("Register");
        registerButton.addActionListener(this::registerButtonClicked);

        // Set button colors
        loginButton.setBackground(new Color(59, 89, 182));
        loginButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(59, 89, 182));
        registerButton.setForeground(Color.WHITE);

        // Create panel for login form
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        frame.add(loginPanel);
        frame.setVisible(true);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/users_data";
            String username = "root"; // Change this if needed
            String password = ""; // Change this if needed
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to connect to the database!");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginRegistrationSystem::new);
    }

    private void loginButtonClicked(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(frame, "Login successful!");
            // Open the food ordering system window after successful login
            new FoodOrderingSystem1(connection, username);
            frame.dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password!");
        }
    }


    private void registerButtonClicked(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (registerUser(username, password)) {
            JOptionPane.showMessageDialog(frame, "Registration successful!");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to register user!");
        }
    }

    private boolean authenticateUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username=? AND BINARY password=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String username, String password) {
        try {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
