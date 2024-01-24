import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManagerApp {

    private static PasswordManager passwordManager;

    public static void main(String[] args) {
        passwordManager = new PasswordManager();

        SwingUtilities.invokeLater(() -> {
            createAndShowUI();
        });
   
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Password Manager Menu options:");
            System.out.println("1. Add Password");
            System.out.println("2. Password Creator for Website");
            System.out.println("3. Display All Passwords");
            System.out.println("4. Autofill Password");
            System.out.println("5. Retrieve Password");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    addPasswordUI();
                    break;
                case 2:
                    passwordCreatorUI();
                    break;
                case 3:
                    displayAllPasswordsUI();
                    break;
                case 4:
                    autofillPasswordUI();
                    break;
                case 5:
                    retrievePasswordUI();
                    break;
                case 6:
                    System.out.println("Exiting Password Manager. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Password Manager");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a panel with a custom background image
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Replace this path with the location of your image file
                ImageIcon imageIcon = new ImageIcon("Bg22.jpg");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add buttons with black background color and larger size
        addButton(imagePanel, "Add Password", 0, 0);
        addButton(imagePanel, "Password Creator for Website", 0, 1);
        addButton(imagePanel, "Display All Password", 0, 2);
        addButton(imagePanel, "Autofill Password", 0, 3);
        addButton(imagePanel, "Retrieve Password", 0, 4);
        addButton(imagePanel, "Exit", 0, 5);

        frame.add(imagePanel, BorderLayout.CENTER);
        frame.setVisible(true);
        centerFrameOnScreen(frame);
    }

    private static void addButton(JPanel panel, String text, int x, int y) {
        JButton button = createStyledButton(text);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(button, gbc);

        // Button actions
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform action based on the button clicked
                String buttonText = button.getText();
                switch (buttonText) {
                    case "Add Password":
                        addPasswordUI();
                        break;
                    case "Password Creator for Website":
                        passwordCreatorUI();
                        break;
                    case "Display All Password":
                        displayAllPasswordsUI();
                        break;
                    case "Autofill Password":
                        autofillPasswordUI();
                        break;
                    case "Retrieve Password":
                        retrievePasswordUI();
                        break;
                    case "Exit":
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Increased button padding
        return button;
    }

    private static void centerFrameOnScreen(JFrame frame) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    private static void addPasswordUI() {
        String website = JOptionPane.showInputDialog("Enter website:");
        String password = JOptionPane.showInputDialog("Enter password:");
        passwordManager.addPassword(website, password);
    }

    private static void passwordCreatorUI() {
        String website = JOptionPane.showInputDialog("Enter website for password creation:");
        String generatedPassword = passwordManager.createPasswordForWebsite(website);
        JOptionPane.showMessageDialog(null, "Generated Password for " + website + ": " + generatedPassword);
    }

    private static void displayAllPasswordsUI() {
        StringBuilder passwordInfo = new StringBuilder("Stored Passwords:\n");
        for (Map.Entry<String, String> entry : passwordManager.getPasswords().entrySet()) {
            passwordInfo.append("Website: ").append(entry.getKey()).append(" | Password: ").append(entry.getValue()).append("\n");
        }
        JOptionPane.showMessageDialog(null, passwordInfo.toString());
    }

    private static void autofillPasswordUI() {
        String website = JOptionPane.showInputDialog("Enter website to autofill password:");
        String password = passwordManager.autofillPassword(website);
        if (password != null) {
            JOptionPane.showMessageDialog(null, "Autofilling password for " + website + ": " + password);
        } else {
            JOptionPane.showMessageDialog(null, "Password not found for " + website);
        }
    }

    private static void retrievePasswordUI() {
        String website = JOptionPane.showInputDialog("Enter website to retrieve password:");
        String retrievedPassword = passwordManager.retrievePassword(website);
        if (retrievedPassword != null) {
            JOptionPane.showMessageDialog(null, "Retrieved Password for " + website + ": " + retrievedPassword);
        } else {
            JOptionPane.showMessageDialog(null, "Password not found for " + website);
        }
    }

    private static class PasswordManager {

        private Map<String, String> passwords;

        public PasswordManager() {
            passwords = new HashMap<>();
        }

        public Map<String, String> getPasswords() {
            return passwords;
        }

        public void addPassword(String website, String password) {
            passwords.put(website, password);
            System.out.println("Password added successfully for " + website);
        }

        public String createPasswordForWebsite(String website) {
            String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String lowerCase = "abcdefghijklmnopqrstuvwxyz";
            String digits = "0123456789";
            String specialChars = "!@#$%^&*()-_=+";
            String allChars = upperCase + lowerCase + digits + specialChars;

            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder();

            for (int i = 0; i < 10; i++) { // Adjust the length of the password as needed
                int randomIndex = random.nextInt(allChars.length());
                password.append(allChars.charAt(randomIndex));
            }

            String generatedPassword = password.toString();
            addPassword(website, generatedPassword);
            return generatedPassword;
        }

        public void displayPasswords() {
            System.out.println("Stored Passwords:");
            for (Map.Entry<String, String> entry : passwords.entrySet()) {
                System.out.println("Website: " + entry.getKey() + " | Password: " + entry.getValue());
            }
        }

        public String autofillPassword(String website) {
            String password = passwords.get(website);
            if (password != null) {
                System.out.println("Autofilling password for " + website + ": " + password);
            } else {
                System.out.println("Password not found for " + website);
            }
            return password;
        }

        public String retrievePassword(String website) {
            return passwords.get(website);
        }
    }
}