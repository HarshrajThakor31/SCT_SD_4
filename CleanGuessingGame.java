import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CleanGuessingGame extends JFrame {
    private int randomNumber, attempts;
    private JTextField guessField;
    private JLabel resultLabel, attemptsLabel, titleLabel;
    private JButton guessButton, newGameButton, websiteButton;
    private JComboBox<String> difficultyBox;
    private JProgressBar progressBar;
    private String[] difficulties = {"Easy (1-50)", "Medium (1-100)", "Hard (1-200)"};
    private int[] ranges = {50, 100, 200};
    
    public CleanGuessingGame() {
        setupUI();
        newGame();
    }
    
    private void setupUI() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Colors
        Color bgColor = new Color(245, 245, 245);
        Color primaryColor = new Color(70, 130, 180);
        
        getContentPane().setBackground(bgColor);
        
        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        topPanel.setBackground(primaryColor);
        
        JLabel gameTitle = new JLabel("🎯 Number Guessing Game");
        gameTitle.setFont(new Font("Arial", Font.BOLD, 18));
        gameTitle.setForeground(Color.WHITE);
        
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        diffLabel.setForeground(Color.WHITE);
        
        difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyBox.addActionListener(e -> newGame());
        
        topPanel.add(gameTitle);
        topPanel.add(Box.createHorizontalStrut(30));
        topPanel.add(diffLabel);
        topPanel.add(difficultyBox);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(bgColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Title
        titleLabel = new JLabel("Guess the number between 1 and 100");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        centerPanel.add(titleLabel, gbc);
        
        // Input row - centered
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputPanel.setBackground(bgColor);
        
        JLabel inputLabel = new JLabel("Your guess:");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(inputLabel);
        
        guessField = new JTextField(15);
        guessField.setFont(new Font("Arial", Font.PLAIN, 18));
        guessField.setPreferredSize(new Dimension(200, 40));
        guessField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) makeGuess();
            }
        });
        inputPanel.add(guessField);
        
        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Arial", Font.BOLD, 16));
        guessButton.setBackground(primaryColor);
        guessButton.setForeground(Color.WHITE);
        guessButton.setPreferredSize(new Dimension(100, 40));
        guessButton.addActionListener(e -> makeGuess());
        inputPanel.add(guessButton);
        
        gbc.gridwidth = 3; gbc.gridy = 1;
        centerPanel.add(inputPanel, gbc);
        
        // Result
        resultLabel = new JLabel("Click 'New Game' to start!");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setForeground(new Color(100, 100, 100));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        centerPanel.add(resultLabel, gbc);
        
        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Progress");
        progressBar.setFont(new Font("Arial", Font.PLAIN, 12));
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setForeground(primaryColor);
        gbc.gridy = 3;
        centerPanel.add(progressBar, gbc);
        
        // Attempts
        attemptsLabel = new JLabel("Attempts: 0");
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        attemptsLabel.setForeground(new Color(100, 100, 100));
        gbc.gridy = 4;
        centerPanel.add(attemptsLabel, gbc);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(bgColor);
        
        newGameButton = new JButton("🎮 New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        newGameButton.setBackground(new Color(34, 139, 34));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setPreferredSize(new Dimension(120, 35));
        newGameButton.addActionListener(e -> newGame());
        
        websiteButton = new JButton("🌐 Website");
        websiteButton.setFont(new Font("Arial", Font.BOLD, 14));
        websiteButton.setBackground(new Color(128, 128, 128));
        websiteButton.setForeground(Color.WHITE);
        websiteButton.setPreferredSize(new Dimension(120, 35));
        websiteButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new java.net.URI("file:///" + System.getProperty("user.dir").replace("\\", "/") + "/index.html"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Could not open website. Please open index.html manually.");
            }
        });
        
        bottomPanel.add(newGameButton);
        bottomPanel.add(websiteButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }
    
    private void makeGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText().trim());
            int range = ranges[difficultyBox.getSelectedIndex()];
            
            if (guess < 1 || guess > range) {
                resultLabel.setText("Please enter a number between 1 and " + range);
                resultLabel.setForeground(Color.RED);
                return;
            }
            
            attempts++;
            updateProgress(guess);
            
            if (guess == randomNumber) {
                resultLabel.setText("🎉 Correct! You won in " + attempts + " attempts!");
                resultLabel.setForeground(new Color(0, 128, 0));
                guessButton.setEnabled(false);
                progressBar.setValue(100);
                progressBar.setString("Perfect!");
            } else {
                String hint = guess < randomNumber ? "📈 Too low!" : "📉 Too high!";
                int diff = Math.abs(guess - randomNumber);
                if (diff <= 5) hint += " Very close!";
                else if (diff <= 15) hint += " Getting warmer!";
                
                resultLabel.setText(hint);
                resultLabel.setForeground(new Color(255, 140, 0));
            }
            
            guessField.setText("");
            attemptsLabel.setText("Attempts: " + attempts);
            guessField.requestFocus();
            
        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter a valid number!");
            resultLabel.setForeground(Color.RED);
        }
    }
    
    private void updateProgress(int guess) {
        int range = ranges[difficultyBox.getSelectedIndex()];
        int distance = Math.abs(guess - randomNumber);
        int progress = Math.max(0, 100 - (distance * 100 / range));
        progressBar.setValue(progress);
        progressBar.setString(progress + "% close");
    }
    
    private void newGame() {
        int range = ranges[difficultyBox.getSelectedIndex()];
        randomNumber = new Random().nextInt(range) + 1;
        attempts = 0;
        
        titleLabel.setText("🎯 Guess the number between 1 and " + range);
        resultLabel.setText("New game started! Good luck!");
        resultLabel.setForeground(new Color(100, 100, 100));
        attemptsLabel.setText("Attempts: 0");
        guessField.setText("");
        guessButton.setEnabled(true);
        progressBar.setValue(0);
        progressBar.setString("Start guessing!");
        guessField.requestFocus();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CleanGuessingGame());
    }
}