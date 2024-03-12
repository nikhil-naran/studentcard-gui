import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class GUIApp {
    private final StringBuilder inputText = new StringBuilder();
    private String lastScannedCode = null;

    public GUIApp() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridBagLayout()); // Main yellow panel
        panel.setBackground(new Color(250, 189, 15));
        GridBagConstraints gbc = new GridBagConstraints();

        // Create a new JPanel with a BorderLayout
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Add logo to the WEST (left) of the header panel
        ImageIcon logoIcon = new ImageIcon("logo.png"); // Replace with your logo file path
        Image logoImage = logoIcon.getImage().getScaledInstance(768/4, 584/4, Image.SCALE_SMOOTH); // Resize the image
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Add title to the CENTER of the header panel
        JLabel label = new JLabel("Queen's ID Portal", SwingConstants.CENTER);
        label.setFont(new Font("Open Sans", Font.BOLD, 35));
        label.setForeground(Color.decode("#9B0000")); // Set the color of the title to #9B0000
        headerPanel.add(label, BorderLayout.CENTER);

        // Add the header panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.125;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Position to the top left corner
        panel.add(headerPanel, gbc);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html"); // Set the content type to "text/html"
        textPane.setBorder(null); // Remove the border
        textPane.setBackground(new Color(250, 189, 15)); // Set the background color to match the panel
        textPane.setFont(new Font("Open Sans", Font.PLAIN, 70)); // Increase the font size to 70
        textPane.setBorder(new EmptyBorder(0, 50, 0, 0)); // Add padding to the left
        gbc.gridy = 2;
        panel.add(textPane, gbc);

        JLabel imageLabel = new JLabel();
        gbc.gridy = 3;
        panel.add(imageLabel, gbc);

        JTextField flexField = new JTextField();
        flexField.setVisible(false);

        // Mode text field
        JTextArea mode = new JTextArea("Mode: Meal Swipe");
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 1200, 10, 50);
        mode.setBackground(new Color(250, 189, 15));
        panel.add(mode, gbc);

        // Meal swipe button
        JButton mealSwipe = new JButton("Meal Swipe");
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 1200, 10, 50);
        panel.add(mealSwipe, gbc);
        mealSwipe.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mode.setText("Mode: Meal Swipe");
                flexField.setVisible(false);
                frame.requestFocus();
            }
        });

        // Tam button
        JButton tam = new JButton("TAM");
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 1200, 10, 50);
        panel.add(tam, gbc);
        tam.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mode.setText("Mode: TAM");
                flexField.setVisible(false);
                frame.requestFocus();
            }
        });

        // Flex button
        JButton flex = new JButton("Flex");
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 1200, 10, 50);
        panel.add(flex, gbc);
        flex.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                mode.setText("Mode: Flex");
                flexField.setVisible(true);
                frame.requestFocus();
            }
        });

        // Flex text field
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 1200, 10, 50);
        panel.add(flexField, gbc);
        flexField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                double flexAmount = 0;
                String flexValue = flexField.getText();
                boolean validFlex = true;
                int decimal = 0;
                if(!flexValue.isEmpty()) {
                    for(int i = 0; i < flexValue.length(); i++) {
                        if(!Character.isDigit(flexValue.charAt(i)) && flexValue.charAt(i) != '.') {
                            validFlex = false;
                        }
                        if(flexValue.charAt(i) == '.') {
                            decimal++;
                        }
                        if(decimal < 2 && i == flexValue.length() - 1 && validFlex) {
                            flexAmount = Double.parseDouble(flexValue);
                            flexAmount = Math.ceil(flexAmount * 100) / 100.0;
                            if(flexAmount > 0) {
                                mode.setText("Mode: Flex");
                                frame.requestFocus();
                            } else {
                                mode.setText("Invalid Flex Input!");
                            }
                        } else if(i == flexValue.length() - 1) {
                            mode.setText("Invalid Flex Input!");
                        }
                    }
                } else {
                    mode.setText("Invalid Flex Input!");
                }
            }
        });

        // Quit button
        JButton quit = new JButton("Quit");
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 1200, 10, 50);
        panel.add(quit, gbc);
        quit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (keyChar == '\n') {
                    StringBuilder htmlContent = new StringBuilder("<html><font size=\"7\">"); // Start the HTML content and set the font size
                    List<String[]> data = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("test.csv"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            if (values[0].trim().equals(inputText.toString().trim())) {
                                if (!values[0].trim().equals(lastScannedCode)) { // Check if the current scanned code is different from the last scanned code
                                    String modeValue = mode.getText();
                                    if(modeValue.equals("Mode: Meal Swipe")) {

                                        // Code for meal swipe subtraction

                                    } else if(modeValue.equals("Mode: TAM")) {
                                        int tamsLeft = Integer.parseInt(values[3].trim()) - 1; // Subtract one from the tams left
                                        values[3] = String.valueOf(tamsLeft); // Update the value in the array
                                    } else if(modeValue.equals("Mode: Flex")) {
                                        double flexAmount = Double.parseDouble(flexField.getText());
                                        flexAmount = Math.ceil(flexAmount * 100) / 100.0;

                                        // Code for flex subtraction
                                        // Use variable flexValue
                                    }
                                }
                                htmlContent.append("<font color=\"black\">Name: <b>" + values[1].trim() + "</b></font><br><br>"); // Prepend "Name: " to the output and add two line breaks, and set the color to black
                                htmlContent.append("Tam's Left: <b>" + values[3].trim() + "</b><br>"); // Append "Tam's Left: " and the current value
                                ImageIcon imageIcon = new ImageIcon(values[2].trim()); // Read the image name from the third column
                                Image image = imageIcon.getImage().getScaledInstance(640, 480, Image.SCALE_DEFAULT); // Resize the image
                                imageLabel.setIcon(new ImageIcon(image)); // Display the resized image
                                lastScannedCode = values[0].trim(); // Update the last scanned code
                            }
                            data.add(values); // Add the row data to the list
                        }
                        inputText.setLength(0); // Clear the input text
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Write the updated data back to the CSV file
                    try (PrintWriter pw = new PrintWriter(new FileWriter("test.csv"))) {
                        for (String[] row : data) {
                            pw.println(String.join(",", row));
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    htmlContent.append("</font></html>"); // End the font size and the HTML content
                    textPane.setText(htmlContent.toString()); // Set the HTML content to the JTextPane
                } else {
                    inputText.append(keyChar);
                }
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("GUI App");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setFocusable(true);
    }

    public static void main(String[] args) {
        new GUIApp();
    }
}