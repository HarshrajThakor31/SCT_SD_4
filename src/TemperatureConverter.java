import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class TemperatureConverter extends JFrame {
    private JTextField inputField;
    private JComboBox<String> fromScale, toScale;
    private JLabel resultLabel, allResultsLabel, statusLabel;
    private JTextArea historyArea, favoritesArea;
    private JSpinner precisionSpinner;
    private JSlider tempSlider;
    private JCheckBox soundCheck;
    private ArrayList<String> history = new ArrayList<>();
    private ArrayList<String> favorites = new ArrayList<>();
    private boolean isDark = false;
    
    public TemperatureConverter() {
        setTitle("Ultimate Temperature Converter Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setupLayout();
        setupEvents();
        pack();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        inputField = new JTextField(12);
        fromScale = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin", "Rankine", "Réaumur"});
        toScale = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin", "Rankine", "Réaumur"});
        toScale.setSelectedIndex(1);
        
        tempSlider = new JSlider(-50, 150, 20);
        tempSlider.setMajorTickSpacing(50);
        tempSlider.setPaintTicks(true);
        tempSlider.setPaintLabels(true);
        
        precisionSpinner = new JSpinner(new SpinnerNumberModel(2, 0, 6, 1));
        
        resultLabel = new JLabel("Enter temperature to convert", SwingConstants.CENTER);
        resultLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        resultLabel.setBorder(BorderFactory.createEtchedBorder());
        resultLabel.setPreferredSize(new Dimension(300, 40));
        
        allResultsLabel = new JLabel("", SwingConstants.CENTER);
        allResultsLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        historyArea = new JTextArea(12, 25);
        historyArea.setEditable(false);
        historyArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        
        favoritesArea = new JTextArea(12, 25);
        favoritesArea.setEditable(false);
        
        soundCheck = new JCheckBox("Sound Effects", true);
        
        statusLabel = new JLabel("Ready - Enter temperature or use slider");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Temperature:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(inputField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        inputPanel.add(tempSlider, gbc);
        
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("From:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(fromScale, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("To:"), gbc);
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton swapBtn = new JButton("⇄");
        swapBtn.setPreferredSize(new Dimension(30, 25));
        swapBtn.addActionListener(e -> swapScales());
        toPanel.add(toScale);
        toPanel.add(swapBtn);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(toPanel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Precision:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(precisionSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(soundCheck, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 3, 3));
        JButton convertBtn = new JButton("Convert");
        JButton clearBtn = new JButton("Clear");
        JButton copyBtn = new JButton("Copy");
        JButton favoriteBtn = new JButton("★ Favorite");
        JButton randomBtn = new JButton("Random");
        JButton themeBtn = new JButton("Theme");
        
        convertBtn.addActionListener(e -> performConversion());
        clearBtn.addActionListener(e -> clearAll());
        copyBtn.addActionListener(e -> copyResult());
        favoriteBtn.addActionListener(e -> addToFavorites());
        randomBtn.addActionListener(e -> generateRandom());
        themeBtn.addActionListener(e -> toggleTheme());
        
        buttonPanel.add(convertBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(copyBtn);
        buttonPanel.add(favoriteBtn);
        buttonPanel.add(randomBtn);
        buttonPanel.add(themeBtn);
        
        gbc.gridy = 6;
        inputPanel.add(buttonPanel, gbc);
        
        add(inputPanel, BorderLayout.WEST);
        
        // Results panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Results"));
        
        JPanel resultsTop = new JPanel(new GridLayout(2, 1, 5, 5));
        resultsTop.add(resultLabel);
        resultsTop.add(allResultsLabel);
        resultPanel.add(resultsTop, BorderLayout.NORTH);
        
        // Thermometer visualization
        JPanel visualPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawThermometer(g);
            }
        };
        visualPanel.setPreferredSize(new Dimension(200, 200));
        resultPanel.add(visualPanel, BorderLayout.CENTER);
        
        add(resultPanel, BorderLayout.CENTER);
        
        // Right panel with tabs
        JTabbedPane rightTabs = new JTabbedPane();
        rightTabs.addTab("History", new JScrollPane(historyArea));
        rightTabs.addTab("Favorites", new JScrollPane(favoritesArea));
        
        // Quick reference
        JPanel quickPanel = new JPanel(new GridLayout(8, 1));
        quickPanel.add(new JLabel("Quick Reference:"));
        quickPanel.add(new JLabel("0°C = 32°F = 273.15K"));
        quickPanel.add(new JLabel("100°C = 212°F = 373.15K"));
        quickPanel.add(new JLabel("20°C = 68°F = 293.15K"));
        quickPanel.add(new JLabel("-40°C = -40°F"));
        quickPanel.add(new JLabel("37°C = 98.6°F (Body)"));
        quickPanel.add(new JLabel("-273.15°C = 0K"));
        rightTabs.addTab("Quick Ref", quickPanel);
        
        add(rightTabs, BorderLayout.EAST);
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        // Real-time conversion
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { convertRealTime(); }
            public void removeUpdate(DocumentEvent e) { convertRealTime(); }
            public void changedUpdate(DocumentEvent e) { convertRealTime(); }
        });
        
        // Slider updates input
        tempSlider.addChangeListener(e -> {
            if (!tempSlider.getValueIsAdjusting()) {
                inputField.setText(String.valueOf(tempSlider.getValue()));
            }
        });
        
        // Scale changes trigger conversion
        fromScale.addActionListener(e -> convertRealTime());
        toScale.addActionListener(e -> convertRealTime());
        precisionSpinner.addChangeListener(e -> convertRealTime());
        
        // Keyboard shortcuts
        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performConversion();
                }
            }
        });
        
        // Global shortcuts
        KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlR, "random");
        getRootPane().getActionMap().put("random", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { generateRandom(); }
        });
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "clear");
        getRootPane().getActionMap().put("clear", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { clearAll(); }
        });
    }
    
    private void convertRealTime() {
        try {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                double input = Double.parseDouble(text);
                String from = (String) fromScale.getSelectedItem();
                String to = (String) toScale.getSelectedItem();
                int precision = (Integer) precisionSpinner.getValue();
                
                double result = convert(input, from, to);
                String warning = isValidTemperature(input, from) ? "" : " ⚠️";
                
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(precision);
                df.setMinimumFractionDigits(precision);
                
                resultLabel.setText(df.format(result) + "° " + to + warning);
                
                // Show all conversions
                showAllConversions(input, from, precision);
                
                // Update status
                updateStatus(convertToCelsius(input, from));
                
                repaint();
            } else {
                resultLabel.setText("Enter temperature to convert");
                allResultsLabel.setText("");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid number format");
            allResultsLabel.setText("");
        }
    }
    
    private void showAllConversions(double input, String from, int precision) {
        double c = convertToCelsius(input, from);
        double f = convert(c, "Celsius", "Fahrenheit");
        double k = convert(c, "Celsius", "Kelvin");
        double r = convert(c, "Celsius", "Rankine");
        double re = convert(c, "Celsius", "Réaumur");
        
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(precision);
        df.setMinimumFractionDigits(precision);
        
        allResultsLabel.setText("<html><center>C: " + df.format(c) + "° | F: " + df.format(f) + 
            "° | K: " + df.format(k) + "°<br>R: " + df.format(r) + "° | Ré: " + df.format(re) + "°</center></html>");
    }
    
    private void performConversion() {
        try {
            double input = Double.parseDouble(inputField.getText().trim());
            String from = (String) fromScale.getSelectedItem();
            String to = (String) toScale.getSelectedItem();
            int precision = (Integer) precisionSpinner.getValue();
            
            double result = convert(input, from, to);
            
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(precision);
            df.setMinimumFractionDigits(precision);
            
            String conversion = df.format(input) + "° " + from + " → " + df.format(result) + "° " + to;
            
            history.add(conversion);
            historyArea.append(conversion + "\n");
            historyArea.setCaretPosition(historyArea.getDocument().getLength());
            
            if (soundCheck.isSelected()) {
                Toolkit.getDefaultToolkit().beep();
            }
            
            statusLabel.setText("Conversion completed: " + conversion);
        } catch (NumberFormatException ex) {
            statusLabel.setText("Error: Please enter a valid number");
        }
    }
    
    private double convert(double temp, String from, String to) {
        double celsius = convertToCelsius(temp, from);
        
        switch (to) {
            case "Fahrenheit": return celsius * 9.0 / 5.0 + 32;
            case "Kelvin": return celsius + 273.15;
            case "Rankine": return (celsius + 273.15) * 9.0 / 5.0;
            case "Réaumur": return celsius * 4.0 / 5.0;
            default: return celsius;
        }
    }
    
    private double convertToCelsius(double temp, String from) {
        switch (from) {
            case "Fahrenheit": return (temp - 32) * 5.0 / 9.0;
            case "Kelvin": return temp - 273.15;
            case "Rankine": return (temp - 491.67) * 5.0 / 9.0;
            case "Réaumur": return temp * 5.0 / 4.0;
            default: return temp;
        }
    }
    
    private boolean isValidTemperature(double temp, String scale) {
        double celsius = convertToCelsius(temp, scale);
        return celsius >= -273.15;
    }
    
    private void updateStatus(double celsius) {
        String context;
        if (celsius < -200) context = "Extremely cold - Outer space temperature";
        else if (celsius < -100) context = "Very cold - Dry ice temperature";
        else if (celsius < 0) context = "Below freezing point";
        else if (celsius < 20) context = "Cool temperature";
        else if (celsius < 30) context = "Room temperature";
        else if (celsius < 40) context = "Warm temperature";
        else if (celsius < 100) context = "Hot temperature";
        else if (celsius == 100) context = "Water boiling point";
        else context = "Very hot temperature";
        
        statusLabel.setText(context);
    }
    
    private void drawThermometer(Graphics g) {
        try {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                double temp = Double.parseDouble(text);
                double celsius = convertToCelsius(temp, (String) fromScale.getSelectedItem());
                
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int centerX = width / 2;
                
                // Realistic thermometer dimensions
                int tubeWidth = 12;
                int tubeTop = 40;
                int tubeBottom = height - 50;
                int bulbRadius = 18;
                int bulbCenterY = height - 32;
                
                // Glass tube outer shell
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRoundRect(centerX - tubeWidth/2 - 2, tubeTop - 5, tubeWidth + 4, tubeBottom - tubeTop + 10, 8, 8);
                
                // Glass tube inner chamber
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(centerX - tubeWidth/2, tubeTop, tubeWidth, tubeBottom - tubeTop, 6, 6);
                
                // Glass bulb outer
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillOval(centerX - bulbRadius - 2, bulbCenterY - bulbRadius - 2, (bulbRadius + 2) * 2, (bulbRadius + 2) * 2);
                
                // Glass bulb inner
                g2d.setColor(Color.WHITE);
                g2d.fillOval(centerX - bulbRadius, bulbCenterY - bulbRadius, bulbRadius * 2, bulbRadius * 2);
                
                // Calculate mercury level
                double normalizedTemp = Math.max(0, Math.min(1, (celsius + 50) / 200));
                int mercuryHeight = (int) ((tubeBottom - tubeTop - 10) * normalizedTemp);
                
                // Mercury color based on temperature
                Color mercuryColor;
                if (celsius < 0) mercuryColor = new Color(0, 100, 255);      // Blue for freezing
                else if (celsius < 20) mercuryColor = new Color(0, 150, 255); // Light blue for cold
                else if (celsius < 35) mercuryColor = new Color(0, 200, 100); // Green for normal
                else if (celsius < 60) mercuryColor = new Color(255, 150, 0); // Orange for warm
                else mercuryColor = new Color(255, 50, 50);                   // Red for hot
                
                // Mercury bulb (always filled)
                g2d.setColor(mercuryColor);
                g2d.fillOval(centerX - bulbRadius + 3, bulbCenterY - bulbRadius + 3, (bulbRadius - 3) * 2, (bulbRadius - 3) * 2);
                
                // Mercury column
                if (mercuryHeight > 0) {
                    g2d.fillRoundRect(centerX - tubeWidth/2 + 2, tubeBottom - mercuryHeight, tubeWidth - 4, mercuryHeight, 3, 3);
                }
                
                // Glass reflection effect
                GradientPaint glassReflection = new GradientPaint(
                    centerX - tubeWidth/2, 0, new Color(255, 255, 255, 100),
                    centerX - tubeWidth/2 + 4, 0, new Color(255, 255, 255, 0)
                );
                g2d.setPaint(glassReflection);
                g2d.fillRoundRect(centerX - tubeWidth/2, tubeTop, 4, tubeBottom - tubeTop, 3, 3);
                
                // Bulb reflection
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillOval(centerX - bulbRadius + 4, bulbCenterY - bulbRadius + 4, 8, 8);
                
                // Temperature scale markings
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                
                for (int i = 0; i <= 10; i++) {
                    int y = tubeTop + i * (tubeBottom - tubeTop) / 10;
                    int scaleTemp = 150 - i * 20;
                    
                    // Major tick marks
                    if (i % 2 == 0) {
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.drawLine(centerX + tubeWidth/2 + 2, y, centerX + tubeWidth/2 + 8, y);
                        g2d.drawString(scaleTemp + "°", centerX + tubeWidth/2 + 12, y + 3);
                    } else {
                        // Minor tick marks
                        g2d.setStroke(new BasicStroke(1f));
                        g2d.drawLine(centerX + tubeWidth/2 + 2, y, centerX + tubeWidth/2 + 6, y);
                    }
                }
                
                // Current temperature indicator
                if (mercuryHeight > 0) {
                    int indicatorY = tubeBottom - mercuryHeight;
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(2f));
                    g2d.drawLine(centerX - tubeWidth/2 - 5, indicatorY, centerX - tubeWidth/2 - 2, indicatorY);
                    
                    // Temperature value
                    DecimalFormat df = new DecimalFormat("0.0");
                    String tempText = df.format(celsius) + "°C";
                    g2d.setFont(new Font("Arial", Font.BOLD, 11));
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(tempText, centerX - tubeWidth/2 - 35, indicatorY + 4);
                }
                
                // Thermometer cap
                g2d.setColor(new Color(200, 200, 200));
                g2d.fillRoundRect(centerX - tubeWidth/2 - 1, tubeTop - 8, tubeWidth + 2, 8, 4, 4);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRoundRect(centerX - tubeWidth/2 - 1, tubeTop - 8, tubeWidth + 2, 8, 4, 4);
                
                // Scale label
                g2d.setFont(new Font("Arial", Font.BOLD, 10));
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawString("Celsius", centerX + tubeWidth/2 + 12, tubeTop + 15);
                
                // Reset stroke
                g2d.setStroke(new BasicStroke(1));
            }
        } catch (NumberFormatException ex) {
            // Don't draw if invalid input
        }
    }
    
    private void swapScales() {
        int fromIndex = fromScale.getSelectedIndex();
        fromScale.setSelectedIndex(toScale.getSelectedIndex());
        toScale.setSelectedIndex(fromIndex);
        statusLabel.setText("Scales swapped");
    }
    
    private void clearAll() {
        inputField.setText("");
        resultLabel.setText("Enter temperature to convert");
        allResultsLabel.setText("");
        tempSlider.setValue(20);
        statusLabel.setText("Cleared - Ready for new input");
    }
    
    private void copyResult() {
        String result = resultLabel.getText();
        if (!result.contains("Enter") && !result.contains("Invalid")) {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(result), null);
            statusLabel.setText("Result copied to clipboard");
        }
    }
    
    private void addToFavorites() {
        String text = inputField.getText().trim();
        if (!text.isEmpty() && !resultLabel.getText().contains("Enter") && !resultLabel.getText().contains("Invalid")) {
            String favorite = text + "° " + fromScale.getSelectedItem() + " → " + resultLabel.getText();
            favorites.add(favorite);
            favoritesArea.append(favorite + "\n");
            favoritesArea.setCaretPosition(favoritesArea.getDocument().getLength());
            statusLabel.setText("Added to favorites (" + favorites.size() + " total)");
        }
    }
    
    private void generateRandom() {
        Random rand = new Random();
        int randomTemp = rand.nextInt(201) - 50; // -50 to 150
        inputField.setText(String.valueOf(randomTemp));
        fromScale.setSelectedIndex(rand.nextInt(fromScale.getItemCount()));
        toScale.setSelectedIndex(rand.nextInt(toScale.getItemCount()));
        tempSlider.setValue(randomTemp);
        statusLabel.setText("Random temperature: " + randomTemp + "°");
    }
    
    private void toggleTheme() {
        isDark = !isDark;
        Color bg = isDark ? new Color(50, 50, 50) : Color.WHITE;
        Color fg = isDark ? Color.WHITE : Color.BLACK;
        
        updateColors(getContentPane(), bg, fg);
        statusLabel.setText("Theme: " + (isDark ? "Dark" : "Light"));
        repaint();
    }
    
    private void updateColors(Container container, Color bg, Color fg) {
        container.setBackground(bg);
        container.setForeground(fg);
        
        for (Component comp : container.getComponents()) {
            if (!(comp instanceof JButton)) {
                comp.setBackground(bg);
                comp.setForeground(fg);
            }
            if (comp instanceof Container) {
                updateColors((Container) comp, bg, fg);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TemperatureConverter().setVisible(true);
        });
    }
}